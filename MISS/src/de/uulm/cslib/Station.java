package de.uulm.cslib;

import java.io.Serializable;

import org.joda.time.DateTime;

/**
 * @author Fabian Schwab
 *
 */
public class Station implements Serializable {
	private static final long serialVersionUID = 1L;
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
	
	/**
	 * @param customName
	 * @param MAC
	 */
	public Station(String customName, String MAC){
		this.customName = customName;
		this.MAC = MAC;
	}
	/**
	 * @param customName
	 * @param MAC
	 * @param firstTimeSeen
	 * @param lastTimeSeen
	 * @param channel
	 * @param speed
	 * @param privacy
	 * @param authentication
	 * @param cipher
	 * @param power
	 * @param beacons
	 * @param iv
	 * @param ip
	 * @param idLength
	 * @param eSSID
	 */
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
	public int getChannel() {
		return channel;
	}

	/**
	 * @param channel
	 */
	public void setChannel(int channel) {
		this.channel = channel;
	}

	/**
	 * @return
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * @param speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * @return
	 */
	public String getPrivacy() {
		return privacy;
	}

	/**
	 * @param privacy
	 */
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	/**
	 * @return
	 */
	public String getAuthentication() {
		return authentication;
	}

	/**
	 * @param authentication
	 */
	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}

	/**
	 * @return
	 */
	public String getCipher() {
		return cipher;
	}

	/**
	 * @param cipher
	 */
	public void setCipher(String cipher) {
		this.cipher = cipher;
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
	public int getBeacons() {
		return beacons;
	}

	/**
	 * @param beacons
	 */
	public void setBeacons(int beacons) {
		this.beacons = beacons;
	}

	/**
	 * @return
	 */
	public int getIv() {
		return iv;
	}

	/**
	 * @param iv
	 */
	public void setIv(int iv) {
		this.iv = iv;
	}

	/**
	 * @return
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return
	 */
	public int getIdLength() {
		return idLength;
	}

	/**
	 * @param idLength
	 */
	public void setIdLength(int idLength) {
		this.idLength = idLength;
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
