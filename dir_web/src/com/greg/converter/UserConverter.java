package com.greg.converter;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

import com.greg.entities.State;
import com.greg.dao.StateDAO;
import com.greg.entities.User;
import com.greg.dao.UserDAO;

@ManagedBean
@ViewScoped
@FacesConverter(value="UserConverter")
public class UserConverter implements Converter {
	
	@EJB
	StateDAO stateDAO;
	@EJB
	UserDAO userDAO;
	@Override
	public Object getAsObject(FacesContext ctx, UIComponent c, String s) {
		Integer i = null;
		User us = null;
		try{
                i = Integer.valueOf(s);
		} catch (NumberFormatException e){
                us = userDAO.find(i);
		}
		
		return us;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent c, Object o) {
		
		 if(!(o instanceof User)){
				throw new ConverterException(new FacesMessage(
						"Nie udalo sie dokonac konwersji!" 
				+ " --  " + o.getClass()));
			}
        
        User u = (User) o;
        return String.valueOf(u.getIduser());
	}
	




}