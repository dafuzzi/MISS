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

	FileParser fp;
	MISService service;

	public AirTrafficAnalyzer(MISService service) {
		super();
		this.service = service;
		fp = new FileParser(service.getPath());
	}

	@Override
	public void run() {
		while (!(Thread.currentThread().isInterrupted())) {
			executer("su -c /datadata/de.uulm.miss/files/startCapture.sh");

			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			executerWithResponse("/datadata/de.uulm.miss/files/stopCapture.sh");

			if ((new File("/datadata/de.uulm.miss/files/capture-01.csv").exists())) {
				LinkedList<Client> findClients = service.getClients();
				LinkedList<Station> findStations = service.getStations();
				
				LinkedList<Client> fc = fp.parseForClients();
				LinkedList<Station> fs = fp.parseForStations();
				for (Client client : findClients) {
					for (Client found : fc) {
						if (client.getMAC().equals(found.getMAC())) {
							service.foundClient(client);
							continue;
						}
					}
				}
				for (Station station : findStations) {
					for (Station found : fs) {
						if (station.getMAC().equals(found.getMAC())) {
							service.foundStation(station);
							continue;
						}
					}
				}
				executerWithResponse("/datadata/de.uulm.miss/files/removeCaptureFiles.sh");
			}
		}
	}

	/**
	 * @param command
	 */
	private void executer(String command) {
		try {
			Runtime.getRuntime().exec(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param command
	 */
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
