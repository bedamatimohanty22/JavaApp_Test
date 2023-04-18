# JavaApp_Test
Test App for Bus Stop functionality


This application is written in Java using Java and json library functions. External library required to run the code is included in the /lib folder.

When the code is executed, it connects to a couple of APIs from Trafiklab's Open API mentioned on this link : http://www.trafiklab.se/api/sl-hallplatser-och-linjer-2

The application finds Top 10 bus lines having the most bus stops on their route. It also lists the name of every bus stop of these Top 10 bus lines. There is no requirement on how the bus stops are sorted.  

From reading the documentation on Open API page, it is understood that there are 2 APIs that we need to connect to : 
First is JourneyPatternPointOnLine - which gives the connection between Line and how many stops it contains.
Second is StopPoint - which gives the name of each bus stop.

Both of these information need to be combined to get the desired outcome. For our purpose, we have used an optional parameter to get only BUS details for this application, but it can be easily changed for any other traffic mode (METRO, TRAM,etc). This is done by adding this to suffix of API URL : &DefaultTransportModeCode=BUS

This code is divided into the below steps:
1. Use the first API URL to fetch the data related to each Line and how many stops it contains. The API response will be in JSON format, so we have used a JSON parser to extract the relevant information from the response. The information needed from this response is the LineNumber, DirectionCode, and JourneyPatternPointNumber.
2. Filter the data for DirectionCode = 1. This is because every Line would have the same number of stops for each Direction (1 or 2). So, we use this filter to not "double-count" bus stops for each line.
3. Compute the Top 10 Bus Lines with the most number of stops.
4. For each of these Top 10 Bus Lines, we now need to use the second API URL to fetch the stop names.
5. Combine the result set with the response from the second API URL to display the stop names.
