package me.playskyhd.configapi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.Getter;
import me.playskyhd.configapi.config.AbstractConfiguration;
import me.playskyhd.configapi.config.annotation.Configuration;
import me.playskyhd.configapi.config.annotation.IgnoreSave;

@Getter
public final class ConfigAPI {

	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public static final boolean loadConfig(final AbstractConfiguration abstractConfiguration) {
		final Configuration config = abstractConfiguration.getClass().getAnnotation(Configuration.class);
		if(config == null)return false;
		
		File file;
		if(config.path() == null || config.path().isEmpty()) {
			file = new File(config.fileName());
		}else
			file = new File(config.path() + (config.path().endsWith("/") || config.path().endsWith("//") || config.path().endsWith("\\") ? config.fileName() : (config.path().isEmpty() ? config.fileName() : "/" + config.fileName())));
		
		
		if(!file.exists())return false;
		try {
			String jsonString = new String(Files.readAllBytes(file.toPath()));
			
			@SuppressWarnings("unchecked")
			final HashMap<String, Object> dataMap = gson.fromJson(jsonString, HashMap.class);
			abstractConfiguration.setLoadedDate(dataMap);
			
			for(Field field : getFields(abstractConfiguration.getClass())) {
				field.setAccessible(true);
				if(field.getDeclaredAnnotation(IgnoreSave.class) != null)continue;
				try {
					if(dataMap.containsKey(field.getName())) {
						field.set(abstractConfiguration, dataMap.get(field.getName()));
					}
				} catch (Exception e) {
					System.out.println("[ConfigAPI] Can not set the value of the field " + field.getName() + "! You can get the value over the LoadedData-Map!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static final boolean saveConfig(final AbstractConfiguration abstractConfiguration) {
		final Configuration config = abstractConfiguration.getClass().getAnnotation(Configuration.class);
		if(config == null)return false;
		
		final Map<String, Object> map = Maps.newHashMap();
		
		for(Field field : getFields(abstractConfiguration.getClass())) {
			field.setAccessible(true);
			if(field.getDeclaredAnnotation(IgnoreSave.class) != null)continue;
			try {
				map.put(field.getName(), field.get(abstractConfiguration));
			} catch (Exception e) {}
		}
		
		final String jsonConfiguration = gson.toJson(map);
		File file;
		if(config.path() == null || config.path().isEmpty()) {
			file = new File(config.fileName());
		}else
			file = new File(config.path() + (config.path().endsWith("/") || config.path().endsWith("//") || config.path().endsWith("\\") ? config.fileName() : (config.path().isEmpty() ? config.fileName() : "/" + config.fileName())));
		
		if(file.getParentFile() != null && !file.getParentFile().exists())
			file.getParentFile().mkdirs();

		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try(FileWriter writer = new FileWriter(file)){
			writer.write(jsonConfiguration);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	private final static List<Field> getFields(final Class<? extends AbstractConfiguration> clazz){
		final List<Field> list = Lists.newArrayList(clazz.getDeclaredFields());
		list.addAll(Arrays.asList(clazz.getFields()));
		return list;
	}
	
}
