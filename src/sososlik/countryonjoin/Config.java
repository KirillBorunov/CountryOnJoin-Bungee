package sososlik.countryonjoin;

import java.util.HashMap;

public class Config
{
	private boolean broadcastOnUnknownCountry;
	private boolean broadcastAltJoinMsgOnUnknownCountry;
	private String joinWithCountryMessage;
	private String joinWithoutCountryMessage;
	private HashMap<String, String> countryNames;
	private String messagesCulture;
	private String countrynamesCulture;
	private boolean debug;

	public Config()
	{
		this.countryNames = new HashMap<String, String>();
	}
	
	public boolean getBroadcastAltJoinMsgOnUnknownCountry() {
		return broadcastAltJoinMsgOnUnknownCountry;
	}
	public void setBroadcastAltJoinMsgOnUnknownCountry(boolean broadcastAltJoinMsgOnUnknownCountry) {
		this.broadcastAltJoinMsgOnUnknownCountry = broadcastAltJoinMsgOnUnknownCountry;
	}
	public String getJoinWithoutCountryMessage() {
		return joinWithoutCountryMessage;
	}
	public void setJoinWithoutCountryMessage(String joinWithoutCountryMessage) {
		this.joinWithoutCountryMessage = joinWithoutCountryMessage;
	}
	public boolean getDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	public String getMessagesCulture() {
		return messagesCulture;
	}
	public void setMessagesCulture(String messagesCulture) {
		this.messagesCulture = messagesCulture;
	}
	public String getCountrynamesCulture() {
		return countrynamesCulture;
	}
	public void setCountrynamesCulture(String countrynamesCulture) {
		this.countrynamesCulture = countrynamesCulture;
	}
	public boolean getBroadcastOnUnknownCountry() {
		return broadcastOnUnknownCountry;
	}
	public void setBroadcastOnUnknownCountry(boolean broadcastOnUnknownCountry) {
		this.broadcastOnUnknownCountry = broadcastOnUnknownCountry;
	}
	public String getJoinWithCountryMessage() {
		return joinWithCountryMessage;
	}
	public void setJoinWithCountryMessage(String joinWithCountryMessage) {
		this.joinWithCountryMessage = joinWithCountryMessage;
	}
	public HashMap<String, String> getCountryNames() {
		return countryNames;
	}
	public void setCountryNames(HashMap<String, String> countryNames) {
		this.countryNames = countryNames;
	}
	
}
