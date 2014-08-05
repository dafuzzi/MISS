package de.uulm.miss;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
public class MISService extends Service {

	private LinkedList<Client> clients;
	private LinkedList<Station> stations;

	private String pathToAppData;
	private String captureFile;
	private String scriptNames[];

	Thread scanner;

	public MISService() {
		clients = new LinkedList<Client>();
		stations = new LinkedList<Station>();

		pathToAppData = this.getFilesDir().toString();
		captureFile = "capture-01.csv";
		scriptNames = new String[] { "removeCaptureFiles.sh", "startCapture.sh", "stopCapture.sh" };

		Boolean init = false;
		for (String file : scriptNames) {
			if (!(new File(pathToAppData + "/" + file).exists())) {
				init = true;
			}
		}
		if (init) {
			generateScriptsFromAssets(scriptNames);
			makeScriptsExecutable(pathToAppData, scriptNames);
		}

		//scanner = new Thread(new AirTrafficAnalyzer(stations, clients));

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
	 * @param files
	 * @return
	 */
	private boolean generateScriptsFromAssets(String files[]) {
		try {
			for (String file : files) {
				String data = "";
				InputStream inputStream = this.getAssets().open(file);
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ((receiveString = bufferedReader.readLine()) != null) {
					stringBuilder.append(receiveString + "\n");
				}

				inputStream.close();
				data = stringBuilder.toString();

				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(file, MODE_PRIVATE));
				outputStreamWriter.write(data);
				outputStreamWriter.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param path
	 * @param scriptNames
	 */
	private void makeScriptsExecutable(String path, String[] scriptNames) {
		String abs;
		for (String file : scriptNames) {
			abs = path + "/" + file;
			executer("chmod a+x " + abs);
		}

	}

	/**
	 * @param command
	 */
	private void executer(String command) {
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
