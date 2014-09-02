package de.uulm.miss;

import java.util.LinkedList;

import android.os.Messenger;

public class ScanOrder{
	private Messenger messenger;
	private LinkedList<Client> clients;
	private LinkedList<Station> stations;
	
	public ScanOrder(Messenger messenger) {
		super();
		this.messenger = messenger;
		clients = new LinkedList<Client>();
		stations = new LinkedList<Station>();
	}

	public Messenger getMessenger() {
		return messenger;
	}
	public LinkedList<Client> getClients() {
		return clients;
	}
	public LinkedList<Station> getStations() {
		return stations;
	}
}

