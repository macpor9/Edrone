# Application has 3 endpoints:<br> 
### 1. http://localhost:8080/jobs/count - get currently running jobs (max 4 allowed) <br>
### 2. http://localhost:8080/jobs/{id} - get file with id provided in path parameter <br>
### 3. http://localhost:8080/jobs - post request to generate file, constructed as following: <br>
{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "numberOfString": 65536, <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "length": 8,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "allowedChars": ["a","b","c","d"]<br>
}
