package com.revature.project1.Web;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.IllegalFormatException;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.revature.project1.DB.types.BenefitsRequest;
import com.revature.project1.DB.types.Employee;
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
		switch (request.getRequestURI()) {
		case "/project1/s/getAllRequests.do":
			getAllRequests(request,response);
			break;
		case "/project1/s/employeeMap.do":
			getEmployeeMap(request,response);
			break;
		case "/project1/s/getRequest.do":
			getBenefitsRequest(request, response);
			break;
		default:
			response.sendError(404);
			break;
		}
	}
	
	private void printError(HttpServletRequest request, HttpServletResponse response, TRMSWebSafeException e) throws IOException, ServletException {
		response.setStatus(e.getErrorCode());
		response.getWriter().append(e.getMessage());
	}
	
	private void getEmployeeMap(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		SessionManager sessionManager = SessionManager.getSessionManager();
		String employeeIDstr = request.getParameter("employeeID");
		int employeeID=0;
		try {
			employeeID=Integer.parseInt(employeeIDstr);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			response.sendError(500);
		}
		try {
			Employee employee = sessionManager.lookupEmployee(employeeID);
			if (employee == null) {
				throw new TRMSWebSafeException("No such employee");
			}
			JSONObject json = new JSONObject();
			json.append("name", employee.getName());
			json.append("role", employee.getRole());
			response.getWriter().append(json.toString(1));
		} catch (TRMSWebSafeException e) {
			printError(request,response,e);
			return;
		}
	}
	
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sess = request.getSession();
		SessionManager sessionManager = SessionManager.getSessionManager();
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		if (username == null || password == null) {
			response.sendError(400);
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
			} catch (TRMSWebSafeException e) {
				printError(request, response, e);
				return;
			}
		}
	}
	
	private void handleBenefitsRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		BufferedReader br = request.getReader();
		String sess = request.getSession().getId();
		String body = br.lines().collect(Collectors.joining());
		SessionManager sm = SessionManager.getSessionManager();
		try {
			sm.createBenefitsRequest(sess,body);
		} catch (TRMSWebSafeException e) {
			printError(request, response, e);
			return;
		}
		
		response.setStatus(200);
		response.getWriter().append("Success");
	}
	
	void getAllRequests(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionManager sm = SessionManager.getSessionManager();
		try {
			List<BenefitsRequest> requests = sm.getAllBenefitRequests();
			JSONArray requestArray= new JSONArray();
			for (int i=0;i<requests.size();++i) {
				requestArray.put(requests.get(i).toJson());
			}
			response.getWriter().append(requestArray.toString(1));
		} catch (TRMSWebSafeException e) {
			printError(request, response, e);
			return;
		}
	}
	
	private void getBenefitsRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
		SessionManager sm = SessionManager.getSessionManager();
		int requestID=0;
		try {
			String requestIDStr = request.getParameter("requestID");
			requestID=Integer.parseInt(requestIDStr);
		} catch (NumberFormatException e) {
			response.sendError(400);
			return;
		}
		try {
			BenefitsRequest br = sm.lookupBenefitsRequest(requestID);
			if (br == null) {
				response.sendError(400);
				return;
			} else {
				response.getWriter().append(br.toJson().toString(1));
			}
		} catch (TRMSWebSafeException e) {
			printError(request, response, e);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		switch (request.getRequestURI()) {
		case "/project1/login.do":
			login(request,response);
			break;
		case "/project1/s/request.do":
			handleBenefitsRequest(request,response);
			break;
		default:
			response.sendError(404);
			break;
		}
		
	}

}
