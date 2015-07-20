package com.greg.dir;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.greg.dao.DirDAO;
import com.greg.dao.StateDAO;
import com.greg.dao.UserDAO;
import com.greg.entities.Dir;
import com.greg.entities.State;
import com.greg.entities.User;

@ManagedBean
@ViewScoped
public class DirEditBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_START_ALIST = 
			"adminStart?faces-redirect=true";
	private static final String PAGE_START_ULIST = 
			"../user/userStart?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	@EJB
	DirDAO dirDAO;
	@EJB
	StateDAO stateDAO;
	@EJB
	UserDAO userDAO;

	// Dlatego stringi, ze w http wszystko leci w tekscie
	private String iddir;
	private String name;
	private String size;
	private String state;
	private String user;
	private String idstate;
	private String iduser;

	// @ManagedProperty(value="#{param.pageId}")	
	private String pageId;
	

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	// getters, setters (properties used at the JSF page)
	public String getIduser() {
		return iduser;
	}

	public void setIduser(String iduser) {
		this.iduser = iduser;
	}

	public String getIdstate() {
		return idstate;
	}

	public void setIdstate(String idstate) {
		this.idstate = idstate;
	}

	public String getIddir() {
		return iddir;
	}

	public void setIddir(String iddir) {
		this.iddir = iddir;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	// object to be used
	private Dir dir = null;
	private User userOb = null;
	private State stateOb = null;

	// ok
	@PostConstruct
	public void postConstruct() {
		// A. load dir if passed through session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		dir = (Dir) session.getAttribute("dir");
		
		if (dir != null) {
			session.removeAttribute("dir");
		}
		
		
		// .... add code for get method
		
		
		// if loaded record is to be edited then copy data to properties
		if (dir != null && Integer.valueOf(dir.getIddir()) != 0) {
			setName(dir.getName());
			setSize(String.valueOf(dir.getSize()));
			setIdstate(String.valueOf(dir.getState().getIdstate()));
			setIduser(String.valueOf(dir.getUser().getIduser()));
		}
		
	}

	// ok
	@SuppressWarnings("unused")
	private boolean validate() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		boolean result = false;

		if (name == null || name.trim().length() == 0) {
			ctx.addMessage(null, new FacesMessage("imie wymagane" ));
		}
		
		if (size == null || size.trim().length() == 0) {
			ctx.addMessage(null, new FacesMessage("rozmiar wymagany"));
		}
		
		// if no errors
		if (ctx.getMessageList().isEmpty()) {
			dir.setName(getName().trim());
			dir.setSize(Integer.valueOf(size.trim()));

			// obiekt State i User podczas edycji jest niezmieniany
			// jak starczy czasu to zmienic
			if (dir != null && Integer.valueOf(dir.getIddir()) == 0) {
				stateOb = stateDAO.find(Integer.valueOf(getState()));
				userOb = userDAO.find(Integer.valueOf(getUser().trim()));
			} else {
				stateOb = stateDAO.find(Integer.valueOf(getIdstate()));
				userOb = userDAO.find(Integer.valueOf(getIduser().trim()));
			}
			
			dir.setState(stateOb);
			dir.setUser(userOb);
			result = true;
		}

		return result;
	}
	
	// ok
	public String saveData() {

		// no dir object passed
		if (dir == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Bledne uzycie systemu!!!"));
			return PAGE_STAY_AT_THE_SAME;
		}

		if (!validate()) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if ((Integer) dir.getIddir() == null) {
				// new record
				dirDAO.create(dir);
			} else {
				// existing record
				dirDAO.merge(dir);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return PAGE_STAY_AT_THE_SAME;
		}

		// return PAGE_START_LIST;
        return PAGE_STAY_AT_THE_SAME;
	}

	// ok
	/*
	 * W metodzie wykorzystuje sie dwa stany
	 * reserve i free o wartosciach 0 i 1
	 */
	public String reserve(){
		try {
			// TODO: W metodzie powinna wystąpić zmiana dostępu do folderu
			if(dir.getState().getState().equals("reserv")){
				State s2 = stateDAO.find(2);
				dir.setState(s2);
			} else {
				State s1 = stateDAO.find(1);
				dir.setState(s1);
			}
			
			dirDAO.merge(dir);
			System.out.println(" ------------- " + pageId);
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("blad podczas zapisu"));
			return PAGE_STAY_AT_THE_SAME;
		}
		
		if(pageId == null ){
			return PAGE_STAY_AT_THE_SAME;
		} else if(pageId.equals("1")){
			return PAGE_START_ULIST;	
		}else{
			return PAGE_START_ALIST;
		}
	}

	// ok
	/*
	 * Metoda getFolder() dziala jako funkcja remove
	 */
	public String getFolder(){
		// delete rekord
		try{
		if(String.valueOf(dir.getIddir()) != null){
			dirDAO.remove(dir);
		}
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("blad podczas usuwania"));
			return PAGE_STAY_AT_THE_SAME;
		}
		
		if(pageId == null ){
			return PAGE_STAY_AT_THE_SAME;
		} else if(pageId.equals("1")){
			return PAGE_START_ULIST;	
		}else{
			return PAGE_START_ALIST;
		}
	}

}
