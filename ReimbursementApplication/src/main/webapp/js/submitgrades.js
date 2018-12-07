function getCurrentEmployee() {

	let xhr = new XMLHttpRequest();

	xhr.onreadystatechange = function() {

		if (xhr.readyState === 4 && xhr.status === 200) {
			let employee = JSON.parse(xhr.responseText);
			
			document.getElementById("username").innerHTML = employee.firstName + " " + employee.lastName;
		}
	}

	xhr.open("GET", "../api/employee/current", true);

	xhr.send();
}

window.onload = function() {
	
	getCurrentEmployee();
}