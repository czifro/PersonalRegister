
public class DetectOS {
	private String os = System.getProperty("os.name");
	private String USERNAME = System.getProperty("user.name");
	private String defaultFCPath = "";
	private String settingsDirectory = "";
	private String settingsPath = "";
	
	//Constructor, calls setPaths()
	public DetectOS() {
		setPaths();
	}
	
	//Depending on OS, it sets the paths to paths that work with OS
	private void setPaths(){
		if (os.contains("Windows")){
			defaultFCPath = "C:\\Users\\" + USERNAME + "\\";
			settingsDirectory = "C:\\Users\\" + USERNAME + "\\Documents\\Personal_Register";
			settingsPath = "C:\\Users\\" + USERNAME + "\\Documents\\Personal_Register\\settings.txt";
		} else if (os.contains("Linux")){
			defaultFCPath = "/home/" + USERNAME + "/";
			settingsDirectory = "/home/" + USERNAME + "/Documents/Personal_Register";
			settingsPath = "/home/" + USERNAME + "/Documents/Personal_Register/settings.txt";
		} else if (os.contains("Mac")){
			defaultFCPath = "~/";
			settingsDirectory = "~/Documents/Personal_Register";
			settingsPath = "~/Documents/Personal_Register/settings.txt";
		}
	}
	
	//returns the directory the fileChooser will start in
	public String getDefaultFCPath() {
		return defaultFCPath;
	}
	
	//LoadUserSettings.mkDirectory() will call this to check if dir exists
	public String getSettingsParentDirectory() {
		return settingsDirectory;
	}
	
	//returns the path to the settings file
	public String getSettingsPath() {
		return settingsPath;
	}
}










