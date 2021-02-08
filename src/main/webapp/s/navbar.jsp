
<%@page import="com.revature.project1.Sessions.SessionManager"%>

<%
	String curpage = (String) request.getParameter("curpage");
	String requestsActive="";
	String messagesActive="";
	String approvalsActive="";
	String adminActive="";
	String uploadsActive="";
	switch (curpage) {
	case "requests":
		requestsActive="active";
		break;
	case "messages":
		messagesActive="active";
		break;
	case "approvals":
		approvalsActive="active";
		break;
	case "admin":
		adminActive="active";
		break;
	case "uploads":
		uploadsActive="active";
		break;
	default:
		break;
	}
	
	String adminDisabled="disabled";
	SessionManager sessionManager = SessionManager.getSessionManager();
	if (sessionManager.isSuperuser(session.getId())) {
		adminDisabled="";
	}
%>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
	<a class="navbar-brand" href="#">TRMS</a>
	<button class="navbar-toggler" type="button" data-toggle="collapse"
		data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup"
		aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="navbarNavAltMarkup">
		<div class="navbar-nav">
			<a class="nav-item nav-link <%=requestsActive%>" href="/project1/s/"> Requests</a>
			<a class="nav-item nav-link <%=messagesActive%>" href="/project1/s/messages.jsp"> Messages </a>
			<a class="nav-item nav-link <%=approvalsActive%>" href="/project1/s/approvals.jsp">Approvals</a>
			<a class="nav-item nav-link <%=adminActive%> <%=adminDisabled%>" href="/project1/s/useradmin.jsp">User Admin</a>
			<a class="nav-item nav-link <%=uploadsActive%>" href="/project1/s/uploads.jsp">Uploads</a>
			<a class="nav-item nav-link" href="/project1/logout.do">Log out</a>
		</div>
	</div>
</nav>