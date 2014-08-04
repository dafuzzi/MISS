package com.example.testservice;

import org.joda.time.DateTime;

public class Client {
	private String customName;
	private String MAC;
	private DateTime firstTimeSeen;
	private DateTime lastTimeSeen;
	private int power;
	private int packets;
	private String BSSID;
	private String ESSID;

	public Client(String customName, String MAC) {
		this.customName = customName;
		this.MAC = MAC;
	}

	public Client(String customName, String MAC, DateTime firstTimeSeen, DateTime lastTimeSeen, int power, int packets, String bSSID, String eSSID) {
		this.customName = customName;
		this.MAC = MAC;
		this.firstTimeSeen = firstTimeSeen;
		this.lastTimeSeen = lastTimeSeen;
		this.power = power;
		this.packets = packets;
		BSSID = bSSID;
		ESSID = eSSID;
	}
	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	public String getMAC() {
		return MAC;
	}

	public void setMAC(String MAC) {
		this.MAC = MAC;
	}

	public DateTime getFirstTimeSeen() {
		return firstTimeSeen;
	}

	public void setFirstTimeSeen(DateTime firstTimeSeen) {
		this.firstTimeSeen = firstTimeSeen;
	}

	public DateTime getLastTimeSeen() {
		return lastTimeSeen;
	}

	public void setLastTimeSeen(DateTime lastTimeSeen) {
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

	public String getESSID() {
		return ESSID;
	}

	public void setESSID(String eSSID) {
		ESSID = eSSID;
	}

}
