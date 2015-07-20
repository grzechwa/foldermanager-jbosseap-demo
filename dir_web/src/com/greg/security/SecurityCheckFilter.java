package com.greg.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.greg.entities.User;

/**
 * Filter launched for every request. Used for security check.
 * 
 * @author Przemysław Kudłacik
 */
public class SecurityCheckFilter implements Filter {
	private ServletContext servletContext;
	String publicRes;
	String loginPage;
	String adminPage;
	String userPage;

	private static final String FACES_REDIRECT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<partial-response><redirect url=\"%s\"></redirect></partial-response>";

	/**
	 * Initialization of the filter object (overrides the parent method). The
	 * method loads parameters specifying paths of public resources and login
	 * page. Parameters should be set for the filter in web.xml as
	 * "publicResource" and "loginPage". If the parameters are not set "/public"
	 * and "/ligin.jsf" are assumed as the relevant values.
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		servletContext = config.getServletContext();
		// get defined public resources, if not - assume "/public" subfolder
		// (default)
		publicRes = config.getInitParameter("publicResource");
		if (publicRes == null) {
			publicRes = "/public";
		}
		loginPage = config.getInitParameter("loginPage");
		if (loginPage == null) {
			loginPage = "/login.jsf";
		}
	}

	/**
	 * The filtering method (overrides the parent method). The method retrieves
	 * a User object from session. If the conditions of valid connection are
	 * fulfilled then the request is passed further. In other case the user is
	 * forwarded to a login page. The approach works also for JSF AJAX requests.
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		// load user object from session
		HttpSession session = request.getSession();
		User u = (User) session.getAttribute("user");

		boolean pass = false;

		if (u == null) { // no data - check if call for public resources or
							// login page
			String path = request.getServletPath();
			if (path.startsWith(publicRes) || path.startsWith(loginPage)) {
				pass = true;
			}
		} else {
			pass = true;
		}

		// IMPORTANT: other checking based on remote host or port could be
		// performed above for higher level of security

		// if the request is not valid (client is not logged in)
		if (!pass) {
			// if AJAX request then redirect to application root
			if ("partial/ajax".equals(request.getHeader("Faces-Request"))) {
				res.setContentType("text/xml");
				res.setCharacterEncoding("UTF-8");
				res.getWriter().printf(FACES_REDIRECT_XML,
						request.getContextPath() + "/");
			} else {
				// if other (regular) request then forward to the defined login
				// page
				servletContext.getRequestDispatcher(loginPage).forward(request,
						response);
			}

		} else { // if request is valid (client is logged in) then
			chain.doFilter(request, response);
		}

	}

	@Override
	public void destroy() {
	}
}
