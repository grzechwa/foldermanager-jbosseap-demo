package com.greg.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the state database table.
 * 
 */
@Entity
@Table(name="state")
@NamedQuery(name="State.findAll", query="SELECT s FROM State s")
public class State implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idstate;

	private String state;

	//bi-directional many-to-one association to Dir
	@OneToMany(mappedBy="state")
	private List<Dir> dirs;

	public State() {
	}

	public int getIdstate() {
		return this.idstate;
	}

	public void setIdstate(int idstate) {
		this.idstate = idstate;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<Dir> getDirs() {
		return this.dirs;
	}

	public void setDirs(List<Dir> dirs) {
		this.dirs = dirs;
	}

	public Dir addDir(Dir dir) {
		getDirs().add(dir);
		dir.setState(this);

		return dir;
	}

	public Dir removeDir(Dir dir) {

        getDirs().remove(dir);
		dir.setState(null);

		return dir;
	}
	
	/*
	 * Metody potrzebne przy budowie Convera?
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
        return other instanceof State && (Integer.valueOf(idstate) != null) ? Integer.valueOf(idstate).equals(((State) other).idstate) : (other == this);
    }

	@Override
    public int hashCode() {
        return Integer.valueOf(idstate) != null ? this.getClass().hashCode() + Integer.valueOf(idstate).hashCode() : super.hashCode();
    }

}