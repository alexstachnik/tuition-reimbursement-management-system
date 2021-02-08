<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>TRMS - Approve Benefit Request</title>

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
        integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>
    <link rel="stylesheet" href="../static/css/common.css">
    <link rel="stylesheet" href="../static/css/approve.css">
	<link rel="icon" href="../static/imgs/favicon-16x16.png">
	<script src="js/approve.js"></script>
</head>
<body onload="main()">

<%@page import="java.util.List"%>
<%@page import="com.revature.project1.Sessions.SessionManager"%>
<%
	String closeDisabled="disabled";
	SessionManager sessionManager = SessionManager.getSessionManager();
	if (sessionManager.isBenco(session.getId())) {
		closeDisabled="";
	}
%>


<jsp:include page="navbar.jsp">
<jsp:param name="curpage" value="approval" />
</jsp:include>

<label>Reimbursement amount: </label><input id="reimbursementAmt" type="number"></input><br />
<div id="buttons">
	<button type="button" class="btn btn-primary" onclick="approve()">Approve</button>
	<button type="button" class="btn btn-danger" onclick="deny()">Deny</button>
	<button type="button" class="btn btn-secondary" <%= closeDisabled %> onclick="closeReq()">Close</button>
</div>
<div id="statusLabels">
</div>
<div id="content">
	<div id="otherData">
		<ul id="otherDataList">
		<%
		int requestID=Integer.parseInt(request.getParameter("requestID"));
		String grade = sessionManager.getGrade(requestID);
		if (grade != null) {
			out.println("<li>Grade: "+grade+"</li>");
		}
		
		List<String> links=sessionManager.getPresentations(requestID);
		for (int i=0;i<links.size();++i) {
			out.println("<li><a href=\""+links.get(i)+"\">Presentation File</a></li>");
		}
		
		if (sessionManager.employeeHasApproved(request.getSession().getId(), requestID)) {
			out.println("<li>You have approved this request</li>");
		}
		
		if (sessionManager.isBencoApproved(requestID)) {
			out.println("<li>The Benco has approved this");
		}
		
		if (sessionManager.isBossApproved(requestID)) {
			out.println("<li>Either the employees direct supervisor or dept. head has approved this</li>");
		}
		%>
		</ul>
	</div>
	<div id="requestInfoDiv">
		<table class="table" id="requestInfoTable">
		</table>
	</div>

</div>
 


</body>
</html>