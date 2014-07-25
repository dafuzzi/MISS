package com.example.testservice;

import java.util.LinkedList;

public class Client {
	private String MAC;
	private String firstTimeSeen; //TODO jodatime
	private String lastTimeSeen;	//TODO jodatime
	private int power;
	private int packets;
	private String BSSID;
	private LinkedList<String> ESSID;
	
	public Client(String mAC, String firstTimeSeen, String lastTimeSeen,
			int power, int packets, String bSSID, LinkedList<String> eSSID) {
		super();
		MAC = mAC;
		this.firstTimeSeen = firstTimeSeen;
		this.lastTimeSeen = lastTimeSeen;
		this.power = power;
		this.packets = packets;
		BSSID = bSSID;
		ESSID = eSSID;
	}

	public String getMAC() {
		return MAC;
	}

	public void setMAC(String mAC) {
		MAC = mAC;
	}

	public String getFirstTimeSeen() {
		return firstTimeSeen;
	}

	public void setFirstTimeSeen(String firstTimeSeen) {
		this.firstTimeSeen = firstTimeSeen;
	}

	public String getLastTimeSeen() {
		return lastTimeSeen;
	}

	public void setLastTimeSeen(String lastTimeSeen) {
		this.lastTimeSeen = lastTimeSeen;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getPackets() {
		return packets;
	}

	public void setPackets(int packets) {
		this.packets = packets;
	}

	public String getBSSID() {
		return BSSID;
	}

	public void setBSSID(String bSSID) {
		BSSID = bSSID;
	}

	public LinkedList<String> getESSID() {
		return ESSID;
	}

	public void setESSID(LinkedList<String> eSSID) {
		ESSID = eSSID;
	}
	
	
}
