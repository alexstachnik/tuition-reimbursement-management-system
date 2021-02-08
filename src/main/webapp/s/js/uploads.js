/**
 * 
 */

var requestID;

function submitGrade() {
	let xhttp = new XMLHttpRequest();
	xhttp.open("POST","/project1/s/grade.do");
	xhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	xhttp.onreadystatechange=function () {
		if (this.readyState==4) {
			if (this.status==200) {
				window.location.href="/project1/s/";
			}
		}
	}
	
	grade = document.getElementById("grade-input").value;
	
	xhttp.send("requestID="+requestID+"&grade="+grade);
}

function submitFile() {
	let file=document.getElementById("fileUpload").files[0]
	let xhttp = new XMLHttpRequest();
	let formData = new FormData();
	
	formData.append("requestID", requestID);
	if (document.getElementById("file-type-presentation").checked) {
		formData.append("uploadType","presentation");
	} else {
		formData.append("uploadType","email");
	}
	formData.append("fileType",file.name.split(".")[1]);
	formData.append("file", file);
	xhttp.open("POST","/project1/s/upload.post");
	xhttp.onreadystatechange = function () {
		//window.location.href="/project1/s/";
	}
	xhttp.send(formData);
}

function buttonPress() {
	requestID = this.getAttribute("requestID");
	$("#mainModal").modal('toggle');
}

function procList(requestList) {
	$("#mainModal").modal();
	let body=document.getElementById("modalBody");
	body.innerHTML="";
	
	for (i=0;i<requestList.length;++i) {
		let divElt = document.createElement("div");
		divElt.classList.add("modal-button");
		let buttonElt = document.createElement("button");
		buttonElt.setAttribute("type","button");
		buttonElt.classList.add("btn");
		buttonElt.classList.add("btn-secondary");
		buttonElt.setAttribute("requestID",requestList[i].requestID);
		buttonElt.onclick=buttonPress;
		divElt.appendChild(buttonElt);
		body.appendChild(divElt);
		buttonElt.innerText=requestList[i].description;
	}
	
}

function main() {
	document.getElementById("file-type-presentation").checked=true;
	let xhttp = new XMLHttpRequest();
	xhttp.open("GET","/project1/s/getAllMyRequests.do");
	xhttp.onreadystatechange=function () {
		if (xhttp.readyState == 4) {
			if (xhttp.status == 200) {
				requestList=JSON.parse(xhttp.responseText);
				procList(requestList);
			}
		}
	}
	xhttp.send();
	
}