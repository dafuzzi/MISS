package com.example.testservice;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import android.os.Environment;
import android.util.Log;

public class FileParser {
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

	public FileParser(File filePath) {
		super();
		doParse = false;
		clientParts = new String[7];
		stationParts = new String[14];
		foundStations = new LinkedList<Station>();
		foundClients = new LinkedList<Client>();
		try {
			file = filePath;
			fis = new FileInputStream(file);
			dis = new DataInputStream(fis);
			br = new BufferedReader(new InputStreamReader(dis));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public LinkedList<Station> parseForStations() {
		foundStations.clear();
		try {
			while (br.readLine() != null) {
				line = br.readLine();
				if(line.contains("BSSID")){
					doParse = true;
					continue;
				}else if(line.contains("Station Mac")){
					doParse = false;
					break;
				}
				if(doParse){
					stationParts = line.split(" *, *", 14);
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
