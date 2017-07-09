import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import smartcity.gtfs.*;
import smartcity.util.*;

public class Gps {
	
	private Double latInicial; 
	private Double longInicial;
	private Double latFinal;
	private Double longFinal;
	private double limite;
	private String[] resultados = new String[20];
	private int numElementos = 0;
	private double smallLimit;
	
	private Map<String,Stop> stops;
	private Map<String,Trip> trips;
	
	public Gps(Map<String,Stop> stops, Map<String,Trip> trips, Double laI, Double loI, Double laF, Double loF, double limite) {
		this.stops = stops;
		this.trips = trips;
		this.latInicial = laI;
		this.latFinal = laF;
		this.longInicial = loI;
		this.longFinal = loF;
		this.limite = limite;
	}//constructer
	
	public void start() {
		GPSCoordinate origem = new GPSCoordinate(latInicial, longInicial);
		GPSCoordinate destino = new GPSCoordinate(latFinal, longFinal);
		calcRoute(origem, destino, limite);
	}

	private void calcRoute(GPSCoordinate origem, GPSCoordinate destino, double limite) {
		List<Stop> initalStop = listStops(origem, stops, limite);
		Stop finalStop = finalStop(destino, stops);
		List<Trip> travelList = listTrip(finalStop.getGPSCoordinate(), limite, trips);
		calcTravel(initalStop, finalStop, travelList, limite, origem);		
	}

	private void calcTravel(List<Stop> initalStop, Stop finalStop, List<Trip> travelList, double limite,
			GPSCoordinate origem) {
		boolean found = takeRoute(initalStop, travelList, limite);
		
		if(found){
			showResult();
		}else{
			System.out.println("PRECISA DE MAIS ROTAS");
		}
		
	}
	
	private boolean takeRoute(List<Stop> initalStop, List<Trip> travelList, double limite) {
		Boolean found = false;
		
		for (Trip t : travelList) {
			for (Stop s : initalStop) {
				if (t.hasStopNear(s.getGPSCoordinate(), smallLimit)) { 
					append("LINHA: " + t.getRoute().getLongName() + " - " + t.getRoute().getShortName()
							+ "\nPARADA: " + s.getName());
					found = true;
				}
			}
		}
		return found;
	}

	private List<Stop> listStops(GPSCoordinate origem, Map<String, Stop> stops, double limite) {
		List<Stop> listStops = new ArrayList<Stop>();
		smallLimit = limite;
		for(int i = 0; i <= 1; i++){
			for (Map.Entry<String, Stop> s : stops.entrySet()) {
				Stop stop = s.getValue();
				GPSCoordinate coordinate = stop.getGPSCoordinate();
				Double distance = origem.distance(coordinate);
				if (distance <= smallLimit) {
					smallLimit = distance;
					if(i == 1){
						listStops.add(stop);
					}
				}
			}
		}
		return listStops;
	}
	
	private Stop finalStop(GPSCoordinate destino, Map<String, Stop> stops) {
		Stop finalStop = new Stop("","",0,0);
		Double maxValue = Double.MAX_VALUE;
		
		for(Map.Entry<String, Stop> stop : stops.entrySet()){
			GPSCoordinate coordinate = stop.getValue().getGPSCoordinate();
			Double distance = destino.distance(coordinate);
			if (distance < maxValue) {
				maxValue = distance;
				finalStop = stop.getValue();
			}
		}
		return finalStop;
	}
	
	private List<Trip> listTrip(GPSCoordinate gpsCoordinate, double limite, Map<String, Trip> trips) {
		List<Trip> listTrip = new ArrayList<Trip>();
		List<String> listTripNames = new ArrayList<String>();
		for (Map.Entry<String, Trip> t : trips.entrySet()) {
			Trip trip = t.getValue();
			if (trip.hasStopNear(gpsCoordinate, limite) && !listTripNames.contains(trip.getRoute().getShortName())) {
				listTrip.add(t.getValue());
				listTripNames.add(t.getValue().getRoute().getShortName());
			}
		}
		return listTrip;
	}
	
	private void moreSpace() {
		if (numElementos >= resultados.length) {
			String[] novo = new String[resultados.length * 2];
			for (int i = 0; i < resultados.length; i++) {
				novo[i] = resultados[i];
			} 
			resultados = novo;
		} 
	}
	
	private void append(String info) {
		moreSpace();
		if(!validInfo(info)){
			resultados[numElementos] = info;
			numElementos++;
		}
	}
	
	private boolean validInfo(String info) {
		if (numElementos > 0) {
			for (int i = 0; i < numElementos; i++) {
				if (resultados[i].equals(info)) {
					return true;
				}
			}

		}
		return false;
	}
	
	private void showResult(){
		System.out.println("OPÇÕES MAIS PRÓXIMAS DE VOCÊ:\n");
		for(int i = 0; i < numElementos; i++){
			System.out.println(resultados[i]+"\n");
		}
	}
	
}//close class
