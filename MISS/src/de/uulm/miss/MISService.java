package de.uulm.miss;

import java.io.File;
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
	private File appDataPath;
	private String captureFile;

	private static Thread scanner;

	public MISService() {
		clients = new LinkedList<Client>();
		stations = new LinkedList<Station>();
		appDataPath = this.getFilesDir();
		captureFile = "capture-01.csv";

		if (scanner == null) {
			scanner = new Thread(new AirTrafficAnalyzer(this));
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Started MISS", Toast.LENGTH_LONG).show();
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
		return new File(appDataPath.toString() + "/" + captureFile);
	}

	/**
	 * @param client
	 */
	protected void foundClient(Client client) {
		//TODO intent back to app
	}

	/**
	 * @param station
	 */
	protected void foundStation(Station station) {
		//TODO intent back to app
	}
}
