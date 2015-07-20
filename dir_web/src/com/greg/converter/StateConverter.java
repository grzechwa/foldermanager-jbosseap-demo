package com.greg.converter;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.greg.entities.State;
import com.greg.dao.StateDAO;
import com.greg.entities.Dir;
import com.greg.dao.DirDAO;

@ManagedBean
@FacesConverter(value="stateConverter")
public class StateConverter implements Converter {
	
	@EJB
	private StateDAO stateDAO = new StateDAO();
	@EJB
	private DirDAO dirDAO;
/*
	public StateConverter() {  
        super();  
        try {  
            InitialContext ic = new InitialContext();  
            stateDAO = (StateDAO) ic.lookup("java:ejbModule/StateDAO");  
           } catch (NamingException e) {  
            e.printStackTrace();  
       }  
    } 
*/

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent c, String s) throws ConverterException {
		/*
		Integer i = null;
		State ns = null;
		
		i = Integer.valueOf(s);
		
		
		ns = stateDAO.find(i);
		// ns.setIdstate(1);
		return ns;
		*/
		  return null;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent c, Object o) {
		
		if( o instanceof String){
			return null;
		}
        if(!(o instanceof State)){
        	throw new ConverterException(new FacesMessage(
					"Nie udalo sie dokonac konwersji!" 
			+ " --  " + o.getClass()));
		}
        
        State s = (State) o;
        return String.valueOf(s.getIdstate());
        
		// return null;
	}
	




}