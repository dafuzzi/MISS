package de.uulm.miss;

import java.io.File;
import java.util.LinkedList;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * @author Fabian Schwab
 * 
 */
public class MISService extends Service {

	private LinkedList<Client> clients;
	private LinkedList<Station> stations;
	private String appDataPath;

	private static Thread serviceLogic;
	private ResultReceiver resultReceiver;

	public MISService() {
		clients = new LinkedList<Client>();
		stations = new LinkedList<Station>();
		appDataPath = "/datadata/de.uulm.miss/files/capture-01.csv";

		if (serviceLogic == null) {
			serviceLogic = new Thread(new ServiceLogic(this));
		}

		// TODO read lists from file
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent.getExtras() != null) {
			resultReceiver = intent.getParcelableExtra("receiver");

			if (intent.hasExtra("client")) {
				Client cl = (Client) intent.getExtras().get("client");
				if (cl != null && intent.getStringExtra("operation").equals("add")) {
					Log.d("MISS", "Client " + cl.getCustomName() + " added with MAC: " + cl.getMAC());
					addClient(cl);
				} else if (cl != null && intent.getStringExtra("operation").equals("remove")) {
					Log.d("MISS", "Client " + cl.getCustomName() + " removed with MAC: " + cl.getMAC());
					removeClient(cl);
				}
			} else if (intent.hasExtra("station")) {
				Station st = (Station) intent.getExtras().get("station");
				if (st != null && intent.getStringExtra("operation").equals("add")) {
					Log.d("MISS", "Station " + st.getCustomName() + " added with MAC: " + st.getMAC());
					addStation(st);
				} else if (st != null && intent.getStringExtra("operation").equals("remove")) {
					Log.d("MISS", "Station " + st.getCustomName() + " removed with MAC: " + st.getMAC());
					removeStation(st);
				}
			}
		}

		if (!clients.isEmpty() || !stations.isEmpty()) {
			Log.d("MISS", "Service started. Currently search for " + clients.size() + " client(s).");
			if (!serviceLogic.isAlive()) {
				serviceLogic.start();
			}
		} else if (clients.isEmpty() && stations.isEmpty()) {
			Log.d("MISS", "Service stopped. Currently search for " + clients.size() + " client(s).");
			if (serviceLogic.isAlive()) {
				serviceLogic.interrupt();
			}
			stopSelf();
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
		// TODO wirte lists to file
		serviceLogic.interrupt();
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
	private boolean removeStation(Station station) {
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
		Log.d("MISS", "Found client: " + client.getCustomName());
		//TODO more than one resultreceiver
		resultReceiver.send(100, null);

		//TODO copy in new app
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_stat_name).setContentTitle("MISS").setContentText("The requested person " + client.getCustomName() + " has been found!");
		Intent resultIntent = new Intent(this, MainActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		// Sets an ID for the notification
		int mNotificationId = 001;
		
		//Sound
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		mBuilder.setSound(alarmSound);
		
		// Gets an instance of the NotificationManager service
		NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(mNotificationId, mBuilder.build());
		
		removeClient(client);
	}

	/**
	 * @param station
	 */
	protected void foundStation(Station station) {
		// TODO intent back to app
	}
}
