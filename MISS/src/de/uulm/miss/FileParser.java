package de.uulm.miss;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author Fabian Schwab
 * 
 * Parses a text file, generated form airodump-ng and converts the log into client and station objects.
 *
 */
public class FileParser {
	private static final String STATION = "BSSID, First time seen, Last time seen, channel, Speed, Privacy, Cipher, Authentication, Power, # beacons, # IV, LAN IP, ID-length, ESSID, Key";
	private static final String CLIENT = "Station MAC, First time seen, Last time seen, Power, # packets, BSSID, Probed ESSIDs";
	private FileInputStream fis;
	private BufferedReader br;
	private DataInputStream dis;
	private String line;
	private String clientParts[];
	private String stationParts[];
	private File file;
	private LinkedList<Station> foundStations;
	private LinkedList<Client> foundClients;
	private Boolean doParse;
	private DateTimeFormatter formatter;

	/**
	 * @param filePath Absolute location where the airodump-ng log file is located.
	 */
	public FileParser(File filePath) {
		super();
		doParse = false;
		formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		file = filePath;
	}

	/**
	 * @return Returns a LinkedList of all found Stations
	 */
	public LinkedList<Station> parseForStations() {
		foundStations = new LinkedList<Station>();
		stationParts = new String[14];
		doParse = false;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		dis = new DataInputStream(fis);
		br = new BufferedReader(new InputStreamReader(dis));
		try {
			while ((line = br.readLine()) != null) {
				if (line.contains(STATION)) {
					doParse = true;
					continue;
				}else if (line.contains(CLIENT)) {
					doParse = false;
					break;
				}
				if (doParse) {
					stationParts = line.split("[ ]*,[ ]*", 15);
					if (stationParts.length == 15) {
						foundStations.add(new Station("unnamed",stationParts[0], formatter.parseDateTime(stationParts[1]), formatter.parseDateTime(stationParts[2]), Integer
								.parseInt(stationParts[3]), Integer.parseInt(stationParts[4]), stationParts[5], stationParts[6], stationParts[7],
								Integer.parseInt(stationParts[8]), Integer.parseInt(stationParts[9]), Integer.parseInt(stationParts[10]), stationParts[11], Integer
										.parseInt(stationParts[12]), stationParts[13]));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return foundStations;
	}

	/**
	 * @return Returns a LinkedList of all found Clients
	 */
	public LinkedList<Client> parseForClients() {
		foundClients = new LinkedList<Client>();
		clientParts = new String[7];
		doParse = false;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		dis = new DataInputStream(fis);
		br = new BufferedReader(new InputStreamReader(dis));
		try {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains(CLIENT)) {
					doParse = true;
					continue;
				}
				if (doParse) {
					clientParts = line.split("[ ]*,[ ]*", 7);
					if (clientParts.length == 7) {
						foundClients.add(new Client("unnamed",clientParts[0], formatter.parseDateTime(clientParts[1]), formatter.parseDateTime(clientParts[2]), Integer
								.parseInt(clientParts[3]), Integer.parseInt(clientParts[4]), clientParts[5], clientParts[6]));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return foundClients;
	}
}
