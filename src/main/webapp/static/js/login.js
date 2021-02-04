/**
 * 
 */


function testLoginFailure() {
	const queryString = window.location.search;
	const urlParams = new URLSearchParams(queryString);
	if (urlParams.has("loginFailed") && (urlParams.get("loginFailed") =="true")) {
		let h1elt = document.getElementById("signintext");
		h1elt.innerHTML="Unsuccessful Login,<br> try again";
	} 
}