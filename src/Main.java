import java.io.FileNotFoundException;
import java.util.Map;
import smartcity.gtfs.GTFSReader;
import smartcity.gtfs.Route;
import smartcity.gtfs.Service;
import smartcity.gtfs.Shape;
import smartcity.gtfs.Stop;
import smartcity.gtfs.Trip;

import java.util.Scanner;

public class Main {
	
	static Map<String,Stop> stops;
	static Map<String,Trip> trips;
	static double latInicial; 
	static double longInicial;
	static double latFinal;
	static double longFinal;
	static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) throws FileNotFoundException {
		takeInfos();
		reader();
		Gps gps = new Gps(stops, trips, latInicial, longInicial, latFinal, longFinal, 744.0);
		gps.start();
	}//close main
	
	private static void takeInfos() {
		System.out.println("Digite sua latitude atual: ");
		latInicial = Double.parseDouble(scan.next());
		System.out.println("Digite sua longitude atual: ");
		longInicial = Double.parseDouble(scan.next());	
		System.out.println("Digite sua latitude de destino: ");
		latFinal = Double.parseDouble(scan.next());
		System.out.println("Digite sua longitude de destino: ");
		longFinal = Double.parseDouble(scan.next());
	}//close method to take lat and long

	public static void reader() throws FileNotFoundException {
		System.out.println("Estamos calculando sua rota. Por favor aguarde...");
		stops = GTFSReader.loadStops("bin/arquivos/stops.txt");
		Map<String,Route>routes = GTFSReader.loadRoutes("bin/arquivos/routes.txt");
		Map<String,Shape> shapes = GTFSReader.loadShapes("bin/arquivos/shapes.txt");
		Map<String,Service> calendar = GTFSReader.loadServices("bin/arquivos/calendar.txt");
		trips = GTFSReader.loadTrips("bin/arquivos/trips.txt",routes,calendar,shapes);
		GTFSReader.loadStopTimes("bin/arquivos/stop_times.txt", trips, stops);
		System.out.println("Rota calculada!\n");
	}//close files reader

}//close class
