package com.greg.dir;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.servlet.http.HttpSession;

import com.greg.dao.DirDAO;
import com.greg.entities.Dir;
import com.greg.util.PaginationHelper;



@ManagedBean
public class DirListBB {
	private static final String PAGE_DIR_EDIT = 
			"adminEdit?faces-redirect=true";
	private static final String PAGE_DIR_ADD = 
			"adminAdd?faces-redirect=true";
	private static final String PAGE_USERDIR_EDIT = 
			"userEdit.xhtml?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private String iddir;
	private String name;
	private String size;
	private String state;
	private String user;
	private String iduser;
	private String role;

	 private PaginationHelper pagination;
	 private int selectedItemIndex;
	 private DataModel dtmdl = null;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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
	
	public String getIduser() {
		return iduser;
	}

	public void setIduser(String iduser) {
		this.iduser = iduser;
	}

	//Dependency injection
	// - no setter method needed in this case
	@EJB
	DirDAO dirDAO;

	public List<Dir> getFullList(){
		return dirDAO.getFullList();
	}

	public List<Dir> getList(){
		List<Dir> list = null;
		
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		if (name != null && name.length() > 0){
			searchParams.put("name", name);
		}

		
		//2. Get list
		list = dirDAO.getList(searchParams);
		
		return list;
	}
	
	// ok
	public String newDir(){
		HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSession(true);
		Dir dir = new Dir();
		session.setAttribute("dir", dir);
		
		return PAGE_DIR_ADD;
	}
	
	public String editDir(Dir dir){
		HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSession(true);
		session.setAttribute("dir", dir);
		return PAGE_DIR_EDIT;
	}

	public String deleteDir(Dir dir){
		dirDAO.remove(dir);
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String editUserDir(Dir dir){
		HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSession(true);
		session.setAttribute("dir", dir);
		return PAGE_USERDIR_EDIT;
	}

	public List<File> dir(){
	 	List<File> list = null;
	 	File dir = new File("/home/greg/Pulpit/test/");
			File[] files = dir.listFiles();
			list = Arrays.asList(files);
        return list;
	}

	
	// ---------------------
	
	
}
