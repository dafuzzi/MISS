package de.uulm.miss;

import java.io.File;
import java.util.LinkedList;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * @author Fabian Schwab
 * 
 */
public class MISService extends Service {

	private LinkedList<Client> clients;
	private LinkedList<Station> stations;
	private String appDataPath;

	private static Thread scanner;

	public MISService() {
		clients = new LinkedList<Client>();
		stations = new LinkedList<Station>();
		appDataPath = "/datadata/de.uulm.miss/files/capture-01.csv";

		if (scanner == null) {
			scanner = new Thread(new AirTrafficAnalyzer(this));
		}
		
		clients.add(new Client("Fabian Phone", "90:27:E4:32:F2:03"));
		clients.add(new Client("Fabian Mac", "98:FE:94:49:E9:E2"));
		clients.add(new Client("Seba Phone", "3C:C2:43:C9:6D:9C"));
		clients.add(new Client("Seba Laptop", "6C:71:D9:50:36:19"));
		clients.add(new Client("Random MAC", "38:AA:3C:64:20:D6"));
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Log.d("MISS", "Service started");
		if (!scanner.isAlive()) {
			scanner.start();
		}
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public boolean stopService(Intent name) {
		return super.stopService(name);
	}
	@Override
	public void onDestroy() {
		//TODO geht nicht zu stoppen
		scanner.interrupt();
		Log.d("MISS", "Service stopped");
		super.onDestroy();
	}

	/**
	 * 
	 */
	private void readDevicesFromFile() {
		// TODO Implement: for permanet storage of data
	}

	/**
	 * 
	 */
	private void saveDevicesToFile() {
		// TODO Implement: for permanet storage of data
	}

	/**
	 * @return
	 */
	private boolean activateMonitor() {
		// TODO implement
		return false;
	}

	/**
	 * @return
	 */
	private boolean deaktivateMonitor() {
		// TODO implement
		return false;
	}

	/**
	 * @param client
	 * @return
	 */
	private boolean addClient(Client client) {
		return clients.add(client);
	}

	private boolean addStation(Station station) {
		return stations.add(station);
	}

	/**
	 * @param client
	 * @return
	 */
	private boolean removeClient(Client client) {
		for (Client cl : clients) {
			if (cl.getMAC() == client.getMAC()) {
				return clients.remove(cl);
			}
		}
		return false;
	}

	/**
	 * @param station
	 * @return
	 */
	private boolean removeSation(Station station) {
		for (Station st : stations) {
			if (st.getMAC() == station.getMAC()) {
				return stations.remove(st);
			}
		}
		return false;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected LinkedList<Station> getStations() {
		return (LinkedList<Station>) stations.clone();
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected LinkedList<Client> getClients() {
		return (LinkedList<Client>) clients.clone();
	}

	/**
	 * @return
	 */
	protected File getPath() {
		return new File(appDataPath);
	}

	/**
	 * @param client
	 */
	protected void foundClient(Client client) {
		//TODO intent back to app
		Log.d("MISS","Found client: "+client.getCustomName());
	}

	/**
	 * @param station
	 */
	protected void foundStation(Station station) {
		//TODO intent back to app
	}
}
