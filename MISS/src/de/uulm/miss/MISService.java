package de.uulm.miss;

import java.util.LinkedList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Fabian Schwab
 * 
 */
public class MISService extends Service implements Runnable{

	private LinkedList<Client> clients;
	private LinkedList<Station> stations;
	private LinkedList<Station> knownNetworks;
	
	private WifiManager wifi;
	private ConnectivityManager connectionManager;
	private NetworkInfo networkInfo;

	public MISService() {
		clients = new LinkedList<Client>();
		stations = new LinkedList<Station>();
		
		wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		
		connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		networkInfo = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO create ServiceLogic thread and start
		Log.d("SERVICE", "Service is running");
		Toast.makeText(this, "Started MISS", Toast.LENGTH_LONG).show();
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


	@Override
	public void run() {
		
	}
}
