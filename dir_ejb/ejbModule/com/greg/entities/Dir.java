package com.greg.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the dir database table.
 * 
 */
@Entity
@Table(name="dir")
@NamedQuery(name="Dir.findAll", query="SELECT d FROM Dir d")
public class Dir implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int iddir;

	private String name;

	private int size;

	//bi-directional many-to-one association to State
	@ManyToOne
	@JoinColumn(name="idstate")
	private State state;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="iduser")
	private User user = new User();

	public Dir() {
	}

	public int getIddir() {
		return this.iddir;
	}

	public void setIddir(int iddir) {
		this.iddir = iddir;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return this.size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public State getState() {
		return this.state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}