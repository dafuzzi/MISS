package de.uulm.miss;

import java.util.LinkedList;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Fabian Schwab
 * 
 */
public class MISService extends Service {

	private LinkedList<Client> clients;
	private LinkedList<Station> stations;

	private static Thread scanner;

	public MISService() {
		clients = new LinkedList<Client>();
		stations = new LinkedList<Station>();

		// TEST DATA
		clients.add(new Client("Seba's Phone", "3C:C2:43:C9:6D:9C"));
		clients.add(new Client("Fuzzi's Phone", "90:27:E4:32:F2:03"));
		clients.add(new Client("Random Stanger 1", "00:19:07:07:C5:B0"));
		clients.add(new Client("Random Stanger 2", "00:1F:CA:CB:6B:E0"));
		
		scanner = new Thread(new AirTrafficAnalyzer(stations, clients));

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO create ServiceLogic thread and start
		Log.d("SERVICE", "Service is running");
		Toast.makeText(this, "Started MISS", Toast.LENGTH_LONG).show();
		if (!scanner.isAlive()) {
			scanner.start();
		}
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean stopService(Intent name) {
		return super.stopService(name);
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
	public boolean addClient(Client client) {
		return clients.add(client);
	}

	public boolean addStation(Station station) {
		return stations.add(station);
	}

	/**
	 * @param client
	 * @return
	 */
	public boolean removeClient(Client client) {
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
	public boolean removeSation(Station station) {
		for (Station st : stations) {
			if (st.getMAC() == station.getMAC()) {
				return stations.remove(st);
			}
		}
		return false;
	}

	/**
	 * @param client
	 * @return
	 */
	public Client getClientUpdate(Client client) {
		for (Client cl : clients) {
			if (client.getMAC() == client.getMAC()) {
				return cl;
			}
		}
		return null;
	}

	/**
	 * @param station
	 * @return
	 */
	public Station getStationUpdate(Station station) {
		for (Station st : stations) {
			if (st.getMAC() == station.getMAC()) {
				return st;
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	protected LinkedList<Station> getStations() {
		return stations;
	}

	/**
	 * @return
	 */
	protected LinkedList<Client> getClients() {
		return clients;
	}
}
