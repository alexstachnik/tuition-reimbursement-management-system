<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>TRMS - Uploads</title>

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
    <link rel="stylesheet" href="../static/css/uploads.css">
	<link rel="icon" href="../static/imgs/favicon-16x16.png">
	<script src="js/uploads.js"></script>
</head>
<body onload="main()">


<jsp:include page="navbar.jsp">
<jsp:param name="curpage" value="uploads" />
</jsp:include>

<div id="content">

<div id="grades" class="pagebox">
	<h2>Upload Grade</h2>
	<div id="gradeSelection">
		<button class="btn btn-primary" id="submitGradeButton" onclick="submitGrade()">Submit</button>
		<input type="text" class="form-control" id="grade-input" placeholder="Grade">
	</div>
</div>

<div id="files" class="pagebox">
<h2>Upload File</h2>
<div id="fileSelection">
	<button class="btn btn-primary" id="submitFileButton" onclick="submitFile()">Submit</button>
	<input type="file" id="fileUpload">

		<div class="radio-inputs">
			<div class="single-radio-input">
				<label for="file-type-presentation">Presentation</label> 
				<input type="radio" id="file-type-presentation" name="file-type"></input>
			</div>
			<div class="single-radio-input">
				<label for="file-type-preapproval">Approval Email</label> 
				<input type="radio" id="file-type-email" name="file-type"></input>
			</div>
		</div>
</div>


<div class="modal fade" id="mainModal" tabindex="-1" role="dialog" aria-labelledby="mainModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="modalTitle">Select a Request</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body" id="modalBody">
        ...
      </div>
    </div>
  </div>
</div>


</body>
</html>