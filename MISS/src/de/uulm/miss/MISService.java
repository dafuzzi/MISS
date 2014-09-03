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
	 * 
	 */
	public MISService() {
		appDataPath = "/datadata/de.uulm.miss/files/capture-01.csv";
		boundApplications = new LinkedList<ScanOrder>();

		if (serviceLogic == null) {
			serviceLogic = new Thread(new ServiceLogic(this));
		}
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
		 * @param replyTo
		 * @param msgType
		 * @param name
		 * @param mac
		 */
		private void addDevice(Messenger replyTo, int msgType, Bundle data) {
			String mac, name;
			mac = (String) data.get("MAC");
			name = (String) data.get("Name");

			Log.d(LOGTAG, "addDevice: MAC " + mac + " Name " + name);

			if (mac != null && name != null) {
				for (ScanOrder so : boundApplications) {
					if (so.getMessenger().equals(replyTo)) {
						if (msgType == MSG_ADD_CLIENT) {
							so.getClients().add(new Client(name, mac));
						} else if (msgType == MSG_ADD_STATION) {
							so.getStations().add(new Station(name, mac));
						}
						return;
					}
				}
			}
		}

		/**
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
	 * @return
	 */
	protected File getPath() {
		return new File(appDataPath);
	}

	public void check() {
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
	 * 
	 */
	protected void startLogicThread() {
		if (!serviceLogic.isAlive()) {
			serviceLogic.start();
		}
	}

	/**
	 * 
	 */
	protected void stopLogicThread() {
		if (serviceLogic.isAlive()) {
			serviceLogic.interrupt();
		}
	}

	/**
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
	 * @param to
	 * @param data
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
