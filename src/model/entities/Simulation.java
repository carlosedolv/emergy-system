package model.entities;

import java.time.LocalDateTime;
import util.DateUtil;

public class Simulation {

    private Integer id;
    private Integer userId; 
    private String title;
    private String simulationData; // JSON contendo entradas, resultado e gráfico
    private LocalDateTime simulationDate;

    public Simulation() {
    }

    public Simulation(Integer userId, String title, String simulationData, LocalDateTime simulationDate) {
        this.userId = userId;
        this.title = title;
        this.simulationData = simulationData;
        this.simulationDate = simulationDate;
    }

    public Simulation(Integer id, Integer userId, String title, String simulationData, LocalDateTime simulationDate) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.simulationData = simulationData;
        this.simulationDate = simulationDate;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSimulationData() {
        return simulationData;
    }

    public void setSimulationData(String simulationData) {
        this.simulationData = simulationData;
    }

    public LocalDateTime getSimulationDate() {
        return simulationDate;
    }

    public void setSimulationDate(LocalDateTime simulationDate) {
        this.simulationDate = simulationDate;
    }

    @Override
    public String toString() {
        return "Simulation [ id=" + id + ", userId= " + userId + ", title=" + title + ", date=" + simulationDate.format(DateUtil.DEFAULT_DATETIME_FORMAT) + " ]";
    }
}
