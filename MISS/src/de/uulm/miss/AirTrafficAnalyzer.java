package de.uulm.miss;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.LinkedList;
import android.util.Log;

/**
 * @author Fabian Schwab
 *
 */
public class AirTrafficAnalyzer implements Runnable {

	LinkedList<Station> findStations;
	LinkedList<Client> findClients;
	FileParser fp;

	public AirTrafficAnalyzer(LinkedList<Station> findStations, LinkedList<Client> findClients) {
		super();
		this.findStations = findStations;
		this.findClients = findClients;
		fp = new FileParser(new File("datadata/de.uulm.miss/files/capture-01.csv"));
	}

	@Override
	public void run() {
		while (true) {
			executerWithResponse("/datadata/de.uulm.miss/files/removeCaptureFiles.sh");
			executer("su -c /datadata/de.uulm.miss/files/startCapture.sh");
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			executerWithResponse("su -c /datadata/de.uulm.miss/files/stopCapture.sh");

			LinkedList<Client> fc = fp.parseForClients();
			LinkedList<Station> fs = fp.parseForStations();

			for (Client client : findClients) {
				for (Client found : fc) {
					if (client.getMAC().equals(found.getMAC())) {
						Log.d("Parser", "Found: " + client.getCustomName());
						// TODO intent an MISSservice
						continue;
					}
				}
			}

			for (Station station : findStations) {
				for (Station found : fs) {
					if (station.getMAC().equals(found.getMAC())) {
						Log.d("Parser", "Found: " + station.getESSID());
						// TODO intent an MISSservice
						continue;
					}
				}
			}
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
	private void executerWithResponse(String command) {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String response = output.toString();
		Log.d("AirTrafficAnalyzer", response);
	}
}
