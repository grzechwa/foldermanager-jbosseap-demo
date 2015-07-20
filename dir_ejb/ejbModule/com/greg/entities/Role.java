package com.greg.entities;

import java.io.Serializable;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the role database table.
 * 
 */
@Entity
@Table(name="role")
@NamedQuery(name="Role.findAll", query="select r from Role r")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idrole;

	private String group;

	//bi-directional many-to-many association to User
	@ManyToMany
	@JoinTable(
		name="user_has_role"
		, joinColumns={
			@JoinColumn(name="idrole")
			}
		, inverseJoinColumns={
			@JoinColumn(name="iduser")
			}
		)
	private List<User> users;

	public Role() {
	}

	public int getIdrole() {
		return this.idrole;
	}

	public void setIdrole(int idrole) {
		this.idrole = idrole;
	}

	public String getGroup() {
		return this.group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}