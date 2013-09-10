//import utilities
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Formatter;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
//import i/o packages
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JDialog;
//import file chooser GUI
import javax.swing.JFileChooser;
//import extras
import javax.swing.JOptionPane;

public class ReadWriteFile {
	File file;
	DetectOS DOS;
	LoadUserSettings lus;
	Scanner input;
	Main frame;
	private String filePath = "";
	private String startDir = "";
	private ArrayList<String> Dates = new ArrayList<String>();
	private ArrayList<String> Descs = new ArrayList<String>();
	private ArrayList<Double> Amounts = new ArrayList<Double>();
	private ArrayList<Double> Bals = new ArrayList<Double>();
	private boolean fileIsEmpty = false;
	private boolean writingData = false;

	public ArrayList<String> getDates() {
		return Dates;
	}

	public ArrayList<String> getDescs() {
		return Descs;
	}

	public ArrayList<Double> getAmounts() {
		return Amounts;
	}

	public ArrayList<Double> getBals() {
		return Bals;
	}
	
	public boolean getWritingData(){
		return writingData;
	}

	// Constructor, takes var of DetectOS type as argument
	public ReadWriteFile(DetectOS DOS, LoadUserSettings lus, Main frame) {
		this.DOS = DOS;
		this.lus = lus;
		this.frame = frame;
		openFile();
	}

	// checks if file is empty, returns bool val
	public boolean fileHasContents() {
		readFile();
		return fileIsEmpty;
	}

	// opens file
	private void openFile() {
		if (lus.fileHasContents() == false) {
			filePath = lus.getFilePath();
			file = new File(filePath);
		} else {
			startDir = DOS.getDefaultFCPath();
			findFile();
		}
	}

	// opens FileChooser
	public void findFile() {
		if (startDir.equals(""))
			startDir = DOS.getDefaultFCPath();
		JFileChooser chooser = new JFileChooser(){
			@Override
			protected JDialog createDialog(Component parent) throws HeadlessException {
				JDialog dialog = super.createDialog(parent);
				dialog.setIconImage(Toolkit
						.getDefaultToolkit()
						.getImage(
								"E:\\Programming\\eclipse_workspace\\MyProjects\\MoneyRegisterBetaII\\persregicon.png"));
				return dialog;
			}
		};
		chooser.setDialogTitle("Create/Open");
		System.out.println("getDisplayName(): " + chooser.getLocale().getDisplayName());
		chooser.setCurrentDirectory(new File(startDir));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int response = chooser.showOpenDialog(frame);
		if (response == JFileChooser.CANCEL_OPTION)
			System.exit(0);

		file = chooser.getSelectedFile();
		System.out.println("File selected is: " + file.getName());

		if (!file.exists()) {
			try {
				Formatter output = new Formatter(file);
				output.format("");
				output.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			lus.setFilePath(file.getAbsolutePath());
			return;
		}
		lus.setFilePath(file.getAbsolutePath());
		System.out.println("file exists");
	}

	// set ArrayLists
	public void setArrayLists(ArrayList<String> Dates, ArrayList<String> Desc,
			ArrayList<Double> Amounts, ArrayList<Double> Balances) {
		this.Dates = Dates;
		Descs = Desc;
		this.Amounts = Amounts;
		Bals = Balances;

		writeFile();
	}
	
	//Reads data from local file
	private void readFile() {
		Dates = new ArrayList<String>();
		Descs = new ArrayList<String>();
		Amounts = new ArrayList<Double>();
		Bals = new ArrayList<Double>();
		String tempDate = "";
		String tempDesc = "";
		double tempAmount = 0.0;
		double tempBal = 0.0;

		System.out.println("File length is: " + file.length());
		if (file.length()==0) {
			fileIsEmpty = true;
			return;
		}
		try {
			input = new Scanner(file);

			System.out.println("reading data from file");
			while (input.hasNext()) {
				tempDate = input.next();
				System.out.println("tempDate: " + tempDate);
				tempDesc = input.next();
				System.out.println("tempDesc: " + tempDesc);
				tempAmount = input.nextDouble();
				System.out.println("tempAmount: " + tempAmount);
				tempBal = input.nextDouble();
				System.out.println("tempBal: " + tempBal);
				Dates.add(tempDate);
				Descs.add(tempDesc);
				Amounts.add(tempAmount);
				Bals.add(tempBal);
			}

			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// saves content to selected file
	public void writeFile() {
		FileWriter fw;
		writingData = true;
		try {
			fw = new FileWriter(file, false);
			Formatter output = new Formatter(fw);
			for (int i = 0; i < Dates.size(); ++i) {
				output.format("%s\r\t%s\r\t%.2f\r\t%.2f\r\n", Dates.get(i),
						Descs.get(i), Amounts.get(i), Bals.get(i));
			}

			JOptionPane.showMessageDialog(null,
					"You info has successfully been saved.", "Progress Saved",
					JOptionPane.INFORMATION_MESSAGE);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		writingData = false;
	}
}
