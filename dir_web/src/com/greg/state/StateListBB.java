package com.greg.state;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.greg.dao.StateDAO;
import com.greg.entities.State;

@ManagedBean
@ViewScoped
public class StateListBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_DIR_EDIT = 
			"stateEdit.xhtml?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private String idstate;
	private String state;

	// getters and setters
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
	
	//Dependency injection
	// - no setter method needed in this case
	@EJB
	StateDAO stateDAO;
	
	public List<State> getFullList(){
		return stateDAO.getFullList();
	}

	public List<State> getList(){
		List<State> list = null;
		
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();

		if (state != null && state.length() > 0){
			searchParams.put("state", state);
		}

		
		//2. Get list
		list = stateDAO.getList(searchParams);
		
		return list;
	}
	
	public String newState(){
		HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSession(true);
		State state = new State();
		session.setAttribute("state", state);
		return PAGE_DIR_EDIT;
	}

	public String editState(State state){
		HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSession(true);
		session.setAttribute("state", state);
		return PAGE_DIR_EDIT;
	}

	public String deleteDir(State state){
		stateDAO.remove(state);
		return PAGE_STAY_AT_THE_SAME;
	}
}
