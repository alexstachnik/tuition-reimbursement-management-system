<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>TRMS - Requests</title>

<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
	integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
	crossorigin="anonymous"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
	integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
	crossorigin="anonymous"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
	integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
	crossorigin="anonymous"></script>
	
	<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js"></script>
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.min.css">
<link rel="stylesheet" href="../static/css/common.css">
<link rel="stylesheet" href="../static/css/requests.css">
<link rel="icon" href="../static/imgs/favicon-16x16.png">

</head>
<body onload="main()">


	<jsp:include page="navbar.jsp">
		<jsp:param name="curpage" value="requests" />
	</jsp:include>

<div class="modal fade" id="submitSuccessModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="submitSuccessTitle">Blank</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body" id="submitSuccessBody">
        ...
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>

	<div id="content">

		<h3>To create a new request fill out the fields below</h3>

		<div class="formbody">

			<div class="field"><div>
				<div class="label" id="location-label">
					<label>Location</label>
				</div>
				<div class="input" id="location-input">
					<input inputmode="text"></input>
				</div>
			</div></div>

			<div class="field">
			<div>
			<div class="label" id="date-label">
				<label>Date of Event</label>
			</div>
			<div class="date input-group" id="datepickerdiv">
			</div>
			</div>
			</div>



			<div class="field"><div>
				<div class="label" id="description-label">
					<label>Description</label>
				</div>
				<div class="input" id="description-input">
					<textarea rows="3"></textarea>
				</div>
			</div></div>

			<div class="field"><div>
				<div class="label" id="amount-label">
					<label>Amount (dollars)</label>
				</div>
				<div class="input" id="amount-input">
					<input type="number"></input>
				</div>
			</div></div>

			<div class="field"><div>
				<div class="label" id="grading-format-label">
					<label>Grading Format</label>
				</div>
					<div class="radio-inputs" id="grading-format-input">
						<div class="single-radio-input">
							<label for="grading-format-grade">Grade</label> 
							<input type="radio" id="grading-format-grade" name="grading-format"></input>
						</div>
						<div class="single-radio-input">
							<label for="grading-format-presentation">Presentation</label> 
							<input type="radio" id="grading-format-presentation" name="grading-format"></input>
						</div>
					</div>
				</div></div>

			<div class="field"><div>
				<div class="label" id="min-grade-label">
					<label>Minimum Grade</label>
				</div>
				<div class="input" id="min-grade-input">
					<input type="text"></input>
				</div>
			</div></div>

			<div class="field"><div>
				<div class="label" id="grading-format-label">
					<label>Event Type</label>
				</div>
				<div class="radio-inputs" id="event-type-inputs">
					<div class="single-radio-input">
						<label for="event-type-seminar">Seminar</label> 
						<input type="radio" id="event-type-seminar" name="event-type"></input>
					</div>
					<div class="single-radio-input">
						<label for="event-type-course">University Course</label> 
						<input type="radio" id="event-type-course" name="event-type"></input>
					</div>
					<div class="single-radio-input">
						<label for="event-type-cert">Certification</label> 
						<input type="radio" id="event-type-cert" name="event-type"></input>
					</div>
					<div class="single-radio-input">
						<label for="event-type-cert-prep">Certification Prep Class</label> 
						<input type="radio" id="event-type-cert-prep" name="event-type"></input>
					</div>
					<div class="single-radio-input">
						<label for="event-type-tech">Technical Training</label> 
						<input type="radio" id="event-type-tech" name="event-type"></input>
					</div>
					<div class="single-radio-input">
						<label for="event-type-other">Other</label> 
						<input type="radio" id="event-type-other" name="event-type"></input>
					</div>
				</div>
			</div></div>
			
			<div class="field"><div>
				<div class="label" id="justification-label">
					<label>Justification</label>
				</div>
				<div class="input" id="justification-input">
					<input inputmode="text"></input>
				</div>
			</div></div>
			
			<div class="field"><div>
				<div class="label" id="work-time-missed-label">
					<label>Work time missed</label>
				</div>
				<div class="input" id="work-time-missed-input">
					<input inputmode="text"></input>
				</div>
			</div></div>

			<div class="field"><div>
			<div class="label"><label>Estimated Reimbursement</label></div>
			<div class="output">
				$<input type="number" id="reimbursementOutput" disabled="disabled" value="0.00"></input>
			</div>
			</div></div>

		<button id="submit-button" type="button" class="btn btn-primary" onclick="submit()">Submit</button>
			
		</div>
		


	</div>

</body>

<script src="js/benefits.js">
</script>
</html>