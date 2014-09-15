package de.uulm.miss;

import java.io.Serializable;

import org.joda.time.DateTime;

/**
 * @author Fabian Schwab
 * 
 * Class which contains all data which can be gathered from airodump-ng.
 *
 */
public class Client implements Serializable {
	private static final long serialVersionUID = 1L;
	private String customName;
	private String MAC;
	private DateTime firstTimeSeen;
	private DateTime lastTimeSeen;
	private int power;
	private int packets;
	private String BSSID;
	private String ESSID;

	/**
	 * 
	 * Returns a new object, instantiated with MAC and customName
	 * 
	 * @param customName A user defined name.
	 * @param MAC The MAC address must have the following format <i>XX:XX:XX:XX:XX:XX</i>. Letters must be upper case. 
	 */
	public Client(String customName, String MAC) {
		this.customName = customName;
		this.MAC = MAC;
	}

	/**
	 * 
	 * Returns a new object with all data airodump-ng provides.
	 * 
	 * @param customName A user defined name.
	 * @param MAC The MAC address must have the following format <i>XX:XX:XX:XX:XX:XX</i>. Letters must be upper case.
	 * @param firstTimeSeen When the client was detected for the first time.
	 * @param lastTimeSeen When the client was detected for the last time.
	 * @param power Signal strength in decibel.
	 * @param packets Number of received packets.
	 * @param bSSID SSID the client is connected to.
	 * @param eSSID Probed SSID which are known by the client himself.
	 */
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
	
	/**
	 * @return
	 */
	public String getCustomName() {
		return customName;
	}

	/**
	 * @param customName
	 */
	public void setCustomName(String customName) {
		this.customName = customName;
	}

	/**
	 * @return 
	 */
	public String getMAC() {
		return MAC;
	}

	/**
	 * @param MAC 
	 */
	public void setMAC(String MAC) {
		this.MAC = MAC;
	}

	/**
	 * @return 
	 */
	public DateTime getFirstTimeSeen() {
		return firstTimeSeen;
	}

	/**
	 * @param firstTimeSeen 
	 */
	public void setFirstTimeSeen(DateTime firstTimeSeen) {
		this.firstTimeSeen = firstTimeSeen;
	}

	/**
	 * @return 
	 */
	public DateTime getLastTimeSeen() {
		return lastTimeSeen;
	}

	/**
	 * @param lastTimeSeen
	 */
	public void setLastTimeSeen(DateTime lastTimeSeen) {
		this.lastTimeSeen = lastTimeSeen;
	}

	/**
	 * @return
	 */
	public int getPower() {
		return power;
	}

	/**
	 * @param power
	 */
	public void setPower(int power) {
		this.power = power;
	}

	/**
	 * @return
	 */
	public int getPackets() {
		return packets;
	}

	/**
	 * @param packets
	 */
	public void setPackets(int packets) {
		this.packets = packets;
	}

	/**
	 * @return
	 */
	public String getBSSID() {
		return BSSID;
	}

	/**
	 * @param bSSID
	 */
	public void setBSSID(String bSSID) {
		BSSID = bSSID;
	}

	/**
	 * @return
	 */
	public String getESSID() {
		return ESSID;
	}

	/**
	 * @param eSSID
	 */
	public void setESSID(String eSSID) {
		ESSID = eSSID;
	}

}
