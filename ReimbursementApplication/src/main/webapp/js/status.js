function getRequests(employee){

    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function() {

        if(xhr.readyState === 4 && xhr.status === 200){
            let requestList = JSON.parse(xhr.responseText);
            for (let req in requestList){
                var p = document.createElement("p");
                var t = document.createTextNode(req.requestId + ' ' + req.startDate + ' to ' + req.endDate);
                p.appendChild(t);
                document.body.appendChild(p);
            }
        }
    }


    xhr.open("GET", "../api/request/" + employee.username, true);

    xhr.send();
}

function getCurrentEmployee(){
	
	let xhr = new XMLHttpRequest();
	
	 xhr.onreadystatechange = function() {

	        if(xhr.readyState === 4 && xhr.status === 200){
	            let employee = JSON.parse(xhr.responseText);
	            
	            if(!(employee === null)){
                    document.getElementById("signinbutton").innerText = "Sign Out";
                    document.getElementById("signinbutton").innerText = employee.firstName;
	            }
	            
	            return employee;
	        }
	    }

	    xhr.open("GET", "../api/employee/current", true);

	    xhr.send();
}


window.onload = function() {
    
    let employee = getCurrentEmployee();
    getRequests(employee);
}