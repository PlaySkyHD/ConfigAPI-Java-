package me.playskyhd.configapi.config;

import java.util.Map;

import com.google.common.collect.Maps;

import me.playskyhd.configapi.ConfigAPI;

public abstract class AbstractConfiguration {

	private Map<String, Object> loadedDate = Maps.newHashMap();
	
	public final boolean save() {
		return ConfigAPI.saveConfig(this);
	}
	
	public final boolean load() {
		return ConfigAPI.loadConfig(this);
	}
	
	public final void setLoadedDate(final Map<String, Object> loadedDate) {
		this.loadedDate = loadedDate;
	}
	
	public final Map<String, Object> getLoadedDate() {
		return this.loadedDate;
	}
}
