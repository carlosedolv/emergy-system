package util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import model.entities.Simulation;

public class PdfExporter {

    public static void exportSimulationComparativo0(List<Simulation> sims, BufferedImage chartImage, File destinationFile) throws IOException, DocumentException {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(destinationFile));
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Relatório Comparativo de Simulações", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" ")); // espaço

        for (Simulation sim : sims) {
            document.add(new Paragraph("Título: " + sim.getTitle()));
            document.add(new Paragraph("Tipo: " + sim.getTipo()));
            document.add(new Paragraph(String.format("Litros: %.2f", sim.getLitros())));
            document.add(new Paragraph(String.format("Resultado: %.2f x10¹² sej", sim.getResult())));
            document.add(new Paragraph("Data: " + sim.getSimulationDate().toString()));
            document.add(new Paragraph(" "));
        }

        // Converte BufferedImage para byte[] para inserir como Image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", baos);
        Image chart = Image.getInstance(baos.toByteArray());

        chart.setAlignment(Image.ALIGN_CENTER);
        chart.scaleToFit(500, 300);
        document.add(chart);

        document.close();
    }
    
    public static void exportSimulationComparativo(List<Simulation> sims, List<BufferedImage> chartImages, File destinationFile)
            throws IOException, DocumentException {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(destinationFile));
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Relatório Comparativo de Simulações", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" ")); // espaço

        for (Simulation sim : sims) {
            document.add(new Paragraph("Título: " + sim.getTitle()));
            document.add(new Paragraph("Tipo: " + sim.getTipo()));
            document.add(new Paragraph(String.format("Litros: %.2f", sim.getLitros())));
            document.add(new Paragraph(String.format("Resultado: %.2f x10¹² sej", sim.getResult())));
            document.add(new Paragraph("Data: " + sim.getSimulationDate().toString()));
            document.add(new Paragraph(" "));
        }

        // Adiciona todos os gráficos selecionados
        for (BufferedImage chartImage : chartImages) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(chartImage, "png", baos);
            Image chart = Image.getInstance(baos.toByteArray());

            chart.setAlignment(Image.ALIGN_CENTER);
            chart.scaleToFit(500, 300);
            document.add(chart);
            document.add(new Paragraph(" ")); // espaço entre gráficos
        }

        document.close();
    }

}
