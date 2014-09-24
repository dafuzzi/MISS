package de.uulm.miss;

import java.io.File;
import java.util.LinkedList;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * @author Fabian Schwab
 * 
 * This service needs active monitor mode on the wireless module. 
 * It also runs airodump-ng to scan for other wireless devices in range. 
 * 
 */
public class MISService extends Service {
	private static final String LOGTAG = "MISS";
	public static final int MSG_REGISTER_APPLICATION = 1;
	public static final int MSG_UNREGISTER_APPLICATION = 2;
	public static final int MSG_ADD_CLIENT = 3;
	public static final int MSG_ADD_STATION = 4;
	public static final int MSG_REMOVE_CLIENT = 5;
	public static final int MSG_REMOVE_STATION = 6;
	public static final int MSG_FOUND_DEVICE = 7;

	private String appDataPath;
	private LinkedList<ScanOrder> boundApplications;
	private static Thread serviceLogic;

	private final Messenger mMessenger = new Messenger(new IncomingMessageHandler(this));

	private static boolean allowOnRebind = false;

	/**
	 * This creates a new bound service. The absolute path for the airodump-ng file is set here. 
	 */
	public MISService() {
		appDataPath = "/datadata/de.uulm.miss/files/capture-01.csv";
		boundApplications = new LinkedList<ScanOrder>();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(LOGTAG, "onCreate: Service started");
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(LOGTAG, "onBind: Application bound");
		return mMessenger.getBinder();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(LOGTAG, "onUnbind: Application unbound");
		return allowOnRebind;
	}

	@Override
	public void onDestroy() {
		Log.d(LOGTAG, "onDestroy: Service stopped");
		stopLogicThread();
		super.onDestroy();
	}

	/**
	 * @author Fabian Schwab
	 * 
	 * Handles messages send form a application to this service. 
	 * The allowed 'what' constants in the message objects are
	 * <ul>
	 * <li>MSG_REGISTER_APPLICATION</li>
	 * <li>MSG_UNREGISTER_APPLICATION</li>
	 * <li>MSG_ADD_CLIENT</li>
	 * <li>MSG_REMOVE_CLIENT</li>
	 * <li>MSG_ADD_STATION</li>
	 * <li>MSG_REMOVE_STATION</li>
	 * </ul>
	 */
	private class IncomingMessageHandler extends Handler {
		MISService service;
		
		public IncomingMessageHandler(MISService service) {
			this.service = service;
		}

		@Override
		public void handleMessage(Message msg) {
			Log.d(LOGTAG, "handleMessage: " + msg.what);
			switch (msg.what) {
			case MSG_REGISTER_APPLICATION:
				addApplication(msg.replyTo);
				break;
			case MSG_UNREGISTER_APPLICATION:
				removeApplication(msg.replyTo);
				break;
			case MSG_ADD_CLIENT:
				addDevice(msg.replyTo, MSG_ADD_CLIENT, msg.getData());
				break;
			case MSG_REMOVE_CLIENT:
				removeDevice(msg.replyTo, MSG_REMOVE_CLIENT, msg.getData());
				break;
			case MSG_ADD_STATION:
				addDevice(msg.replyTo, MSG_ADD_STATION, msg.getData());
				break;
			case MSG_REMOVE_STATION:
				removeDevice(msg.replyTo, MSG_REMOVE_STATION, msg.getData());
				break;
			default:
				super.handleMessage(msg);
			}
			service.check();
		}

		/**
		 * Adds a application when it is bound to the service to add scan jobs later. 
		 * 
		 * @param replyTo
		 */
		private void addApplication(Messenger replyTo) {
			Log.d(LOGTAG, "addApplication");
			for (ScanOrder so : boundApplications) {
				if (so.getMessenger().equals(replyTo)) {
					return;
				}
			}
			boundApplications.add(new ScanOrder(replyTo));
		}

		/**
		 * Removes a application and all scan jobs linked with this application. 
		 * 
		 * @param replyTo
		 */
		protected void removeApplication(Messenger replyTo) {
			Log.d(LOGTAG, "removeApplication");
			for (ScanOrder so : boundApplications) {
				if (so.getMessenger().equals(replyTo)) {
					boundApplications.remove(so);
					return;
				}
			}
		}

		/**
		 * Adds a application and adds the device which the application is looking for. 
		 * 
		 * @param replyTo
		 * @param msgType
		 * @param name
		 * @param mac
		 */
		private void addDevice(Messenger replyTo, int msgType, Bundle data) {
			String mac, name;
			mac = (String) data.get("MAC");
			name = (String) data.get("Name");

			if (mac != null && name != null) {
				for (ScanOrder so : boundApplications) {
					if (so.getMessenger().equals(replyTo)) {
						if (msgType == MSG_ADD_CLIENT) {
							so.getClients().add(new Client(name, mac));
						} else if (msgType == MSG_ADD_STATION) {
							so.getStations().add(new Station(name, mac));
						}
						Log.d(LOGTAG, "addDevice: MAC " + mac + " Name " + name);
						return;
					}
				}
				addApplication(replyTo);
				addDevice( replyTo,  msgType,  data);
			}
		}

		/**
		 * 
		 * Removes a device and the application when the are no linked scanjobs
		 * 
		 * @param replyTo
		 * @param msgType
		 * @param data
		 */
		private void removeDevice(Messenger replyTo, int msgType, Bundle data) {
			String mac = (String) data.get("MAC");

			if (mac != null) {
				for (ScanOrder so : boundApplications) {
					if (so.getMessenger().equals(replyTo)) {
						if (msgType == MSG_REMOVE_CLIENT) {
							for (Client cl : so.getClients()) {
								if (cl.getMAC().equals(mac)) {
									so.getClients().remove(cl);
									Log.d(LOGTAG, "removeDevice: Client MAC " + mac);
									break;
								}
							}
						} else if (msgType == MSG_REMOVE_STATION) {
							for (Station st : so.getStations()) {
								if (st.getMAC().equals(mac)) {
									so.getStations().remove(st);
									Log.d(LOGTAG, "removeDevice: Station MAC " + mac);
									break;
								}
							}
						}
						if (so.getClients().size() == 0 && so.getStations().size() == 0) {
							removeApplication(replyTo);
						}
						return;
					}
				}
			}
		}
	}

	/**
	 * @return Returns the absolute path including the filename of the log file which is created by airodump-ng.
	 */
	protected File getPath() {
		return new File(appDataPath);
	}

	/**
	 * Checks if there is any device which has to be found and start or stops the thread.
	 */
	protected void check() {
		Log.d(LOGTAG,"checking...");
		if(getClients().size() == 0 && getStations().size() == 0){
			stopLogicThread();
			Log.d(LOGTAG,"thread stopped");
		}
		if(getClients().size() != 0 || getStations().size() != 0){
			startLogicThread();
			Log.d(LOGTAG,"thread started");
		}
	}

	/**
	 * @return
	 */
	protected LinkedList<Client> getClients() {
		LinkedList<Client> list = new LinkedList<Client>();
		for (ScanOrder so : boundApplications) {
			list.addAll(so.getClients());
		}
		return list;
	}

	/**
	 * @return
	 */
	protected LinkedList<Station> getStations() {
		LinkedList<Station> list = new LinkedList<Station>();
		for (ScanOrder so : boundApplications) {
			list.addAll(so.getStations());
		}
		return list;
	}

	/**
	 * Starts the thread which is scanning for devices.
	 */
	protected void startLogicThread() {
		if (serviceLogic == null) {
			serviceLogic = new Thread(new ScanLogic(this));
		}
		if (!serviceLogic.isAlive()) {
			serviceLogic.start();
		}
	}

	/**
	 * Stops the thread which is scanning for devices. 
	 */
	protected void stopLogicThread() {
		if (serviceLogic != null && serviceLogic.isAlive()) {
			serviceLogic.interrupt();
		}
	}

	/**
	 * Called form the logic thread when a client is found.
	 * 
	 * @param client
	 */
	protected void foundClient(Client client) {
		Log.d(LOGTAG, "foundClient");
		for (ScanOrder so : boundApplications) {
			for (Client cl : so.getClients()) {
				if (cl.getMAC().equals(client.getMAC())) {
					Bundle data = new Bundle();
					data.putString("MAC", cl.getMAC());
					data.putString("Name", cl.getCustomName());
					sendMessage(so.getMessenger(), data);
					return;
				}
			}
		}
	}

	/**
	 * Called form the logic thread when a station is found.
	 * 
	 * @param station
	 */
	protected void foundStation(Station station) {
		Log.d(LOGTAG, "foundStation");
		for (ScanOrder so : boundApplications) {
			for (Station st : so.getStations()) {
				if (st.getMAC().equals(station.getMAC())) {
					Bundle data = new Bundle();
					data.putString("Station", st.getMAC());
					sendMessage(so.getMessenger(), data);
					return;
				}
			}
		}
	}

	/**
	 * If an device is found this is called and sends a new message contains device informations the target application. 
	 * 
	 * @param to The target application
	 * @param data The data bundle which contains the device information. 
	 */
	private void sendMessage(Messenger to, Bundle data) {
		try {
			Message msg = Message.obtain(null, MSG_FOUND_DEVICE);
			msg.setData(data);
			to.send(msg);
			Log.d(LOGTAG, "sendMessage");
		} catch (RemoteException e) {
			// The client is dead. Remove it from the list.
			Log.d(LOGTAG, "sendMessage: Removed due exception");
			for (ScanOrder so : boundApplications) {
				if (so.getMessenger().equals(to)) {
					boundApplications.remove(so);
					return;
				}
			}
		}
	}

}
