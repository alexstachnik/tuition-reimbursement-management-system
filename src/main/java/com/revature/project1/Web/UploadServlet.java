package com.revature.project1.Web;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.project1.DB.types.Approval;
import com.revature.project1.DB.types.ApprovalType;
import com.revature.project1.DB.types.Attachment;
import com.revature.project1.Sessions.SessionManager;
import com.revature.project1.Util.TRMSWebSafeException;

/**
 * Servlet implementation class UploadServlet
 */
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Got unexpected get request");
	}
	
	private void printError(HttpServletRequest request, HttpServletResponse response, TRMSWebSafeException e) throws IOException, ServletException {
		response.setStatus(e.getErrorCode());
		response.getWriter().append(e.getMessage());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean isPresentation=false;
		boolean isMail=false;
		int requestID=0;
		String fileType=null;
		try {
			byte[] requestIDBytes=request.getPart("requestID").getInputStream().readAllBytes();
			requestID=Integer.parseInt(new String(requestIDBytes));
			byte[] uploadTypeBytes=request.getPart("uploadType").getInputStream().readAllBytes();
			String uploadTypeStr = new String(uploadTypeBytes);
			if (uploadTypeStr.equals("presentation")) {
				isPresentation=true;
			} else if (uploadTypeStr.equals("email")) {
				isMail=true;
			} else {
				response.sendError(400);
			}
			byte[] fileTypeBytes = request.getPart("fileType").getInputStream().readAllBytes();
			fileType=new String(fileTypeBytes);
		} catch (NumberFormatException e) {
			response.sendError(400);
		}
		
		Attachment attachment = new Attachment();
		InputStream istream=request.getPart("file").getInputStream();
		attachment.setFiletype(fileType);
		attachment.setRequestID(requestID);
		SessionManager sessionManager = SessionManager.getSessionManager();
		try {
			int fileID=sessionManager.uploadAttachment(attachment, istream);
			if (isPresentation) {
				sessionManager.addPresentation(requestID, fileID);
			} else if (isMail) {
				Approval approval = new Approval();
				approval.setPreApproval(true);
				approval.setRequestID(requestID);
				approval.setApprovalDate(Timestamp.valueOf(LocalDateTime.now()));
				approval.setApprovalType(ApprovalType.APPROVE);
				approval.setFileReference(fileID);
				sessionManager.addPreapproval(approval);
			}
		} catch (TRMSWebSafeException e) {
			printError(request,response,e);
		}
		
	}

}
