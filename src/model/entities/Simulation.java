package model.entities;

import java.time.LocalDateTime;
import util.DateUtil;

public class Simulation {

    private Integer id;
    private Integer userId; 
    private String title;
    private float litros;
    private String tipo;
    private float result;
    private LocalDateTime simulationDate;

	public Simulation(Integer userId, String title, float litros, String tipo, float result, LocalDateTime simulationDate) {
        this.userId = userId;
        this.title = title;
        this.litros = litros;
        this.tipo = tipo;
        this.result = result;
        this.simulationDate = simulationDate;
    }

	public Simulation(Integer id, Integer userId, String title, float litros, String tipo, float result, LocalDateTime simulationDate) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.litros = litros;
        this.tipo = tipo;
        this.result = result;
        this.simulationDate = simulationDate;
    }

    public float getResult() {
		return result;
	}

	public void setResult(float result) {
		this.result = result;
	}

    public float getLitros() {
		return litros;
	}

	public void setLitros(float litros) {
		this.litros = litros;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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

    public LocalDateTime getSimulationDate() {
        return simulationDate;
    }

    public void setSimulationDate(LocalDateTime simulationDate) {
        this.simulationDate = simulationDate;
    }

    @Override
    public String toString() {
        return "Simulation [ id=" + id + ", userId= " + userId + ", title=" + title + ", litros=" + litros + ", tipo=" + tipo + ", date=" + simulationDate.format(DateUtil.DEFAULT_DATETIME_FORMAT) + " ]";
    }
}
