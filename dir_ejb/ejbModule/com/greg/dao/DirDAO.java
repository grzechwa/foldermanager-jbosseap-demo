package com.greg.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.greg.entities.Dir;
import com.greg.entities.User;

//DAO - Data Access Object for Dir entity
//Designed to serve as an interface between higher layers of application and data.
//Implemented as stateless Enterprise Java bean - server side code that can be invoked even remotely.

@Stateless
public class DirDAO {
	private final static String UNIT_NAME = "dirPU";

	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	
	UserDAO userDAO = null;
	public void create(Dir dir) {
		em.persist(dir);
		// em.flush();
	}

	public Dir merge(Dir dir) {
		return em.merge(dir);
	}

	public void remove(Dir dir) {
		em.remove(em.merge(dir));
	}

	public Dir find(Object id) {
		return em.find(Dir.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Dir> getFullList() {
		List<Dir> list = null;

		Query query = em.createQuery("select d from Dir d");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Dir> getList(Map<String, Object> searchParams) {
		List<Dir> list = null;

		// 1. Build query string with parameters
		String select = "select d ";
		String from = "from Dir d ";
		String where = "";
		String orderby = "order by d.name asc, d.size";

		// search for surname
		String name = (String) searchParams.get("name");
		if (name != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "d.name like :name ";
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

	/*
	 * Pomaga usunac uzytkownika w opcji admin->uzytkownicy
	 * usuwa pojedyncza zawartosc listy User.getDirs();
	 */
	public void deleteDir(Integer iduser, Integer index){
		User u = (User) em.find(User.class, iduser);

        if(u != null && index != null){
        	// TODO: kod nie dziala, znalezc przyczyne
        	/* 
        	 * Dir dir = em.find(Dir.class, index);
        	 * System.out.println(" --------------- Dir: " + dir + " " + dir.getIddir() );
        	 * u.getDirs().remove(dir);
        	 * 
        	 */
        	Query query = em.createNativeQuery("delete from dir where iddir=" + index);
        	query.executeUpdate();
	    }
	}
}
