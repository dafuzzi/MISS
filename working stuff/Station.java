package com.example.testservice;

import org.joda.time.DateTime;


public class Station {
	private String customName;
	private String MAC;
	private DateTime firstTimeSeen; 
	private DateTime lastTimeSeen;
	private int channel;
	private int speed;
	private String privacy;
	private String authentication;
	private String cipher;
	private int power;
	private int beacons;
	private int iv;
	private String ip;
	private int idLength;
	private String ESSID;
	
	public Station(String customName, String MAC){
		this.customName = customName;
		this.MAC = MAC;
	}
	public Station(String customName, String MAC, DateTime firstTimeSeen, DateTime lastTimeSeen,
			int channel, int speed, String privacy, String authentication,
			String cipher, int power, int beacons, int iv, String ip,
			int idLength, String eSSID) {
		this.customName = customName;
		this.MAC = MAC;
		this.firstTimeSeen = firstTimeSeen;
		this.lastTimeSeen = lastTimeSeen;
		this.channel = channel;
		this.speed = speed;
		this.privacy = privacy;
		this.authentication = authentication;
		this.cipher = cipher;
		this.power = power;
		this.beacons = beacons;
		this.iv = iv;
		this.ip = ip;
		this.idLength = idLength;
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

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public String getPrivacy() {
		return privacy;
	}

	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	public String getAuthentication() {
		return authentication;
	}

	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}

	public String getCipher() {
		return cipher;
	}

	public void setCipher(String cipher) {
		this.cipher = cipher;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getBeacons() {
		return beacons;
	}

	public void setBeacons(int beacons) {
		this.beacons = beacons;
	}

	public int getIv() {
		return iv;
	}

	public void setIv(int iv) {
		this.iv = iv;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getIdLength() {
		return idLength;
	}

	public void setIdLength(int idLength) {
		this.idLength = idLength;
	}

	public String getESSID() {
		return ESSID;
	}

	public void setESSID(String eSSID) {
		ESSID = eSSID;
	}
}
