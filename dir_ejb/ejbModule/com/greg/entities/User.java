package com.greg.entities;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="user")
@NamedQuery(name="User.findAll", query="select u from User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int iduser;

	private String name;

	//bi-directional many-to-one association to Dir
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	private List<Dir> dirs;

	//bi-directional many-to-many association to Role
	@ManyToMany(mappedBy="users")

	private List<Role> roles;

	public User() {
	}

	public int getIduser() {
		return this.iduser;
	}

	public void setIduser(int iduser) {
		this.iduser = iduser;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Dir> getDirs() {
		return this.dirs;
	}

	public void setDirs(List<Dir> dirs) {
		this.dirs = dirs;
	}

	public Dir addDir(Dir dir) {
		getDirs().add(dir);
		dir.setUser(this);

		return dir;
	}

	public Dir removeDir(Dir dir) {
		getDirs().remove(dir);
		dir.setUser(null);

		return dir;
	}

	
	public List<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}