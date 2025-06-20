package com.example.plateReader.service.pdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                logo.scaleToFit(23.5f, 23.5f);
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

        // LINHA PARA SEPARAR CABEÇALHO E RODAPÉ
        contentByte.setLineWidth(0.5f);
        contentByte.moveTo(document.left(), document.top() + 0);
        contentByte.lineTo(document.right(), document.top() + 0);
        contentByte.stroke();
    }
}
