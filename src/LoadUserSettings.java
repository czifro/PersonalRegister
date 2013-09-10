import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class LoadUserSettings {
	private boolean fileIsEmpty = false;
	private String settingsPath = "";
	private String directory = "";
	private Scanner input;
	private Formatter output;
	private File file;
	private Color color = Color.LIGHT_GRAY;
	private String fileLocation;

	public LoadUserSettings(DetectOS DOS) {
		settingsPath = DOS.getSettingsPath();
		directory = DOS.getSettingsParentDirectory();
		mkDirectory();
	}

	// displays this on initial run
	private void initRun() {
		JOptionPane
				.showMessageDialog(
						null,
						"Welcome to Personal Register Version 0.3.1.\n"
								+ "Since this is the initial run of this version, it is suggested that\n" +
								"you look over the \"How_To_Use_Personal_Register\" text file.\n"
								+ "(Note: This dialog box will not appear after this run.)",
						"First Run", JOptionPane.INFORMATION_MESSAGE);
	}

	// if file exists, checks if it has contents
	public boolean fileHasContents() {
		readFile();
		return fileIsEmpty;
	}

	// if file doesn't exist, it creates the file
	private void createFile() {
		file = new File(settingsPath);
		if (!file.exists()) {
			try {
				output = new Formatter(file);
				output.format("");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// if settings parent folder doesn't exist, this method creates it
	private void mkDirectory() {
		File f = new File(directory);
		if (!f.exists()) {
			f.mkdir();
		}
		createFile();
	}

	// reads data from file if there are any
	private void readFile() {
		if (file.length()==0){
			System.out.println("settings file is empty\n");
			fileIsEmpty = true;
			initRun();
			return;
		}
		try {
			input = new Scanner(file);
			String temp = "";
			String fileVersion = "";
			int tempColor = 0;
			
			fileVersion = input.nextLine();
			if (!fileVersion.contains("file version 3.0")){
				System.out.println("settings file is an older version\n");
				fileIsEmpty = true;
				initRun();
				return;
			}
			
			fileLocation = input.nextLine();
			temp = input.nextLine();
			
			tempColor = Integer.parseInt(temp);
			color = new Color(tempColor);
			
			temp = "";
			
			System.out.println("file path before switch: " + fileLocation);
			
			String filePath = fileLocation;
			for (int i = 0; i < filePath.length(); ++i) {
				if (filePath.charAt(i) == '|')
					temp += "\\";
				else
					temp += filePath.charAt(i);
			}
			
			fileLocation = temp;
			
			System.out.println("file path after switch: " + fileLocation);

			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// writes data to file
	public void writeFile() {
		FileWriter fw;
		String filePath = fileLocation;
		String temp = "";
		int tColor = color.getRGB();
		

		for (int i = 0; i < filePath.length(); ++i) {
			if (filePath.charAt(i) == '\\')
				temp += "|";
			else
				temp += filePath.charAt(i);
		}

		try {
			fw = new FileWriter(file, false);
			output = new Formatter(fw);
			temp += "\r\n";
			output.format("file version 3.0\r\n");
			output.format(temp);
			output.format(Integer.toString(tColor));
			output.format("\r\n");
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	// sets setting for file path
	public void setFilePath(String path) {
		fileLocation = path;
	}

	// sets color to be saved
	public void setRowColor(Color color) {
		this.color = color;
	}

	// returns Settings
	public String getFilePath() {
		System.out.println("getFilePath returned: " + fileLocation);
		return fileLocation;
	}

	// returns user's row color
	public Color getRowColor() {
		System.out.println("getRowColor returned: a color");
		return color;
	}
}
