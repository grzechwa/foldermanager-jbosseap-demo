package com.greg.user;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.greg.dao.RoleDAO;
import com.greg.dao.UserDAO;
import com.greg.entities.Role;
import com.greg.entities.User;

@ManagedBean
@ViewScoped
public class AdminEditBB{
	private static final String PAGE_START_LIST = 
			"adminStart?faces-reuserect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	@EJB
	UserDAO userDAO;
	@EJB
	RoleDAO roleDAO;

	private String iduser;
	private String name;
	private String idrole;
	private String group;
	private List<Role> roles;

	
	// getters, setters (properties used at the JSF page)
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

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
	
	public String getIdrole() {
		return idrole;
	}

	public void setIdrole(String idrole) {
		this.idrole = idrole;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRole(List<Role> roles) {
		this.roles = roles;
	}

	// object to be edited/inserted
	private User user = null;
	private Role role = null;

	@PostConstruct
	public void postConstruct() {
		// A. load user if passed through session
		HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSession(true);
		user = (User) session.getAttribute("user");
		
		// cleaning: attribute received => delete it from session
		if (user != null) {
			session.removeAttribute("user");
		}

		// if loaded record is to be edited then copy data to properties
		if (user != null && (Integer) (user.getIduser()) != null) {
			setName(user.getName());
			setIduser(String.valueOf(user.getIduser()));
			setRoles(user.getRoles());
		}
	}

	private boolean validate() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		boolean result = false;

		if (name == null || name.trim().length() == 0) {
			ctx.addMessage(null, new FacesMessage("imie wymagane" + name));
		}

		// if no errors
		if (ctx.getMessageList().isEmpty()) {
			user.setName(name.trim());
			result = true;
		}

		return result;
	}

	public String saveData() {

		// no user object passed
		if (user == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Bledzne uzycie systemu"));
			return PAGE_STAY_AT_THE_SAME;
		}

		if (!validate()) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if ((Integer) user.getIduser() == null) {
				// new record
				userDAO.create(user);
			} else {
				// existing record
				userDAO.merge(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("wystapil blad podczas zapisu"));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_START_LIST;
	}
	
	
	public String addRole(String iduser, String idrole){
				if(!validate()){
					return PAGE_STAY_AT_THE_SAME;
				}
				try {
					Integer idu = Integer.valueOf(iduser);
					Integer idr = Integer.valueOf(idrole);

					role = roleDAO.find(idr);
					user = userDAO.find(idu);

					userDAO.insertRole(idu, role);
                } catch (Exception e) {
					e.printStackTrace();
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage("Dodawanie nie powiodlo sie" ));
					return PAGE_STAY_AT_THE_SAME;
				}
        return PAGE_STAY_AT_THE_SAME;
	}
	
	public String delRole(String iduser, String idrole){
		try {
			Integer idu = Integer.valueOf(iduser);
			Integer idr = Integer.valueOf(idrole);

			role = roleDAO.find(idr);
			user = userDAO.find(idu);

			userDAO.deleteRole(idu, role);
        } catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Usuwanie roli nie powiodlo sie" ));
			return PAGE_STAY_AT_THE_SAME;
		}
		return PAGE_STAY_AT_THE_SAME;
	}
	/*
	public String removeUser(User user){
		System.out.println("Przed User: " + user);
		// user.getRoles().clear();
		
		// userDAO.remove(user);
		
		System.out.println("Po User: " + user);

		return PAGE_START_LIST;
	}
	*/
	
	
	
}
