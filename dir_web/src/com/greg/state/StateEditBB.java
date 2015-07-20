package com.greg.state;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.greg.dao.StateDAO;
import com.greg.entities.State;

@ManagedBean
@ViewScoped
public class StateEditBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_START_LIST = 
			"userStart.xhtml?faces-reuserect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	@EJB
	StateDAO stateDAO;

	private String idstate;
	private String state;

	// getters, setters (properties used at the JSF page)
	public String getIdstate() {
		return idstate;
	}

	public void setIdstate(String idstate) {
		this.idstate = idstate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	// object to be edited/inserted
	private State st = null;
	
	@PostConstruct
	public void postConstruct() {
		// A. load user if passed through session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		st = (State) session.getAttribute("st");

		// cleaning: attribute received => delete it from session
		if (st != null) {
			session.removeAttribute("st");
		}

		// B. if object not loaded try to get user by id passed as GET
		// parameter
		if (st == null) {
			HttpServletRequest req = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			state = req.getParameter("s");
			if (st != null) {
				// parse id
				Integer id = null;
				try {
					id = Integer.parseInt(state);
				} catch (NumberFormatException e) {
				}
				if (id != null) {
					// if parsing successful
					st = stateDAO.find(id);
				}
			}
		}

		// if loaded record is to be edited then copy data to properties
		if (st != null && (Integer) (st.getIdstate()) != null) {
			setIdstate(String.valueOf(st.getIdstate()));
			setState(st.getState());
		}
	}

	private boolean validate() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		boolean result = false;

		if (state == null || state.trim().length() == 0) {
			ctx.addMessage(null, new FacesMessage("imi� wymagane"));
		}

		// if no errors
		if (ctx.getMessageList().isEmpty()) {
			st.setState(state.trim());
			
			result = true;
		}

		return result;
	}

	public String saveData() {

		// no user object passed
		if (st == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Bledne uzycie systemu"));
			return PAGE_STAY_AT_THE_SAME;
		}

		if (!validate()) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if ((Integer) st.getIdstate() == null) {
				// new record
				stateDAO.create(st);
			} else {
				// existing record
				stateDAO.merge(st);
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("wystapil blad podczas zapisu"));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_START_LIST;
	}
	
}
