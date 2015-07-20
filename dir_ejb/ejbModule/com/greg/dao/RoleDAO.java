package com.greg.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.greg.entities.Role;
import com.greg.entities.User;

//DAO - Data Access Object for Role entity
//Designed to serve as an interface between higher layers of application and data.
//Implemented as stateless Enterprise Java bean - server side code that can be invoked even remotely.

@Stateless
public class RoleDAO {
	private final static String UNIT_NAME = "dirPU";

	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	public void create(Role role) {
		em.persist(role);
		// em.flush();
	}

	public Role merge(Role role) {
		return em.merge(role);
	}

	public void remove(Role role) {
		em.remove(em.merge(role));
	}

	public Role find(Object id) {
		return em.find(Role.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Role> getFullList() {
		List<Role> list = null;

		Query query = em.createQuery("select r from Role r");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Role> getList(Map<String, Object> searchParams) {
		List<Role> list = null;

		// 1. Build query string with parameters
		String select = "select distinct r ";
		String from = "from Role r left join fetch r.users ";
		String where = "";
		String orderby = "order by r.group asc";

		// search for surname
		String group = (String) searchParams.get("group");
		if (group != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "r.group like :group ";
		}
		
		
		// ... other parameters ... 

		// 2. Create query object
		Query query = em.createQuery(select + from + where + orderby);

		// 3. Set configured parameters
		if (group != null) {
			query.setParameter("group", group+"%");
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
	public void createRole(String group){
		
		/*
		 * 		Role role = new Role();
		 * 		role.setIdrole(0);
		 * 		role.setGroup("nowagrupa");
		 * 
		 * 		em.persist(role);
		 */
		Query query = em.createNativeQuery("insert into role values(null, '" + group + "')");
		query.executeUpdate();
	}
	
	// ok
	public void insertUser(Integer idrole, User u){
		Role r = (Role) em.find(Role.class, idrole);
		
        if(r != null && Integer.valueOf(u.getIduser()) != null){
        	r.getUsers().add(u);
	    }
	}
	
	// TODO: czy jest uzywana?
	public void deleteUser(Integer idrole, Integer index){
		Role r = (Role) em.find(Role.class, idrole);

        if(r != null && index != null){
        	r.getUsers().remove((int) index);
	    }
	}
	
}
