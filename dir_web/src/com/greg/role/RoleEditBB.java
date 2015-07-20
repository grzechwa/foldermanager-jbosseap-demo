package com.greg.role;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.greg.dao.RoleDAO;
import com.greg.dao.UserDAO;
import com.greg.entities.Role;
import com.greg.entities.User;

@ManagedBean
@ViewScoped
public class RoleEditBB{
	private static final String PAGE_STAY_AT_THE_SAME = null;
;
	@EJB
	UserDAO userDAO;
	@EJB
	RoleDAO roleDAO;

	private String iduser;
	private String name;
	private String idrole;
	private String group;
	private List<User> users; 

	// getters, setters (properties used at the JSF page)
	public void setIduser(String iduser) {
		this.iduser = iduser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
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

	public String getIduser() {
		return iduser;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}


	// object to be edited/inserted
	private Role role = null;
	private User user = null;
	
	@PostConstruct
	public void postConstruct() {
		// A. load user if passed through session
		HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSession(true);
		role = (Role) session.getAttribute("role");

		// if loaded record is to be edited then copy data to properties
		if (role != null && (Integer) (role.getIdrole()) != 0) {
			setIdrole(String.valueOf(role.getIdrole()));
			setGroup(role.getGroup());
			setUsers(role.getUsers());
		}
}

	private boolean validate() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		boolean result = false;

		if (group == null || group.trim().length() == 0) {
			ctx.addMessage(null, new FacesMessage("nazwa grupuy wymagana"));
		}

		// if no errors
		if (ctx.getMessageList().isEmpty()) {
			role.setGroup(group.trim());
			result = true;
		}

		return result;
	}

	public String saveData() {

		// no user object passed
		if (role == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Bledne uzycie systemu"));
			return PAGE_STAY_AT_THE_SAME;
		}

		if (!validate()) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if ((Integer) role.getIdrole() == null) {
				// new record
				roleDAO.create(role);
			} else {
				// existing record
				roleDAO.merge(role);
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("wystapil blad podczas zapisu"));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String addRole(){
		HttpServletRequest request = (HttpServletRequest)FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getRequest();
        String txtProperty = request.getParameter("myForm:txtProperty");
        String txtAnotherProperty= request.getParameter("txtAnotherProperty");
		roleDAO.createRole(txtAnotherProperty);
		return PAGE_STAY_AT_THE_SAME;
	}
	
 	public String addUser(String idrole, String iduser){
		try {
			Integer idu = Integer.valueOf(iduser);
			Integer idr = Integer.valueOf(idrole);
			role = roleDAO.find(idr);
			user = userDAO.find(idu);
			roleDAO.insertUser(idr, user);
        } catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Dodawanie usera nie powiodlo sie" ));
			return PAGE_STAY_AT_THE_SAME;
		}
        return PAGE_STAY_AT_THE_SAME;
	}
	
	public String delUser(String idrole, Integer iduser){
		try {
			Integer idu = Integer.valueOf(iduser);
			Integer idr = Integer.valueOf(idrole);
			Integer id = Integer.valueOf(iduser);

			role = roleDAO.find(idr);
			user = userDAO.find(idu);

			roleDAO.deleteUser(idr, id);
        } catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Usuwanie uzytkownika nie powiodlo sie" ));
			return PAGE_STAY_AT_THE_SAME;
		}
		return PAGE_STAY_AT_THE_SAME;
	}
}
