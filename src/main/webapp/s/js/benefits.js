/**
 * 
 */

	$('#datepickerdiv').datepicker({});
	
	function getFormData() {
		let gradingElts = document.getElementsByName('grading-format');
		let grading_format="";
		let gradeFormatNames={	"grading-format-grade": "GRADED",
								"grading-format-presentation": "PRESENTATION"};
		gradeFormatIsSet=false;
		for (i=0;i<gradingElts.length;++i) {
			if (gradingElts[i].checked) {
				grading_format=gradeFormatNames[gradingElts[i].id];
				gradeFormatIsSet=true;
			}
		}
		if (!gradeFormatIsSet) {
			return {"error": true,
					"error-msg": "Choose a grading format"};
		}

		let eventTypeElts = document.getElementsByName('event-type');
		let event_type="";
		let eventTypeNames={	"event-type-seminar": "SEMINAR",
								"event-type-course": "UNIVERSITY_COURSE",
								"event-type-cert": "CERTIFICATION",
								"event-type-cert-prep": "CERT_PREP_CLASS",
								"event-type-tech": "TECHNICAL_TRAINING",
								"event-type-other": "OTHER_EVENT"}
		let eventTypeIsSet=false;
		for (i=0;i<eventTypeElts.length;++i) {
			if (eventTypeElts[i].checked) {
				event_type=eventTypeNames[eventTypeElts[i].id];
				eventTypeIsSet=true;
			}
		}

		if (!eventTypeIsSet) {
			return {"error": true,
					"error-msg": "Choose an event type"};
		}
		
		let amount=parseFloat(document.getElementById("amount-input").children[0].value);
		if (isNaN(amount)) {
			return {"error": true,
					"error-msg": "Enter an amount"};
		}
		
		if (!hasDatePicked()) {
			return {"error": true,
					"error-msg": "Choose a date"};
		}
		
		return {
			"error": false,
			"location": document.getElementById("location-input").children[0].value,
			"eventdate": $("#datepickerdiv").datepicker().data('datepicker').getFormattedDate('yyyy-mm-dd'),
			"description": document.getElementById("description-input").children[0].value,
			"amount": amount,
			"grading-format": grading_format,
			"min-grade-input": document.getElementById("min-grade-input").children[0].value,
			"event-type": event_type,
			"justification": document.getElementById("justification-input").children[0].value,
			"work-time-missed": document.getElementById("work-time-missed-input").children[0].value};
	}
	
	function hasDatePicked() {
		if ($("#datepickerdiv").datepicker().data('datepicker').getFormattedDate('yyyy-mm-dd')=="") {
			return false;
		}
		return true;
	}
	
	function activateSuccessModal() {
		document.getElementById("submitSuccessTitle").innerText="Success";
		document.getElementById("submitSuccessBody").innerText="Your request has been submitted";
		$("#submitSuccessModal").modal()
	}
	
	function activateFailureModal(responseText) {
		document.getElementById("submitSuccessTitle").innerText="Failure";
		document.getElementById("submitSuccessBody").innerText="An error occured: "+responseText;
		$("#submitSuccessModal").modal()
	}
	
	function submit() {
		let formData=getFormData();
		if (formData.error) {
			activateFailureModal(formData["error-msg"]);
			return;
		}
		document.getElementById("submit-button").setAttribute("disabled",true);
		xhttp=new XMLHttpRequest();
		xhttp.open("POST","/project1/s/request.do");
		xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
		xhttp.onreadystatechange= function () {
			if (xhttp.readyState == 4) {
				document.getElementById("submit-button").removeAttribute("disabled");
				if (xhttp.status == 200) {
					activateSuccessModal();
				} else {
					activateFailureModal(xhttp.responseText);
				}
			}
		}
		xhttp.send(JSON.stringify(formData));
	}