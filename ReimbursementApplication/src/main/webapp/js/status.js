function getRequests(employee) {

	let xhr = new XMLHttpRequest();

	xhr.onreadystatechange = function() {

		if (xhr.readyState === 4 && xhr.status === 200) {
			let requestList = JSON.parse(xhr.responseText);
			for (let i = 0; i < requestList.length; i++) {
				let req = requestList[i]

				createRequestElement(req, employee);
			}
		}
	}

	xhr.open("GET", "../api/request/" + employee.username, true);

	xhr.send();
}

function getRequestsToApprove(employee) {

	let xhr = new XMLHttpRequest();

	xhr.onreadystatechange = function() {

		if (xhr.readyState === 4 && xhr.status === 200) {
			let requestList = JSON.parse(xhr.responseText);

			let elem = document.getElementById("finishedRequestsTitle");
			elem.parentNode.removeChild(elem);
			
			elem = document.getElementById("finishedRequests");
			elem.parentNode.removeChild(elem);
			
			for (let i = 0; i < requestList.length; i++) {
				let req = requestList[i]

                console.log(req.status + "    " + employee.type);
				if ((req.status === 0 && employee.type === "Supervisor")
						|| (req.status === 1
                                && employee.type === "DepartmentHead" || 
                                ((req.status === 2 || req.status === 4) && employee.type === "BenCo"))) {
					createRequestElement(req, employee);
				}
			}
		}
	}

	xhr.open("GET", "../api/request/" + employee.username, true);

	xhr.send();
}

function createRequestElement(req, employee) {

	let tr = document.createElement("tr");
	let th = document.createElement("th");
	th.setAttribute("scope", "row");
	th.innerHTML = req.requestId;

	let first = document.createElement("td");
	first.innerHTML = req.employee.firstName;

	let last = document.createElement("td");
	last.innerHTML = req.employee.lastName;

	let rdate = document.createElement("td");
	rdate.innerHTML = dateToString(req.requestDate);

	let etype = document.createElement("td");
	etype.innerHTML = req.type;

	let sdate = document.createElement("td");
	sdate.innerHTML = dateToString(req.startDate);

	let edate = document.createElement("td");
	edate.innerHTML = dateToString(req.endDate);

	// var t = document.createTextNode(req.requestId + ' ' +
	// dateToString(req.startDate) + ' to ' + dateToString(req.endDate));

	if (req.urgent && !(employee.type === "Employee")) {
		tr.setAttribute('style', 'color: red ');
	}

	let amount = document.createElement("td");
	amount.innerHTML = '$' + req.expectedAmount;

	let status = document.createElement("td");

	switch (req.status) {
	case -1:
		status.innerHTML = "Rejected";
		break;
	case 0:
		status.innerHTML = "Waiting for supervisor approval";
		break;
	case 1:
		status.innerHTML = "Waiting for department head approval";
		break;
	case 2:
		status.innerHTML = "Waiting for BenCo approval";
		break;
	case 3:
		status.innerHTML = "Waiting for final grades submission";
		break;
	case 4:
		status.innerHTML = "Waiting for final approval";
        break;
    case 5:
        status.innerHTML = "Approved";
        break;    
    
	}

	tr.append(th, first, last, rdate, etype, sdate, edate, amount, status);

	if (req.status === -1 || req.status === 5) {
		amount.innerHTML = '$' + req.awardedAmount;

		let comments = document.createElement("td");
		comments.innerHTML = req.comments;

		tr.append(comments);
		document.getElementById("finishedRequests").appendChild(tr);

	} else {
        
        if(!(employee.type === "Employee")){
            let b = document.createElement("button");
            b.innerHTML = "View";
            b.setAttribute('class', 'btn btn-success centered');
            b.addEventListener("click", function(event) {
                viewRequest(req);
            })

            tr.append(b);
        }
        else if(employee.type === "Employee" && req.status === 3){
        	let b = document.createElement("button");
            b.innerHTML = "Submit Grades";
            b.setAttribute('class', 'btn btn-success centered');
            b.addEventListener("click", function(event) {
                submitGrades(req);
            })
            
            tr.append(b);
        }

		document.getElementById("pendingRequests").appendChild(tr);

	}
}

function submitGrades(req){
    let xhr = new XMLHttpRequest();

	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status >= 200 && xhr.status < 300) {
            
            window.location = "./submitgrades.html";

		}
	}

	xhr.open("POST", "../status", true);

	xhr.send(JSON.stringify(req.requestId));
}

function viewRequest(req) {

	let xhr = new XMLHttpRequest();

	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status >= 200 && xhr.status < 300) {
            
            if(req.status === 4){
                window.location = "./finalapproval.html";
            }
            else{
                window.location = "./viewrequest.html";
            }

		}
	}

	xhr.open("POST", "../status", true);

	xhr.send(JSON.stringify(req.requestId));

}

function dateToString(date) {

	return date.monthValue + "/" + date.dayOfMonth + "/" + date.year;
}

function getCurrentEmployee() {

	let xhr = new XMLHttpRequest();

	xhr.onreadystatechange = function() {

		if (xhr.readyState === 4 && xhr.status === 200) {
			let employee = JSON.parse(xhr.responseText);
			
			document.getElementById("username").innerHTML = employee.firstName + " " + employee.lastName;
			if (!(employee === null)) {
		
                if(employee.type === "Employee"){
                    getRequests(employee);
                }
                else{
                    getRequestsToApprove(employee);
                }

			}
		}
	}

	xhr.open("GET", "../api/employee/current", true);

	xhr.send();
}

window.onload = function() {

	getCurrentEmployee();
}