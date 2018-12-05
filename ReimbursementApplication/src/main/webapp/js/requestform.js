function makeRequest(){
	
	let xhr = new XMLHttpRequest();
	
	xhr.onreadystatechange = function(){
		
		if(xhr.readyState === 4 && xhr.status === 201){
			
		}
	}
	
	xhr.open("POST", "../requestform", true);
	
	let request = createRequest();
	
	xhr.send(JSON.stringify(request));
}

function createRequest(){
	let request = {};
    
    request.startDate = document.getElementById("startDate").value;
    request.endDate = document.getElementById("endDate").value;
    request.location = document.getElementById("location").value;
    request.gradingFormat = document.getElementById("location").value;
    request.eventType = document.getElementById("location").value;
    request.cost = document.getElementById("location").value;
    request.description = document.getElementById("location").value;
    request.justification = document.getElementById("location").value;
    request.missedHours = document.getElementById("location").value;
    request.files = document.getElementById("location").value;
    request.preApproval = document.getElementById("location").value;

    if(document.getElementById("bencoApproval").value){
        request.status = 3;
    }
    else if (document.getElementById("departmentHeadApproval").value){
        request.status = 2;
    }
    else if (document.getElementById("supervisorApproval").value){
        request.status = 1;
    }
    else{
        request.status = 0;
    }
	
	return request
}

window.onload = function(){
	document.getElementById("submitForm").addEventListener("click", makeRequest, true);
}