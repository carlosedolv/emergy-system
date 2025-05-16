package model.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import util.DateUtil;

public class User {
	private Integer id;
	private String name;
	private String email;
	private String password;
	private LocalDate birthday;
	private LocalDateTime registrationDate;
	private List<Simulation> simulations;
	
	public User() {
	}
	
	public User(String name, String email, String password, LocalDate birthday) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.birthday = birthday;
		this.registrationDate = LocalDateTime.now();
		this.simulations = new ArrayList<>();
	}
	
	public User(Integer id, String name, String email, String password, LocalDate birthday) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.birthday = birthday;
		this.registrationDate = LocalDateTime.now();
		this.simulations = new ArrayList<>();
	}
	
	@Override
	public String toString() {
		return "User [ id= " + id + ", name= " + name + ", email= " + email + ", birthday= " + birthday.format(DateUtil.DEAFULT_DATE_FORMAT) + " , createdAt= " + registrationDate.format(DateUtil.DEFAULT_DATETIME_FORMAT) + " ]";
	}
	
	public Integer getId() {
	    return id;
	}
	
	public void setId(Integer id) {
	    this.id = id;
	}
	
	public String getName() {
		return name;	
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String newEmail) {
		email = newEmail;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String newPassword) {
		password = newPassword;
	}
	
	public LocalDate getBirthday() {
		return birthday;
	}
	
	public void setBirthday(LocalDate newBirthday) {
		birthday = newBirthday;
	}
	
	public LocalDateTime getRegistrationDate() {
	    return registrationDate;
	}
	
	public List<Simulation> getSimulations() {
	    return simulations;
	}

	public void setSimulations(List<Simulation> simulations) {
	    this.simulations = simulations;
	}
	
	public void addSimulation(Simulation simulation) {
	    if (simulations == null) {
	        simulations = new ArrayList<>();
	    }
	    simulations.add(simulation);
	}
	
}
