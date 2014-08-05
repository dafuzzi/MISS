package de.uulm.miss;

import java.util.LinkedList;

public class AirTrafficAnalyzer implements Runnable {

	LinkedList<Station> findStations;
	LinkedList<Client> findClients;
	FileParser fp;

	public AirTrafficAnalyzer(LinkedList<Station> findStations, LinkedList<Client> findClients) {
		super();
		this.findStations = findStations;
		this.findClients = findClients;
	}

	@Override
	public void run() {
		startScan();
		try {
			wait(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stopScan();
	}

	private void stopScan() {
		// TODO Auto-generated method stub

	}

	private void startScan() {
		// TODO Auto-generated method stub

	}

}
