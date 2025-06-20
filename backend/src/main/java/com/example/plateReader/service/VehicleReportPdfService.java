package com.example.plateReader.service;

import com.example.plateReader.model.Crime;
import com.example.plateReader.model.Vehicle;
import com.example.plateReader.repository.VehicleRepository;
import com.example.plateReader.service.exception.PersonNotFoundException;
import com.example.plateReader.service.exception.ReportGenerationException;
import com.example.plateReader.service.exception.VehicleNotFoundException;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class VehicleReportPdfService {

    private final VehicleRepository vehicleRepository;


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
            PdfWriter.getInstance(document, baos);
            document.open();

            // Fontes para título, subtítulo e conteúdo
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, Font.BOLD);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            //Título do Relatório
            Paragraph title = new Paragraph("Relatório de Consulta de Placa", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            //SEÇÃO: Dados do Veículo
            document.add(new Paragraph("DADOS DO VEÍCULO [" + vehicle.getPlate() + "]:", sectionFont));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Tipo: " + vehicle.getVehicleType().name(), contentFont));
            document.add(new Paragraph("Marca: " + vehicle.getBrand(), contentFont));
            document.add(new Paragraph("Modelo: " + vehicle.getModel(), contentFont));
            document.add(new Paragraph("Cor: " + vehicle.getColor(), contentFont));
            document.add(new Paragraph("Ano de fabricação: " + vehicle.getFabricationYear(), contentFont));
            document.add(new Paragraph("Status do IPVA: " + vehicle.getIpvaStatus().name(), contentFont));
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
                document.add(new Paragraph("Categoria CNH: " + vehicle.getOwner().getLicenseCategory().name(), contentFont));
                document.add(Chunk.NEWLINE);

                if (vehicle.getOwner().getCriminalRecord() != null
                        && vehicle.getOwner().getCriminalRecord().getCrimeList() != null
                        && !vehicle.getOwner().getCriminalRecord().getCrimeList().isEmpty()
                ) {

                    document.add(new Paragraph("FICHA CRIMINAL:", sectionFont));
                    document.add(Chunk.NEWLINE);

                    for (Crime c : vehicle.getOwner().getCriminalRecord().getCrimeList()) {
                        document.add(new Paragraph("Crime cometido: " + c.getCrimeType(), contentFont));
                        document.add(new Paragraph("Data do crime: " + c.getCrimeDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss")), contentFont));
                        document.add(new Paragraph("Descrição do crime: " + c.getDescription(), contentFont));
                        document.add(new Paragraph("Status do crime: " + c.getCrimeStatus().name(), contentFont));
                        document.add(Chunk.NEWLINE);
                    }
                } else {
                    document.add(new Paragraph("Não há crimes registrados para o proprietário deste veículo.", contentFont));
                }
                document.add(Chunk.NEWLINE);
            }

            document.close();
        } catch (DocumentException e) {
            throw new ReportGenerationException("Falha ao gerar relatório PDF para veículo de placa: " + vehicle.getPlate());
        }

        return baos.toByteArray();
    }
}
