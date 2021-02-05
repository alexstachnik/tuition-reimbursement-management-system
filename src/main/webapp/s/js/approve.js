/**
 * 
 */

var table;

function displayInfo(request) {
	let gradingFormat=request["GradingFormat"];
	let amount=request["amount"];
	let description = request['description'];
	let employeeName = request["employeeName"];
	let eventType = request["eventType"];
	let justification = request["justification"];
	let eventLocation = request["location"];
	let minGrade = request["minGrade"];
	let id=request['requestID'];
	let status= request["status"];
	let eventTime = request["eventTime"];
	let timestamp = request["timestamp"];
	
	let eventTypeValues={	"SEMINAR": 0.6,
							"UNIVERSITY_COURSE": 0.8,
							"CERTIFICATION": 1.0,
							"CERT_PREP_CLASS": 0.75,
							"TECHNICAL_TRAINING": 0.9,
							"OTHER_EVENT": 0.3}
	let amountModifier = 0.3;
	if (eventType != null) {
		amountModifier=eventTypeValues[eventType];
	}
	modifiedAmount=amountModifier*amount;
	
	var cleanTimestamp="";
	if (typeof(timestamp) == "string") {
		let timestampParts=timestamp.split(":")
		cleanTimestamp=timestampParts[0]+":"+timestampParts[1]
	}
	var cleanEventTime=""
	if (typeof(eventTime) == "string") {
		cleanEventTime=eventTime.split("T")[0];
	}
	
	let addRow=function (fieldName,fieldValue) {
		let subrow=document.createElement("tr");
		let col1=document.createElement("th");
		col1.innerText=fieldName;
		let col2=document.createElement("td");
		col2.innerText=fieldValue;
		subrow.appendChild(col1);
		subrow.appendChild(col2);
		table.append(subrow);
	}
	
	document.getElementById("reimbursementAmt").value=modifiedAmount.toFixed(2);
	
	addRow("Reimbursement Amount ($)",modifiedAmount.toFixed(2));
	addRow("Amount ($)",amount.toFixed(2));
	addRow("Description",description);
	addRow("Grading format",gradingFormat);
	addRow("Employee Name",employeeName);
	addRow("Event Type",eventType);
	addRow("Justification",justification);
	addRow("Location",eventLocation);
	addRow("Minimum Grade",minGrade);
	addRow("Event Date",cleanEventTime);
	addRow("Time when request was made", cleanTimestamp);
}

function lookupEmployee(request) {
	let xhttp = new XMLHttpRequest();
	let employeeID=request['employeeID'];
	xhttp.open("GET","/project1/s/employeeMap.do?employeeID="+employeeID);
	xhttp.onreadystatechange=function () {
		if (this.readyState==4) {
			if (this.status==200) {
				employee=JSON.parse(this.responseText);
				request['employeeName'] = employee['name'][0];
				displayInfo(request);
			}
		}
	}
	xhttp.send();
}

function main() {
	table=document.getElementById("requestInfoTable");
	let url = new URL(window.location.href);
	let requestID = url.searchParams.get('requestID');
	if (requestID == null) {
		return;
	}
	
	let xhttp = new XMLHttpRequest();
	xhttp.open("GET","/project1/s/getRequest.do?requestID="+requestID);
	xhttp.onreadystatechange=function () {
		if (this.readyState==4) {
			if (this.status==200) {
				let request = JSON.parse(this.responseText);
				lookupEmployee(request);
			}
		}
	}
	xhttp.send();
}