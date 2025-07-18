package br.com.prf.leitordeplacas.service;

import br.com.prf.leitordeplacas.model.Crime;
import br.com.prf.leitordeplacas.model.Vehicle;
import br.com.prf.leitordeplacas.repository.VehicleRepository;
import br.com.prf.leitordeplacas.service.exception.PersonNotFoundException;
import br.com.prf.leitordeplacas.service.exception.ReportGenerationException;
import br.com.prf.leitordeplacas.service.exception.VehicleNotFoundException;
import br.com.prf.leitordeplacas.service.pdf.PdfPageEvents;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;

@Service
public class VehicleReportPdfService {

    private static final Logger logger = LoggerFactory.getLogger(VehicleReportPdfService.class);

    private final VehicleRepository vehicleRepository;

    private static final String DEFAULT_PLACEHOLDER = "placeHolder-Foto3x4.jpeg";

    public VehicleReportPdfService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional(readOnly = true)
    public byte[] generateVehicleReportPDF(String plate) {
        Vehicle vehicle = vehicleRepository.findByPlateWithDetails(plate)
                .orElseThrow(() -> new VehicleNotFoundException(plate));

        if (vehicle.getOwner() == null) {
            throw new PersonNotFoundException("Proprietário do Veículo não encontrado");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            // Carregar logo do sistema
            Image logo = null;
            try {
                URL logoUrl = getClass().getClassLoader().getResource("images/prf_logo.png");
                if (logoUrl != null) {
                    logo = Image.getInstance(logoUrl);
                } else {
                    logger.warn("Logo 'prf_logo.png' não encontrado em resources.");
                }
            } catch (IOException e) {
                logger.error("Erro ao carregar logo para o PDF: {}", e.getMessage(), e);
            }

            // Registrar evento de página
            writer.setPageEvent(new PdfPageEvents("Sistema de Leitura de Placas - PRF", logo));

            //ABRE DOCUMENTO
            document.open();

            // Fontes para título, subtítulo e conteúdo
            Font titleFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 24, Font.BOLD);
            Font sectionFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD);
            Font subSectionFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14);
            Font contentFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);
            Font titlePhotoFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, Color.WHITE);

            //Título do Relatório
            Paragraph title = new Paragraph("Relatório de Consulta de Placa", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Baixa foto do Proprietário
            try {
                String photoFileName = vehicle.getOwner().getPhotoFileName();
                URL photoUrl = null;

                if (photoFileName != null && !photoFileName.isEmpty()) {
                    photoUrl = getClass().getClassLoader().getResource("images/" + photoFileName);
                    if (photoUrl == null) {
                        logger.warn("Foto específica '{}' não encontrada. Usando placeholder padrão.", photoFileName);
                    }
                }

                if (photoUrl == null) {
                    photoUrl = getClass().getClassLoader().getResource("images/" + DEFAULT_PLACEHOLDER);
                }

                if (photoUrl != null) {
                    Image ownerPhoto = Image.getInstance(photoUrl);
                    ownerPhoto.scaleToFit(127.5f, 170f);

                    PdfPTable photoTable = new PdfPTable(1);
                    photoTable.setTotalWidth(132.5f);
                    photoTable.setLockedWidth(true);

                    PdfPCell titlePhotoCell = new PdfPCell(new Phrase("Foto do Proprietário", titlePhotoFont));
                    titlePhotoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    titlePhotoCell.setBackgroundColor(new Color(0x40, 0x40, 0x40));
                    titlePhotoCell.setPadding(4f);
                    photoTable.addCell(titlePhotoCell);

                    PdfPCell imageCell = new PdfPCell(ownerPhoto, false);
                    imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    imageCell.setPadding(2f);
                    photoTable.addCell(imageCell);

                    float xPosition = document.getPageSize().getWidth() - document.rightMargin() - ownerPhoto.getScaledWidth();
                    float yPosition = writer.getVerticalPosition(true);

                    photoTable.writeSelectedRows(0,
                            -1,
                            xPosition,
                            yPosition,
                            writer.getDirectContent());

                } else {
                    logger.error("Não foi possível encontrar nem a foto do proprietário nem o placeholder padrão.");
                }
            } catch (Exception e) {
                logger.error("Falha ao carregar a imagem do proprietário. O relatório será gerado sem a foto.", e);
            }

            //SEÇÃO: Dados do Veículo
            document.add(new Paragraph("DADOS DO VEÍCULO - PLACA [" + vehicle.getPlate() + "]:", sectionFont));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Tipo: " + vehicle.getVehicleType().getDisplayName(), contentFont));
            document.add(new Paragraph("Marca: " + vehicle.getBrand(), contentFont));
            document.add(new Paragraph("Modelo: " + vehicle.getModel(), contentFont));
            document.add(new Paragraph("Cor: " + vehicle.getColor(), contentFont));
            document.add(new Paragraph("Ano de fabricação: " + vehicle.getFabricationYear(), contentFont));
            document.add(new Paragraph("Status do IPVA: " + vehicle.getIpvaStatus().getDisplayName(), contentFont));
            document.add(Chunk.NEWLINE);

            //SEÇÃO: Dados do Proprietário
            if (vehicle.getOwner() != null) {

                document.add(new Paragraph("DADOS DO PROPRIETÁRIO:", sectionFont));
                document.add(Chunk.NEWLINE);

                document.add(new Paragraph("Nome completo: " + vehicle.getOwner().getFullName(), contentFont));
                document.add(new Paragraph("RG: " + vehicle.getOwner().getRg(), contentFont));
                document.add(new Paragraph("CPF: " + vehicle.getOwner().getCpf(), contentFont));
                document.add(new Paragraph("Data de nascimento: " + vehicle.getOwner().getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")), contentFont));
                document.add(new Paragraph("Gênero: " + vehicle.getOwner().getGender(), contentFont));
                document.add(new Paragraph("Categoria CNH: " + vehicle.getOwner().getLicenseCategory().getDisplayName(), contentFont));
                document.add(new Paragraph("Endereço: "
                        + vehicle.getOwner().getAddress().getStreet()
                        + ", " + vehicle.getOwner().getAddress().getNumber()
                        + ". Complemento: " + vehicle.getOwner().getAddress().getComplement()
                        + ". Bairro: " + vehicle.getOwner().getAddress().getNeighborhood(), contentFont));
                document.add(Chunk.NEWLINE);

                if (vehicle.getOwner().getCriminalRecord() != null
                        && vehicle.getOwner().getCriminalRecord().getCrimeList() != null
                        && !vehicle.getOwner().getCriminalRecord().getCrimeList().isEmpty()
                ) {

                    document.add(new Paragraph("FICHA CRIMINAL:", sectionFont));
                    document.add(Chunk.NEWLINE);

                    int contCrime = 1;
                    for (Crime c : vehicle.getOwner().getCriminalRecord().getCrimeList()) {

                        float estimatedCrimeBlockHeight = 100;

                        if (document.isOpen() && writer.getVerticalPosition(true) - estimatedCrimeBlockHeight < document.bottom()) {
                            document.newPage();
                            logger.debug("Forçando nova página antes do crime {}: espaço insuficiente na página atual.", contCrime);

                        }

                        PdfPTable crimeTable = new PdfPTable(2);
                        crimeTable.setWidthPercentage(100);
                        crimeTable.setSpacingBefore(10);
                        crimeTable.setSpacingAfter(10);
                        crimeTable.setSplitLate(false);

                        PdfPCell defaultCell = crimeTable.getDefaultCell();
                        defaultCell.setBorder(Rectangle.NO_BORDER);
                        defaultCell.setPadding(2);
                        defaultCell.setVerticalAlignment(Element.ALIGN_TOP);

                        crimeTable.addCell(new Phrase("Crime [" + contCrime + "]", subSectionFont));
                        crimeTable.addCell("");

                        crimeTable.addCell(new Phrase("Crime cometido:", contentFont));
                        crimeTable.addCell(new Phrase(c.getCrimeType(), contentFont));

                        crimeTable.addCell(new Phrase("Data e Horário:", contentFont));
                        crimeTable.addCell(new Phrase(c.getCrimeDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss")), contentFont));

                        crimeTable.addCell(new Phrase("Descrição:", contentFont));
                        crimeTable.addCell(new Phrase(c.getDescription(), contentFont));

                        crimeTable.addCell(new Phrase("Status:", contentFont));
                        crimeTable.addCell(new Phrase(c.getCrimeStatus().getDisplayName(), contentFont));

                        contCrime++;

                        document.add(crimeTable);
                    }
                } else {
                    document.add(new Paragraph("FICHA CRIMINAL VAZIA: ", sectionFont));
                    document.add(Chunk.NEWLINE);
                    document.add(new Paragraph("Não há crimes registrados para o proprietário deste veículo.", contentFont));
                }
                document.add(Chunk.NEWLINE);
            }

            //FECHA DOCUMENTO
            document.close();
        } catch (DocumentException e) {
            throw new ReportGenerationException("Falha ao gerar relatório PDF para veículo de placa: " + vehicle.getPlate());
        }

        return baos.toByteArray();
    }
}
