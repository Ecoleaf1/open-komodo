package net.wigoftime.open_komodo.objects;

import java.util.UUID;

import net.wigoftime.open_komodo.config.PlayerSettingsConfig;

public class Settings {
	
	private final UUID uuidOwner;
	private CustomItem currentPhone;
	private String activeTagDisplay;
	private boolean displayTip;
	private boolean masterSounds;
	private boolean tpaEnabled;
	private boolean playerParticlesEnabled;
	private boolean isDiscordChatEnabled;
	
	public Settings(UUID uuidOwner, CustomItem currentPhone, String activeTagDisplay, 
			boolean displayTip, boolean masterSounds, boolean tpaEnabled, boolean playerParticlesEnabled, boolean isDiscordChatEnabled) {
		this.uuidOwner = uuidOwner;
		this.currentPhone = currentPhone;
		this.activeTagDisplay = activeTagDisplay;
		this.displayTip = displayTip;
		this.masterSounds = masterSounds;
		this.tpaEnabled = tpaEnabled;
		this.playerParticlesEnabled = playerParticlesEnabled;
		this.isDiscordChatEnabled = isDiscordChatEnabled;
	}
	
	public CustomItem getPhone() {
		return currentPhone;
	}
	
	public void setPhone(CustomItem phone) {
		this.currentPhone = phone;
		PlayerSettingsConfig.setSettings(this, uuidOwner);
	}
	
	public String getTagDisplay() {
		return activeTagDisplay;
	}
	
	public void setTagDisplay(String tagDisplay) {
		this.activeTagDisplay = tagDisplay;
		PlayerSettingsConfig.setSettings(this, uuidOwner);
	}
	
	public boolean isDisplayTipEnabled() {
		return displayTip;
	}
	
	public void setDisplayTip(boolean isOn) {
		this.displayTip = isOn;
		PlayerSettingsConfig.setSettings(this, uuidOwner);
	}
	
	public boolean isMasterSoundsOn() {
		return masterSounds;
	}
	
	public void setMasterSounds(boolean isOn) {
		this.masterSounds = isOn;
		PlayerSettingsConfig.setSettings(this, uuidOwner);
	}
	
	public boolean isTpaEnabled() {
		return tpaEnabled;
	}
	
	public void setPlayerParticlesEnabled(boolean isEnabled) {
		playerParticlesEnabled = isEnabled;
		PlayerSettingsConfig.setSettings(this, uuidOwner);
	}
	
	public boolean isPlayerParticlesEnabled() {
		return playerParticlesEnabled;
	}
	
	public void setTpa(boolean isOn) {
		this.tpaEnabled = isOn;
		PlayerSettingsConfig.setSettings(this, uuidOwner);
	}
	
	public void enableDiscordChat(boolean enable) {
		this.isDiscordChatEnabled = enable;
		PlayerSettingsConfig.setSettings(this, uuidOwner);
	}
	
	public boolean isDiscordChatEnabled() {
		return isDiscordChatEnabled;
	}
}