/**
 * 
 */

function displayList(requests) {
	for (i=0;i<requests.length;++i) {
		let tbody=document.getElementById("requestTableBody");
		let row=document.createElement("tr");
		let idField=document.createElement("th");
		let amountField=document.createElement("td");
		let descField=document.createElement("td");
		row.appendChild(idField);
		row.appendChild(amountField);
		row.appendChild(descField);
		tbody.appendChild(row);

		id=requests[i]['requestID']
		description = requests[i]['description']
		
		idField.innerText=id
		amountField.innerText=requests[i]['amount']
		descField.innerText=description
		
		row.onclick = function () {
			document.getElementById("modalTitle").innerText="Benefit Request "+id;
			document.getElementById("modalBody").innerText=description;
			$("#mainModal").modal()
		}
		console.log(requests[i]);
	}
}

function main() {
	let xhttp = new XMLHttpRequest();
	xhttp.open("GET","/project1/getAllRequests.do");
	xhttp.onreadystatechange=function () {
		if (xhttp.readyState == 4) {
			if (xhttp.status == 200) {
				console.log(xhttp.responseText);
				displayList(JSON.parse(xhttp.responseText));
			}
		}
	}
	xhttp.send();
}