/**
 * 
 */

function displayList(requests) {
	let tbody=document.getElementById("requestTableBody");
	tbody.innerHTML="";
	
	filterClosed=true;
	if (document.getElementById("closedRequestsCheckbox").checked) {
		filterClosed=false;
	}
	filterApproved=true;
	if (document.getElementById("approvedRequestsCheckbox").checked) {
		filterApproved=false;
	}
		
	for (i=requests.length-1;i>=0;--i) {
		if (filterClosed && requests[i].status == "CLOSED") {
			continue;
		}
		if (filterApproved && requests[i].employeeHasApproved) {
			continue;
		}
		let row=document.createElement("tr");
		let idField=document.createElement("th");
		let amountField=document.createElement("td");
		let descField=document.createElement("td");
		row.appendChild(idField);
		row.appendChild(amountField);
		row.appendChild(descField);
		tbody.appendChild(row);
		
		if (requests[i].status == "CLOSED") {
			row.classList.add("table-dark");
		} else if (requests[i].employeeHasApproved) {
			row.classList.add("table-danger");
		}

		let gradingFormat=requests[i]["GradingFormat"];
		let amount=requests[i]["amount"];
		let description = requests[i]['description'];
		let employeeID = requests[i]["employeeID"];
		let eventType = requests[i]["eventType"];
		let justification = requests[i]["justification"];
		let eventLocation = requests[i]["location"];
		let minGrade = requests[i]["minGrade"];
		let id=requests[i]['requestID'];
		let eventTime = requests[i]["eventTime"];
		let timestamp = requests[i]["timestamp"];
		
		let modalTable=document.createElement("table");
		let addRow=function (fieldName,fieldValue) {
			let subrow=document.createElement("tr");
			let col1=document.createElement("th");
			col1.innerText=fieldName;
			let col2=document.createElement("td");
			col2.innerText=fieldValue;
			subrow.appendChild(col1);
			subrow.appendChild(col2);
			modalTable.append(subrow);
		}
		
		var cleanTimestamp="";
		if (typeof(timestamp) == "string") {
			let timestampParts=timestamp.split(":")
			cleanTimestamp=timestampParts[0]+":"+timestampParts[1]
		}
		var cleanEventTime="";
		if (typeof(eventTime) == "string") {
			cleanEventTime=eventTime.split("T")[0];
		}
		

		idField.innerText=id;
		amountField.innerText=amount;
		descField.innerText=description;
		
		let popupModal = function (employeeName) {
			currentRequest=id;
			document.getElementById("modalTitle").innerText="Benefit Request "+id;
			document.getElementById("modalBody").innerHTML="";
			document.getElementById("modalBody").appendChild(modalTable);
			$("#mainModal").modal();

			addRow("Amount ($)",amount);
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
		row.onclick= function() {
			let xhttp = new XMLHttpRequest();
			xhttp.open("GET","/project1/s/employeeMap.do?employeeID="+employeeID);
			xhttp.onreadystatechange = function() {
				if (this.readyState == 4) {
					if (this.status == 200) {
						employee=JSON.parse(this.responseText);
						popupModal(employee['name'][0]);
					}
				}
			}
			xhttp.send();
		}
		console.log(requests[i]);
	}
}

function approve() {
	window.location.href="/project1/s/approve.jsp?requestID="+currentRequest;
}

var approvalList;
var currentRequest;

function main() {
	document.getElementById("approvedRequestsCheckbox").onchange=function() {
		displayList(approvalList);
	}
	
	document.getElementById("closedRequestsCheckbox").onchange=function() {
		displayList(approvalList);
	}
	
	let xhttp = new XMLHttpRequest();
	xhttp.open("GET","/project1/s/getAllRequests.do");
	xhttp.onreadystatechange=function () {
		if (xhttp.readyState == 4) {
			if (xhttp.status == 200) {
				console.log(xhttp.responseText);
				approvalList=JSON.parse(xhttp.responseText);
				displayList(approvalList,true);
			}
		}
	}
	xhttp.send();
}