package com.revature.project1.Web;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import com.revature.project1.DB.types.ApprovalType;
import com.revature.project1.DB.types.Attachment;
import com.revature.project1.DB.types.BenefitsRequest;
import com.revature.project1.DB.types.Employee;
import com.revature.project1.Sessions.SessionManager;
import com.revature.project1.Util.TRMSWebSafeException;

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
		case "/project1/s/getAllMyRequests.do":
			getAllMyRequests(request, response);
			break;
		case "/project1/s/employeeMap.do":
			getEmployeeMap(request,response);
			break;
		case "/project1/s/getRequest.do":
			getBenefitsRequest(request, response);
			break;
		case "/project1/s/presentation.do":
			getPresentation(request,response);
			break;
		case "/project1/logout.do":
			logout(request,response);
			break;
		default:
			response.sendError(404);
			break;
		}
	}
	
	private void getPresentation(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		SessionManager sessionManager = SessionManager.getSessionManager();
		String fileIDstr = request.getParameter("fileID");
		int fileID=0;
		try {
			fileID=Integer.parseInt(fileIDstr);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			response.sendError(500);
		}
		
		
		try {
			Attachment attachment = sessionManager.fetchAttachment(fileID);
			response.setContentType("application/octet-stream");
			//response.setHeader("content-disposition","attachment,filename="+attachment.getFileID()+"."+attachment.getFiletype());
			response.getOutputStream().write(attachment.getFiledata());
		} catch (TRMSWebSafeException e) {
			printError(request, response, e);
			return;
		}
	}
	
	private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().invalidate();
		response.sendRedirect("/project1/");
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
	
	void getAllMyRequests(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionManager sm = SessionManager.getSessionManager();
		String sess = request.getSession().getId();
		try {
			List<BenefitsRequest> requests = sm.getAllMyBenefitsRequests(sess);
			JSONArray requestArray= new JSONArray();
			for (int i=0;i<requests.size();++i) {
				JSONObject requestObj = requests.get(i).toJson();
				int requestID = requests.get(i).getRequestID();
				requestObj.put("employeeHasApproved", sm.employeeHasApproved(sess, requestID));
				requestObj.put("bencoHasApproved", sm.isBencoApproved(requestID));
				requestObj.put("bossHasApproved", sm.isBossApproved(requestID));
				requestArray.put(requestObj);
			}
			response.getWriter().append(requestArray.toString(1));
		} catch (TRMSWebSafeException e) {
			printError(request, response, e);
			return;
		}
	}
	
	void getAllRequests(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionManager sm = SessionManager.getSessionManager();
		String sess = request.getSession().getId();
		try {
			List<BenefitsRequest> requests = sm.getAllBenefitRequests();
			JSONArray requestArray= new JSONArray();
			for (int i=0;i<requests.size();++i) {
				JSONObject requestObj = requests.get(i).toJson();
				int requestID = requests.get(i).getRequestID();
				requestObj.put("employeeHasApproved", sm.employeeHasApproved(sess, requestID));
				requestObj.put("bencoHasApproved", sm.isBencoApproved(requestID));
				requestObj.put("bossHasApproved", sm.isBossApproved(requestID));
				requestArray.put(requestObj);
			}
			response.getWriter().append(requestArray.toString(1));
		} catch (TRMSWebSafeException e) {
			printError(request, response, e);
			return;
		}
	}
	
	private void getBenefitsRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
		SessionManager sm = SessionManager.getSessionManager();
		String sess = request.getSession().getId();
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
				JSONObject jsonObj = br.toJson();
				jsonObj.put("employeeHasApproved", sm.employeeHasApproved(sess, requestID));
				jsonObj.put("bencoHasApproved", sm.isBencoApproved(requestID));
				jsonObj.put("bossHasApproved", sm.isBossApproved(requestID));
				jsonObj.put("totalReimbursement", sm.totalReimbursement(br.getEmployeeID()));
				response.getWriter().append(jsonObj.toString(1));
			}
		} catch (TRMSWebSafeException e) {
			printError(request, response, e);
			return;
		}
	}
	
	private void approve(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionManager sm = SessionManager.getSessionManager();
		String sessionId = request.getSession().getId();
		
		int requestID=0;
		ApprovalType approvalType=null;
		double requestAmt=0;
		try {
			String requestIDStr = request.getParameter("requestID");
			String approvalTypeStr = request.getParameter("approvalType");
			String requestAmtStr = request.getParameter("requestAmt");

			requestID=Integer.parseInt(requestIDStr);
			approvalType=ApprovalType.valueOf(approvalTypeStr);
			if (approvalType == null) {
				response.sendError(400);
			}
			requestAmt=Double.parseDouble(requestAmtStr);
		} catch (NumberFormatException e) {
			response.sendError(400);
		} catch (IllegalArgumentException e) {
			response.sendError(400);
		}
		
		try {
			sm.addApproval(sessionId, requestID, approvalType, requestAmt);
		} catch (TRMSWebSafeException e) {
			printError(request, response, e);
		}
	}
	
	private void submitGrade(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionManager sm = SessionManager.getSessionManager();
		try {
			String requestIDStr = request.getParameter("requestID");
			String grade = request.getParameter("grade");
			
			int requestID=Integer.parseInt(requestIDStr);
			sm.setGrade(requestID, grade);
		} catch (NumberFormatException e) {
			response.sendError(400);
		} catch (TRMSWebSafeException e) {
			printError(request, response, e);
		}
	}
	
	private void closeRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionManager sm = SessionManager.getSessionManager();
		try {
			String requestIDStr = request.getParameter("requestID");
			
			int requestID=Integer.parseInt(requestIDStr);
			sm.closeRequest(requestID);
		} catch (NumberFormatException e) {
			response.sendError(400);
		} catch (TRMSWebSafeException e) {
			printError(request, response, e);
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
		case "/project1/s/approve.do":
			approve(request,response);
			break;
		case "/project1/s/grade.do":
			submitGrade(request,response);
			break;
		case "/project1/s/close.do":
			closeRequest(request,response);
			break;
		default:
			response.sendError(404);
			break;
		}
		
	}

}
