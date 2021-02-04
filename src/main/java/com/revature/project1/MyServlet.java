package com.revature.project1;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.math3.util.MultidimensionalCounter.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

import com.revature.project1.DB.types.BenefitsRequest;
import com.revature.project1.Sessions.SessionManager;
import com.revature.project1.Util.TRMSException;
import com.revature.project1.Util.TRMSWebSafeException;
import com.revature.project1.Util.User;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public MyServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served baz Hi at: ").append(request.getContextPath());
		HttpSession sess = request.getSession();
		System.out.println(sess.getId());
		System.out.println("get");
	}
	
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sess = request.getSession();
		SessionManager sessionManager = SessionManager.getSessionManager();
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		if (username == null || password == null) {
			response.sendRedirect("/project1/error-400.jsp");
		}
		if (sessionManager.isLoggedIn(sess.getId())) {
			response.sendRedirect("/project1/s/");
		} else {
			try {
				boolean loginSuccessful = sessionManager.login(sess.getId(), username, password);
				if (loginSuccessful) {
					response.sendRedirect("/project1/s/");
				} else {
					response.sendRedirect("/project1/index.jsp?loginFailed=true");
				}
			} catch (TRMSException e) {
				e.printStackTrace();
				response.sendRedirect("/project1/error-500.jsp");
			}
		}
	}
	
	private void handleBenefitsRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		BufferedReader br = request.getReader();
		String sess = request.getSession().getId();
		String body = br.lines().collect(Collectors.joining());
		SessionManager sm = SessionManager.getSessionManager();
		try {
			sm.createBenefitsRequest(sess,body);
		} catch (TRMSWebSafeException e) {
			response.setStatus(400);
			response.getWriter().append(e.getMessage());
			return;
		}
		
		response.setStatus(200);
		response.getWriter().append("Success");
	}
	
	void printAllRequests(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SessionManager sm = SessionManager.getSessionManager();
		try {
			List<BenefitsRequest> requests = sm.getAllBenefitRequests();
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			//Gson gson = new Gson();
			for (int i=0;i<requests.size()-1;++i) {
				//String json = gson.toJson(requests.get(i));
				//sb.append(json);
				sb.append(",");
			}
			//sb.append(gson.toJson(requests.get(requests.size()-1)));
			sb.append("]");
			response.getWriter().append(sb.toString());
		} catch (TRMSWebSafeException e) {
			response.setStatus(400);
			response.getWriter().append(e.getMessage());
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		switch (request.getRequestURI()) {
		case "/project1/login.do":
			login(request,response);
			break;
		case "/project1/request.do":
			handleBenefitsRequest(request,response);
			break;
		case "/project1/allRequests.do":
			printAllRequests(request,response);
			break;
		default:
			response.sendRedirect("/project1/error-404.jsp");
			break;
		}
		
	}

}
