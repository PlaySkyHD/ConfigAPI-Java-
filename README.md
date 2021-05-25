# ConfigAPI-Java-
ConfigAPI(JSON) for "Java-Applications"

#Requirements:
- Java 8
- Google Guava (https://mvnrepository.com/artifact/com.google.guava/guava)
- Google Gson (https://mvnrepository.com/artifact/com.google.code.gson/gson)

#Download:
- https://github.com/PlaySkyHD/ConfigAPI-Java-/blob/master/ConfigAPI.jar?raw=true

#Example:

Setting-Class
```
@Configuration(fileName = "connection.cfg", path = "config/")
public class ConntectionSettings extends AbstractConfiguration{

	@IgnoreSave
	private String serverName = "Rest-Server";
	
	private String host = "localhost";
	private String username = "root";
	private String password = "password1234";
	private int port = 22;
	
	public String getHost() {
		return host;
	}
	
	public String getPassword() {
		return password;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
}
```

Main-Method
```
	public static void main(String[] args) {
		
		final ConntectionSettings conntectionSettings = new ConntectionSettings();
		
		if(!conntectionSettings.load())
			conntectionSettings.save(); //FIRST SAVE
		
		//OR
		/*
		 if(!ConfigAPI.loadConfig(conntectionSettings))
			ConfigAPI.saveConfig(conntectionSettings); //FIRST SAVE
		 */
			
		conntectionSettings.setHost("127.0.0.1");
		conntectionSettings.setPassword("hallo12345");
		
		conntectionSettings.save();
		//OR
		ConfigAPI.saveConfig(conntectionSettings);
	}
```
