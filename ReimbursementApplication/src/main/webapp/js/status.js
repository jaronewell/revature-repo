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

function getRequestsToApprove(employee){

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

function createRequestElement(req){
    
	let tr = document.createElement("tr");
    let th = document.createElement("th");
    th.setAttribute("scope", "row");
    th.innerHTML=req.requestId;

    let first = document.createElement("td");
    first.innerHTML=req.employee.firstName;

    let last = document.createElement("td");
    last.innerHTML=req.employee.lastName;

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
    
    let amount = document.createElement("td");
    amount.innerHTML = '$' + req.expectedAmount;

    let status = document.createElement("td");

    switch(req.status){
        case -1:
            status.innerHTML="Rejected";
            break;
        case 0:
            status.innerHTML="Waiting for supervisor approval";
            break;
        case 1:
            status.innerHTML="Waiting for department head approval";
            break;
        case 2:
            status.innerHTML="Waiting for Benco approval";
            break;
        case 3:
            status.innerHTML="Waiting for final grades approval";
            break;
        case 4:
            status.innerHTML="Approved";
            break;
    }
    

    tr.append(th, first, last, rdate, etype, sdate, edate, amount, status);
    
    if(req.status === -1 || req.status === 4){
        amount.innerHTML = '$' + req.awardedAmount;
        
        let comments = document.createElement("td");
        comments.innerHTML = req.comments;
        
        tr.append(comments);
        document.getElementById("finishedRequests").appendChild(tr);

    }
    else{
        
        let b = document.createElement("button");
        b.innerHTML = "View";
        b.setAttribute('class', 'btn btn-success centered');
        b.addEventListener("click", function(event){
            viewRequest(req);
        })
    
        tr.append(b);

        document.getElementById("pendingRequests").appendChild(tr);

    }
}

function viewRequest(req){

    let xhr = new XMLHttpRequest();
    
    if(xhr.readyState === 4 && xhr.status === 201){
        let url = JSON.parse(xhr.responseText);
        console.log("in relocation");
        window.location = "./viewrequest.html";
    }

    xhr.open("POST", "../status", true);

    xhr.send(JSON.stringify(req.requestId));
}

function dateToString(date){
	
	return date.monthValue + "/" + date.dayOfMonth + "/" + date.year;
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