# weather-bulletin

Prerequesities

-> Java 8+ <br />
-> Spring Boot <br />
-> Postman <br />

# About

Weather bulletin is a weather forecast API used to check the weather forecast of next 3 days for the required city. <br />
This API uses the OpenWeather API [ https://openweathermap.org/api ] to fetch the weather forecast for the given city. <br />
User needs to provide city name and work hours in the payload, API gives the average min/max temperature and humidity for work and non-work hours. <br />

# How to run

To run the application, get a free API key from openweathermap.com by registering an account and put it to file /src/main/java/com/neosoft/weatherbulletin/util/UrlBuilder.java file

Clone the application from github and then run the application and using postman provide the payload in following format : <br />
  { <br />
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 'from':'HH:mm'                [24 HOURS FORMAT, MANDATORY], <br />
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 'to':'HH:mm'                  [24 HOURS FORMAT, MANDATORY], <br />
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 'cityName':'YOUR_CITY'        [MANDATORY], <br />
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 'stateCode':'STATE_CODE'      [OPTIONAL], <br />
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 'countryCode':'COUNTRY_CODE'  [OPTIONAL] <br /> 
  } <br />
	as a POST request on the URL [ http://localhost:8080/api/weather/forecast ].
