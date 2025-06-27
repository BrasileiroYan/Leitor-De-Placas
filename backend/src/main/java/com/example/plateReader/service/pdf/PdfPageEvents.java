package com.example.plateReader.service.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PdfPageEvents extends PdfPageEventHelper {

    private static final Logger logger = LoggerFactory.getLogger(PdfPageEvents.class);

    private String systemName;
    private Image logo;

    public PdfPageEvents(String systemName, Image logo) {
        this.systemName = systemName;
        this.logo = logo;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte contentByte = writer.getDirectContent();

        Rectangle headerBox = new Rectangle(document.left(), document.top(), document.right(), document.top() + 30);
        Rectangle footerBox = new Rectangle(document.left(), document.bottom() - 20, document.right(), document.bottom());


        // CABEÇALHO
        if (logo != null) {
            try {
                logo.scaleToFit(23.45f, 23.45f);
                logo.setAbsolutePosition(document.left(), document.top() + 5);
                contentByte.addImage(logo);
            } catch (DocumentException e) {
                logger.error("Erro ao adicionar logo ao PDF: {}", e.getMessage(), e);
            }
        }

        Font headerFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10);
        Phrase headerPhrase = new Phrase(systemName, headerFont);
        ColumnText.showTextAligned(
                contentByte,
                Element.ALIGN_RIGHT,
                headerPhrase,
                headerBox.getRight(),
                headerBox.getTop() - 10,
                0
        );

        // RODAPÉ
        Font footerFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9);
        Phrase footerPhrase = new Phrase("Página "+ writer.getPageNumber(), footerFont);
        ColumnText.showTextAligned(
                contentByte,
                Element.ALIGN_CENTER,
                footerPhrase,
                (document.left() + document.right()) / 2,
                footerBox.getBottom() + 5,
                0
        );

        Phrase datePhrase = new Phrase("Gerado em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")), footerFont);
        ColumnText.showTextAligned(
                contentByte,
                Element.ALIGN_RIGHT,
                datePhrase,
                document.right() - 5,
                footerBox.getBottom() + 5,
                0
        );

        // LINHA PARA SEPARAR CABEÇALHO E RODAPÉ
        contentByte.setLineWidth(0.5f);
        contentByte.moveTo(document.left(), document.top() + 0);
        contentByte.lineTo(document.right(), document.top() + 0);
        contentByte.stroke();

        // MARCA D'ÁGUA
        float positionX = (document.left() + document.right()) / 2;
        float positionY = (document.top() + document.bottom()) / 2;

        Font waterMarkFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 110, Font.BOLD, Color.lightGray);

        PdfGState pdfGState = new PdfGState();
        pdfGState.setFillOpacity(0.18f); // Nível de opacidade (vai de 0.0 a 1.0)
        contentByte.saveState();
        contentByte.setGState(pdfGState);

        Phrase waterMarkPhrase = new Phrase("CONFIDENCIAL", waterMarkFont);

        ColumnText.showTextAligned(
                contentByte,
                Element.ALIGN_CENTER,
                waterMarkPhrase,
                positionX,
                positionY,
                54.73f  // Angulação calculada com base nas dimensões de uma folha A4
        );
        contentByte.restoreState();
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        if (writer.getPageNumber() > 1) {
            try {
                document.add(new Paragraph(" "));
                document.add(Chunk.NEWLINE);
                document.setPageSize(PageSize.A4);
            } catch (DocumentException e) {
                logger.error("Erro ao pular linha no início da página: {}", e.getMessage(), e);
            }
        }
    }
}
