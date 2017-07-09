import java.io.FileNotFoundException;
import java.util.Map;
import smartcity.gtfs.GTFSReader;
import smartcity.gtfs.Route;
import smartcity.gtfs.Service;
import smartcity.gtfs.Shape;
import smartcity.gtfs.Stop;
import smartcity.gtfs.Trip;

public class Main {
	
	static Map<String,Stop> stops;
	static Map<String,Trip> trips;
	
	public static void main(String[] args) throws FileNotFoundException {
		
		reader();
		Gps gps = new Gps(stops, trips, -30.067852, -51.161605, -30.022673, -51.195715, 744.0);
		gps.start();

	}//close main
	
	public static void reader() throws FileNotFoundException {
		System.out.println("Calculando rota..");
		stops = GTFSReader.loadStops("bin/arquivos/stops.txt");
		
		System.out.println("Calculando rota...");
		Map<String,Route>routes = GTFSReader.loadRoutes("bin/arquivos/routes.txt");
		
		System.out.println("Calculando rota....");
		Map<String,Shape> shapes = GTFSReader.loadShapes("bin/arquivos/shapes.txt");
		
		System.out.println("Calculando rota.....");
		Map<String,Service> calendar = GTFSReader.loadServices("bin/arquivos/calendar.txt");
		
		System.out.println("Calculando rota......");
		trips = GTFSReader.loadTrips("bin/arquivos/trips.txt",routes,calendar,shapes);
		
		System.out.println("Calculando rota.......");
		GTFSReader.loadStopTimes("bin/arquivos/stop_times.txt", trips, stops);
		System.out.println("Rota calculada!\n");
	}//close files reader

}//close class
