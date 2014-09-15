package de.uulm.miss;

import java.util.LinkedList;

import android.os.Messenger;

/**
 * @author Fabian Schwab
 * 
 * This contains the calling application and its clients and stations.
 * 
 */
public class ScanOrder{
	private Messenger messenger;
	private LinkedList<Client> clients;
	private LinkedList<Station> stations;
	
	/**
	 * @param messenger This is needed to send information back to the calling application.
	 */
	public ScanOrder(Messenger messenger) {
		super();
		this.messenger = messenger;
		clients = new LinkedList<Client>();
		stations = new LinkedList<Station>();
	}

	/**
	 * @return Returns a bound application to this service. 
	 */
	public Messenger getMessenger() {
		return messenger;
	}
	/**
	 * @return
	 */
	public LinkedList<Client> getClients() {
		return clients;
	}
	/**
	 * @return
	 */
	public LinkedList<Station> getStations() {
		return stations;
	}
}

