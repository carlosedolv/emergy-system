package util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import model.entities.Simulation;

public class ChartGenerator {

    /**
     * Gera um gráfico de barras representando a emergia calculada
     * para a simulação fornecida.
     */
    public static BufferedImage generateChart(Simulation sim, int width, int height) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(sim.getResult(), "Resultado", sim.getTipo());

        JFreeChart barChart = ChartFactory.createBarChart(
                "Emergia por Tipo de Biocombustível",
                "Tipo",
                "Emergia (x10¹² sej)",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        // Customização visual
        CategoryPlot plot = (CategoryPlot) barChart.getPlot();
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setOutlinePaint(Color.GRAY);

        return barChart.createBufferedImage(width, height);
    }
    
    
 // Método para gerar imagem diretamente
    public static BufferedImage generateChart(List<Simulation> simulations, int width, int height) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Simulation sim : simulations) {
            dataset.addValue(sim.getResult(), "Emergia (sej)", sim.getTitle());
        }

        JFreeChart chart = ChartFactory.createBarChart(
            "Comparativo de Simulações",
            "Título",
            "Emergia (x10¹² sej)",
            dataset
        );

        return chart.createBufferedImage(width, height);
    }
    
    public static BufferedImage generatePieChart(List<Simulation> simulations, int width, int height) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Simulation sim : simulations) {
            dataset.setValue(sim.getTitle(), sim.getResult());
        }

        JFreeChart chart = ChartFactory.createPieChart(
            "Distribuição de Energia entre Simulações",
            dataset,
            true,
            true,
            false
        );

        return chart.createBufferedImage(width, height);
    }
    
    public static BufferedImage generateLineChart(List<Simulation> simulations, int width, int height) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Cria cópia mutável para ordenação
        List<Simulation> sortedSimulations = new ArrayList<>(simulations);
        sortedSimulations.sort(Comparator.comparing(Simulation::getSimulationDate));

        for (Simulation sim : sortedSimulations) {
            dataset.addValue(sim.getResult(), "Emergia", sim.getTitle());
        }

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Evolução das Simulações",
                "Simulação",
                "Emergia (x10¹² sej)",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        return lineChart.createBufferedImage(width, height);
    }



}
