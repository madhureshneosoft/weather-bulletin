# weather-bulletin

Prerequesities

-> Java 8+ <br />
-> Spring Boot <br />
-> Postman <br />

# About

Weather bulletin is a weather forecast API used to check the weather forecast of next 3 days for a given city. <br />
This API uses the OpenWeather API [ https://openweathermap.org/api ] to fetch the weather forecast for the given city. <br />
User needs to provide city name and work hours in the payload, API gives the average min/max temperature and humidity for work and non-work hours. <br />

# How to run

1. To run the application, get a free API key from openweathermap.com by registering an account.

Clone the application from github and then run the application and using postman provide the payload in following format : <br />
  { <br />
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "from":"HH:mm" 			[24 HOURS FORMAT, MANDATORY], <br />
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "to":"HH:mm" 			[24 HOURS FORMAT, MANDATORY], <br />
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "cityName":"CITY_NAME",<br />
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "stateCode":"STATE_CODE",<br />
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "countryCode":"COUNTRY_CODE"<br /> 
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "apiKey":"YOUR_API_KEY" 	[MANDATORY]<br />
  } <br />
	as a POST request on the URL [ http://localhost:8080/api/weather/forecast ].

2. Run the package task and the jar will be created in target folder.
You can also run the application using the jar file and use the API to get data.

-> Command to run jar file: java -jar jarName

3. You can use Docker File located in the main folder.
First create the jar file using above command.
Then cd into the folder and run command "sudo docker build -t nameOfImage ." .
Image will be created in the docker and you can use the API to get data.
