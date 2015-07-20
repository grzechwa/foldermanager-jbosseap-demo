package com.greg.role;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.greg.dao.RoleDAO;
import com.greg.entities.Role;

@ManagedBean
public class RoleListBB implements Serializable{
	private static final long serialVersionUID = 1L;

	private static final String PAGE_DIR_EDIT = 
			"roleEdit.xhtml?faces-redirect=true";
	private static final String PAGE_AROLE_EDIT = 
			"rAdminAdd?faces-redirect=true";
	private static final String PAGE_USERROLE_ADD = 
			"ruAdminAdd?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private String idrole;
	private String group;
	private String user;

	// getters and setters
	public String getIdrole() {
		return idrole;
	}

	public void setIdrole(String idrole) {
		this.idrole = idrole;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	//Dependency injection
	// - no setter method needed in this case
	@EJB
	RoleDAO roleDAO;

	public List<Role> getFullList(){
		return roleDAO.getFullList();
	}

	public List<Role> getList(){
		List<Role> list = null;
		
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();

		if (group != null && group.length() > 0){
			searchParams.put("group", group);
		}

		
		//2. Get list
		list = roleDAO.getList(searchParams);
		
		return list;
	}
	
	public String newRole(){
		HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSession(true);
		Role role = new Role();
		session.setAttribute("role", role);
		return PAGE_AROLE_EDIT;
	}
	
	public String addUserRole(Role role){
		HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSession(true);
		session.setAttribute("role", role);
		return PAGE_USERROLE_ADD;
	}

	public String editRole(Role role){
		HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSession(true);
		session.setAttribute("role", role);
		return PAGE_DIR_EDIT;
	}

	// ok
	public String deleteRole(Role role){
		roleDAO.remove(role);
		return PAGE_STAY_AT_THE_SAME;
	}
}
