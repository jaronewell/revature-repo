function getRequests(employee){

    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function() {

        if(xhr.readyState === 4 && xhr.status === 200){
            let requestList = JSON.parse(xhr.responseText);
            for (let i = 0; i < requestList.length; i++){
                let req = requestList[i]
                
                createRequestElement(req, employee);
            }
        }
    }

    xhr.open("GET", "../api/request/" + employee.username, true);

    xhr.send();
}

function createRequestElement(req, employee){
	
	let tr = document.createElement("tr");
    let th = document.createElement("th");
    th.setAttribute("scope", "row");
    th.innerHTML=req.requestId;

    let first = document.createElement("td");
    first.innerHTML=employee.firstName;

    let last = document.createElement("td");
    last.innerHTML=employee.lastName;

    let rdate = document.createElement("td");
    rdate.innerHTML=dateToString(req.requestDate);

    let etype = document.createElement("td");
    etype.innerHTML=req.type;

    let sdate = document.createElement("td");
    sdate.innerHTML=dateToString(req.startDate);

    let edate = document.createElement("td");
    edate.innerHTML=dateToString(req.endDate);

    //var t = document.createTextNode(req.requestId + ' ' + dateToString(req.startDate)  + ' to ' + dateToString(req.endDate));
    
    if(req.urgent){
        tr.setAttribute('style', 'color: red ');
    }
    
    // switch(req.status){
    //     case 0:
    //         t.appendData( " Waiting for supervisor approval");
    //         break;
    //     case 1:
    //         t.appendData( " Waiting for department head approval");
    //         break;
    //     case 2:
    //         t.appendData( " Waiting for Benco approval");
    //         break;
    //     case 3:
    //         t.appendData( " Waiting for final grades approval");
    //         break;
    // }
    
    tr.append(th, first, last, rdate, etype, sdate, edate);
    document.getElementById("pendingRequests").appendChild(tr);
}

function dateToString(date){
	
	return date.month + " " + date.dayOfMonth + " " + date.year;
}

function getCurrentEmployee(){
	
	let xhr = new XMLHttpRequest();
	
	 xhr.onreadystatechange = function() {

	        if(xhr.readyState === 4 && xhr.status === 200){
	            let employee = JSON.parse(xhr.responseText);
                
	            if(!(employee === null)){
                    document.getElementById("signinbutton").innerText = "Sign Out";

    	            getRequests(employee);
	            }
	        }
	    }

	    xhr.open("GET", "../api/employee/current", true);

	    xhr.send();
}


window.onload = function() {
    
   getCurrentEmployee();
}