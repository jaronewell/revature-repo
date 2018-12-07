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

function getInformation(){

    let xhr = new XMLHttpRequest();
	
    xhr.onreadystatechange = function() {

        if (xhr.readyState === 4 && xhr.status === 200) {
            let request = JSON.parse(xhr.responseText);

            if (!(request === null)) {
                document.getElementById("firstName").setAttribute("value", request.employee.firstName);
                document.getElementById("lastName").setAttribute("value", request.employee.lastName);
                document.getElementById("requestDate").setAttribute("value", dateToString(request.requestDate));
                document.getElementById("startDate").setAttribute("value", dateToString(request.startDate));
                document.getElementById("endDate").setAttribute("value", dateToString(request.endDate));
                document.getElementById("location").setAttribute("value", request.location);
                document.getElementById("gradingFormat").setAttribute("value", request.format);
                document.getElementById("eventType").setAttribute("value", request.type);
                document.getElementById("cost").setAttribute("value", request.cost);
                document.getElementById("description").setAttribute("placeholder", request.description);
                document.getElementById("justification").setAttribute("placeholder", request.justification);
                document.getElementById("missedHours").setAttribute("value", request.missedHours);

            }
        }
    }

       xhr.open("GET", "../view", true);

       xhr.send();
}

function dateToString(date){
	return date.monthValue + "/" + date.dayOfMonth + "/" + date.year;
}

function approve(){
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status >= 200 && xhr.status < 300) {
            
            window.location = "../status";

        }

    }

    xhr.open("POST", "../view", true);

    xhr.send(JSON.stringify("approve"));

}

function reject(){
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status >= 200 && xhr.status < 300) {
            
            window.location = "../status";

        }

    }

    xhr.open("POST", "../view", true);

    xhr.send(JSON.stringify("reject"));
}

function info(){
    window.location = "../info.html";
}

window.onload = function() {
    getInformation();
    getCurrentEmployee();
    document.getElementById("approvebtn").addEventListener("click", approve, true);
    document.getElementById("rejectbtn").addEventListener("click", reject, true);
    document.getElementById("infobtn").addEventListener("click", info, true);
    
}