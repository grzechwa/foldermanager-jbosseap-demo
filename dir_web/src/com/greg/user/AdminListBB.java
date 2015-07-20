package com.greg.user;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.greg.dao.DirDAO;
import com.greg.dao.UserDAO;
import com.greg.entities.Role;
import com.greg.entities.User;

@ManagedBean
@SessionScoped
public class AdminListBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_DIR_EDIT = 
			"userEdit.xhtml?faces-redirect=true";
	private static final String PAGE_AUSER_EDIT = 
			"aUserAdd.xhtml?faces-redirect=true";
	private static final String PAGE_USERROLE_ADD = 
			"urAdminAdd.xhtml?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private String iduser;
	private String name;
	private List<Role> roles;
	private String role;
	private String idrole;
	private String group;

	
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getIdrole() {
		return idrole;
	}

	public void setIdrole(String idrole) {
		this.idrole = idrole;
	}

	private Role roleOb;

	public Role getRoleOb() {
		return roleOb;
	}

	public void setRoleOb(Role roleOb) {
		this.roleOb = roleOb;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	// getters and setters
	public String getIduser() {
		return iduser;
	}

	public void setIduser(String iduser) {
		this.iduser = iduser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//Dependency injection
	// - no setter method needed in this case
	@EJB
	UserDAO userDAO;
	@EJB
	DirDAO dirDAO;
	
	public List<User> getFullList(){
		return userDAO.getFullList();
	}

	public List<User> getList(){
		List<User> list = null;
		
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();

		if (name != null && name.length() > 0){
			searchParams.put("name", name);
		}

		
		//2. Get list
		list = userDAO.getList(searchParams);
		
		return list;
	}
	
	public String newUser(){
		HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSession(true);
		User user = new User();
		session.setAttribute("user", user);
		return PAGE_AUSER_EDIT;
	}

	public String editUser(User user){
		HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSession(true);
		session.setAttribute("user", user);
		return PAGE_DIR_EDIT;
	}

	public String deleteUser(User user){
		userDAO.removeUser(user);
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String addRole(User user, Role role){
		HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSession(true);
		session.setAttribute("user", user);
		session.setAttribute("role", role);

		return PAGE_USERROLE_ADD;
	}
	
}
