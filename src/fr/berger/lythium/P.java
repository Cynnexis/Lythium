package fr.berger.lythium;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public abstract class P {
	
	/* VARIABLES */
	
	private static boolean initialized = false;
	private static Properties appProperties = null;
	private static Properties musicProperties = null;
	
	/* CONSTRUCTOR */
	
	public static void initialize() {
		appProperties = new Properties();
		try {
			appProperties.loadFromXML(new FileInputStream(R.getApplicationPreferencesFile()));
		} catch (IOException ex) {
			saveAppPref();
		}
		
		musicProperties = new Properties();
		try {
			musicProperties.loadFromXML(new FileInputStream(R.getMusicPreferencesFile()));
		} catch (IOException ex) {
			saveMusicPref();
		}
		
		initialized = true;
	}
	
	private static void checkInitialize() {
		if (!initialized)
			initialize();
	}
	
	/* P METHODS */
	
	public static void setApp(@NotNull String key, @Nullable Object value) {
		checkInitialize();
		set(appProperties, key, value);
		saveAppPref();
	}
	
	public static void setMusic(@NotNull String key, @Nullable Object value) {
		checkInitialize();
		set(musicProperties, key, value);
		saveMusicPref();
	}
	
	private static void set(@NotNull Properties properties, @NotNull String key, @Nullable Object value) {
		properties.setProperty(key, Objects.toString(value, "null"));
	}
	
	@Nullable
	public static String getApp(@NotNull String key, @Nullable String defaultValue) {
		checkInitialize();
		return get(appProperties, key, defaultValue);
	}
	@Nullable
	public static String getApp(@NotNull String key) {
		checkInitialize();
		return get(appProperties, key);
	}
	
	@Nullable
	public static String getMusic(@NotNull String key, @Nullable String defaultValue) {
		checkInitialize();
		return get(musicProperties, key, defaultValue);
	}
	@Nullable
	public static String getMusic(@NotNull String key) {
		checkInitialize();
		return get(musicProperties, key);
	}
	
	@Nullable
	private static String get(@NotNull Properties properties, @NotNull String key, @Nullable String defaultValue) {
		String result = properties.getProperty(key);
		if (result == null)
			return defaultValue;
		else
			return result;
	}
	@Nullable
	private static String get(@NotNull Properties properties, @NotNull String key) {
		return get(properties, key, null);
	}
	
	public static void saveAppPref() {
		if (appProperties != null) {
			try {
				save(appProperties, R.getApplicationPreferencesFile());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void saveMusicPref() {
		if (musicProperties != null) {
			try {
				save(musicProperties, R.getMusicPreferencesFile());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void save() {
		saveAppPref();
		saveMusicPref();
	}
	
	private static void save(@NotNull Properties properties, @NotNull File f) {
		try {
			properties.storeToXML(new FileOutputStream(f), "");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/* GETTERS & SETTERS */
	
	@NotNull
	public static Properties getApplicationProperties() {
		checkInitialize();
		return appProperties;
	}
	
	@NotNull
	public static Properties getMusicProperties() {
		checkInitialize();
		return musicProperties;
	}
}
