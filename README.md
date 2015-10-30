AFCC
====

This app is for calculating average fuel consumption of vehicles. It can calculate l/100 km, km/l and mpg.
For addition there is an average speed calculator, which uses time and distance travelled to calculate the average speed and a speedometer
which uses GPS data to calculate speed.

Settings - Lets select which units to use for distance, amount, consumption and speed. It saves data to database. If "Restore defaults"
	is clicked, default units are used for distance (km), amount (litres), consumption (l/100km) and speed (km/h).

Average Fuel Consumption Calculator - Allows to calculate average fuel consumption based on distance travelled and amount of fuel consumed.
	It uses units set in settings "Calculate" button initializes the calculation, "Save" button saves result to database and
	"View Saved Results" shows previously saved results.
	
Previous Results - Shows previously saved results as a list with date when the result was saved. When long-clicked on the list item,
	alert view appears to allow deletion of that result.
	
Average Speed Calculator - Calculates average speed based on distance and time taken. It uses distance unit set in settings.
	Start and end time are set using TimePickers and it shows time in 24 hour format. "Calculate" button initializes the calculation of
	average speed.
	
Speedometer - Shows current speed based on device's GPS data. Uses speed unit that was set in settings.
