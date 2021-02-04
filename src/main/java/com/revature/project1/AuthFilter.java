package com.revature.project1;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;

import com.revature.project1.Sessions.SessionManager;

/**
 * Servlet Filter implementation class AuthFilter
 */
public class AuthFilter implements Filter {

    /**
     * Default constructor. 
     */
    public AuthFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse hresp=(HttpServletResponse)response;
		HttpServletRequest hreq=(HttpServletRequest)request;
		
		String originalURI = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
		String uri;
		if (originalURI != null) {
			uri=originalURI;
		} else {
			uri=hreq.getRequestURI();
		}
		
		HttpSession sess = hreq.getSession();
		SessionManager sessionManager=null;
		try {
			System.out.println("running");
			sessionManager = SessionManager.getSessionManager();
		} catch (JSONException e) {
			e.printStackTrace();
			System.out.println("Uhoh");
		}
		if (sessionManager.isLoggedIn(sess.getId())) {
			chain.doFilter(request, response);
		} else if (
				uri.matches("/project1/s/.*") ||
				uri.matches("/project1/s")) {
			hresp.sendRedirect("/project1/");
		} else {
			chain.doFilter(request, response);
		}
	}
		
		
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
