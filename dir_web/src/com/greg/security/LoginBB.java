package com.greg.security;

import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpSession;

import com.greg.entities.User;
import com.greg.dao.UserDAO;

@ManagedBean
public class LoginBB {
	private static final String PAGE_AMAIN = 
			"/pages/admin/adminStart";
	private static final String PAGE_UMAIN = 
			"/pages/user/userStart";
	private static final String PAGE_LOGIN = 
			"/pages/login";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	@ManagedProperty("#{loginErr}")
	private ResourceBundle loginErr;
	
	public void setLoginErr(ResourceBundle loginErr) {
		this.loginErr = loginErr;
	}
	
	private ResourceBundle loginErrManual;
	
	@PostConstruct
	public void postConstruct(){
		FacesContext context = FacesContext.getCurrentInstance();
		loginErrManual = ResourceBundle.getBundle("resources.loginErr", 
				context.getViewRoot().getLocale());
	}

	private String name;
	private String pass;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public boolean validateData() {
		boolean result = true;
		FacesContext ctx = FacesContext.getCurrentInstance();

		// check if not empty
		if (name == null || name.length() == 0) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					 loginErrManual.getString("givename"), null));
		}

		if (pass == null || pass.length() == 0) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					 loginErrManual.getString("givepass"), null));
		}

		if (ctx.getMessageList().isEmpty()) {
			result = true;
		} else {
			result = false;
		}
		return result;

	}

	public String doLogin() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		User user = null;

		// 1. check parameters and stay if errors
		if (!validateData()) {
			return PAGE_STAY_AT_THE_SAME;
		}

		// 2. verify login and password - get User from "database"
		user = getUserFromDatabase(name, pass);

		// 3. if bad login or password - stay with error info
		if (user == null) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                loginErrManual.getString("faillogin"), null));
			return PAGE_STAY_AT_THE_SAME;
		}

		// 4. if login ok - save User object in session
		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(true);
		session.setAttribute("user", user);
		
		if (user.getName().equals("admin")){
		// and enter the system
			return PAGE_AMAIN;
		} else {
			return PAGE_UMAIN;
		}
	}

	public User getUser() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		return (User) session.getAttribute("user");
	}
	
	public String doLogout(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		//Invalidate session
		// - all objects within session will be destroyed
		// - new session will be created (with new ID)
		session.invalidate();
		return PAGE_LOGIN;
	}
	
	@EJB
	UserDAO userDAO;

	private User getUserFromDatabase(String login, String pass) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		User u = null;
		
		try{
			u = userDAO.findByName(login);
		}catch(NoResultException ex){
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					loginErrManual.getString("noentity"), null));
		}
		
		if(u == null){
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					loginErrManual.getString("noentity"), null));
			return u;
		}
			
		if (login.equals(u.getName()) && pass.equals("pass")) {
			return u;
		}
		
		return null;
	}

}
