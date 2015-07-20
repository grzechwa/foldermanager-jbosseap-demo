package com.greg.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.greg.entities.State;

//DAO - Data Access Object for State entity
//Designed to serve as an interface between higher layers of application and data.
//Implemented as stateless Enterprise Java bean - server side code that can be invoked even remotely.

@Stateless
public class StateDAO {
	private final static String UNIT_NAME = "dirPU";

	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(State state) {
		em.persist(state);
		// em.flush();
	}

	public State merge(State state) {
		return em.merge(state);
	}

	public void remove(State state) {
		em.remove(em.merge(state));
	}

	public State find(Object id) {
		return em.find(State.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<State> getFullList() {
		List<State> list = null;

		Query query = em.createQuery("select s from State s");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<State> getList(Map<String, Object> searchParams) {
		List<State> list = null;

		// 1. Build query string with parameters
		String select = "select s ";
		String from = "from State s ";
		String where = "";
		String orderby = "order by s.state asc";

		// search for surname
		String state = (String) searchParams.get("state");
		if (state != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "d.state like :state ";
		}
		
		
		// ... other parameters ... 

		// 2. Create query object
		Query query = em.createQuery(select + from + where + orderby);

		// 3. Set configured parameters
		if (state != null) {
			query.setParameter("state", state+"%");
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

}
