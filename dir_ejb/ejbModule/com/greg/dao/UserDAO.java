package com.greg.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.greg.entities.Dir;
import com.greg.entities.Role;
import com.greg.entities.User;

//DAO - Data Access Object for Dir entity
//Designed to serve as an interface between higher layers of application and data.
//Implemented as stateless Enterprise Java bean - server side code that can be invoked even remotely.

@Stateless
public class UserDAO {
	private final static String UNIT_NAME = "dirPU";
	
	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	public void create(User user) {
		em.persist(user);
		// em.flush();
	}

	public User merge(User user) {
		return em.merge(user);
	}

	public void remove(User user) {
		em.remove(em.merge(user));
	}

	public void removeUser(User user){
		
		// user.getDirs().;
		// user.removeDir(user.g)
		/*
for(int i = 0; i < user.getDirs().size(); i++){
			Dir dir = user.getDirs().get(i);
			System.out.println(" ----------------- for dir: " + dir.getName());
			dir.getUser().getDirs().clear();
			user.removeDir(dir);
			em.merge(user);
		}
		em.merge(user);
		
		for(int i = 0; i < user.getRoles().size(); i++){
			Role r = user.getRoles().get(i);
			user.getRoles().remove(i);
			System.out.println(" ----------------- for role: " + r.getGroup());
			em.merge(user);
		}
		*/
		// user.getRoles().clear();
		// em.merge(user);
		// user.removeDir(u)
		
        // System.out.println("Za petla User.getDirs ------ : " +  "size" + user.getDirs().size() + " -- " + user.getDirs());
		
		// em.remove(em.merge(user));
		try{
						
			
			
			Query query = em.createQuery("delete Dir where iduser=:id");
		query.setParameter("id", user.getIduser());

		for(int i = 0; i < user.getDirs().size(); i++){
			query.executeUpdate();
		}
		
		
		

 query = em.createQuery("delete User where iduser=:id");
		query.setParameter("id", user.getIduser());

		query.executeUpdate();

		em.remove(user);

		}
		catch(Exception ex){
			ex.printStackTrace(); 
			/*
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Prosze ususnac katalogi najpierw" ));
					*/
			
		}
		
		
	}

	public User find(Object id) {
		return em.find(User.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<User> getFullList() {
		List<User> list = null;

		Query query = em.createQuery("select u from User u");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	// TODO: czy jest uzywane?
	@SuppressWarnings("unchecked")
	public List<User> getListEmptyDir() {
		List<User> list = null;

		Query query = em.createQuery("select d from Dir d where d.iddir IS NOT EMPTY");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	// TODO: czy jest uzywane?
	@SuppressWarnings("unchecked")
	public List<Role> getRoleList(){
		List<Role> list = null;

		Query query = em.createQuery("select r from Role r ");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<User> getList(Map<String, Object> searchParams) {
		List<User> list = null;
		
		// 1. Build query string with parameters
		 String select = "select distinct u ";
		 String from = "from User u left join fetch u.roles ";
		 String where = "";
		 String orderby = "order by u.name asc";

		// search for surname
		String name = (String) searchParams.get("name");
		if (name != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "u.name like :name ";
		}
		
		
		// ... other parameters ... 

		// 2. Create query object
		Query query = em.createQuery(select + from + where + orderby);

		// 3. Set configured parameters
		if (name != null) {
			query.setParameter("name", name+"%");
		}

		// ... other parameters ... 

		// 4. Execute query and retrieve list of Dir objects
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// TODO: kod nie dziala, znalezc przyczyne
	/*
	 * wprowadza uzytkownia w admin->role
	 */
	public void insertRole(Integer iduser, Role r){
		User u = (User) em.find(User.class, iduser);
		
		/*
         * info("BEFORE", u, r);
         * if(u != null && Integer.valueOf(r.getIdrole()) != null){
         * u.getRoles().add(r);
         * info("AFTER", u, r);
         *  }
        */
        
		Query query = em.createNativeQuery("insert into user_has_role values(" + 
        		iduser + ", " + 
        		r.getIdrole() + ")");
        query.executeUpdate();
       
	}
	
	/*
	 * usuwa role w admin->users
	 */
	public void deleteRole(Integer iduser, Role r){
		User u = (User) em.find(User.class, iduser);
		if(u != null && Integer.valueOf(r.getIdrole()) != null){
        	r = (Role) em.merge(r);
        	r.getUsers().remove(u);
			
        	u = (User) em.merge(u);
			u.getRoles().remove(r);
	    }
	}
	
	// TODO: implementacja metody usuwajacej katalogi od uzytkownika
	public void deleteDir(Integer iduser, Dir d){
		User u = (User) em.find(User.class, iduser);
		
		
		if(u != null && Integer.valueOf(d.getIddir()) != null){
			// Query query = em.createNamedQuery("delete from dir where iddir=" + d.getIddir());
			// query.executeUpdate();
			
        	// d = (Dir) em.merge(d);
        	u = (User) em.merge(u);
	    }
		
		
	}
	
	
	/*
	 * Metody pomocnicze do wyswietlania w konsoli jboss
	 */
	public void info(String s, User u, Dir d){
		System.out.println(" ------- " + s + " ------- ");
		System.out.println("User:       " + u +	" Iduser: " + u.getIduser());
		System.out.println("Role:       " +	d + " Iddir: " + d.getIddir());
		System.out.println("u.getRoles size: " + u.getDirs().size());
	}
	public void info(String s, User u, Role r){

		System.out.println(" ------- " + s + " ------- ");
		System.out.println("User:       " + u +	" Iduser: " + u.getIduser());
		System.out.println("Role:       " +	r + " Idrole: " + r.getIdrole());
		System.out.println("u.getRoles size: " + u.getRoles().size());
		// System.out.println("r.getUsers size: " + r.getUsers().size());
	}
	
	
	/*
	 * Do logowania przz imie
	 */
	public User findByName(String name){
		// return em.find(User.class, name);
		// Query query =  em.createNativeQuery("Select name from user where name='" + name + "'");
		Query query = em.createQuery("Select u from User u where u.name = :name");
		query.setParameter("name", name);
		// System.out.println(" Query " + "Select name from user where name='" + name + "'");
		// query.executeUpdate()i;
		User u = null;
		try{
		u = (User) query.getSingleResult();
		}catch(Exception ex){
			
		}
		return u;
		
	}
}
