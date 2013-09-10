import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class TableData {
	private ArrayList<String> Dates = new ArrayList<String>();
	private ArrayList<String> Descriptions = new ArrayList<String>();
	private ArrayList<Double> Balances = new ArrayList<Double>();
	private ArrayList<Double> Amounts = new ArrayList<Double>();
	private Vector<String> row;
	private Vector<Vector> data = new Vector<Vector>();
	private Vector<String> columns = new Vector<String>();
	private DefaultTableModel dtm;
	private ReadWriteFile rwf;
	boolean emptyCellEdit = false;
	boolean isDeposit = false;
	boolean isWithdraw = false;
	double currentBalance = 0.0;
	int ROW = 0;
	int COL = 0;

	public TableData(ReadWriteFile rwf) {
		setColumns();
		setInitData();
		dtm = new DefaultTableModel(data, columns);
		this.rwf = rwf;
	}

	public void updateTable() {
		dtm.fireTableDataChanged();
	}

	public DefaultTableModel getDTM() {
		return dtm;
	}

	private void setColumns() {
		columns.add("Date");
		columns.add("Description");
		columns.add("Deposit");
		columns.add("Withdraw");
		columns.add("Balance");
	}

	private void setInitData() {
		data.clear();
		row = new Vector<String>();
		for (int i = 0; i < 5; ++i)
			row.add("");
		data.add(row);
	}

	private void addToArrayLists(String date, String desc, double amount) {
		Dates.add(date);
		Descriptions.add(desc);
		Amounts.add(amount);
		Balances.add(currentBalance);
	}

	private void getCurrentBalance(double amount) {
		if (currentBalance != 0.0) {
			currentBalance += amount;
		} else {
			if (!(Amounts.size() == 0 || Amounts == null))
				for (int i = 0; i < Amounts.size(); ++i)
					currentBalance += Amounts.get(i);
			else
				currentBalance += amount;
		}
	}

	private void setNewData() {
		setInitData();
		updateTable();
		for (int i = 0; i < Dates.size(); ++i) {
			addFileData(Dates.get(i), Descriptions.get(i), Amounts.get(i),
					Balances.get(i));
		}
	}

	private void addFileData(String date, String desc, double amount,
			double balance) {
		if (dtm.getValueAt(0, 0) == "") {
			data.clear();
			updateTable();
		}

		row = new Vector<String>();

		row.add(date);
		row.add(desc);
		row.add(amount > 0 ? Double.toString(amount) : "");
		row.add(amount < 0 ? Double.toString(amount) : "");
		row.add(Double.toString(balance));

		data.add(row);

		updateTable();
	}

	private void getNewCellData() {
		System.out.println("getting new data");
		String newValue = JOptionPane.showInputDialog(null,
				"Please enter a new date/description", "Edit Table",
				JOptionPane.QUESTION_MESSAGE);
		
		System.out.println("newValue is: " + newValue);
		
		if (newValue==null){
			System.out.println("newValue is empty");
			return;
		}

		if (COL == 0) {
			Dates.set(ROW, newValue);
		} else {
			Descriptions.set(ROW, newValue);
		}

		Vector<String> temp = data.get(ROW);
		temp.set(COL, newValue);
		data.set(ROW, temp);
	}

	private double getNewAmount() {
		String temp = JOptionPane.showInputDialog(null,
				"Please enter a new value", "Edit Table",
				JOptionPane.QUESTION_MESSAGE);
		if (temp == null)
			return 0.0;

		double newValue = Double.parseDouble(temp);
		return newValue;
	}

	private void findDif(double newValue) {
		if (newValue == 0.0)
			return;
		double oldValue = 0.0;
		double dif = 0.0;

		if (emptyCellEdit) {
			if (isDeposit) {
				oldValue = Amounts.get(ROW) * -1;
				dif = newValue + oldValue;
				Amounts.set(ROW, newValue);
				Vector<String> temp = data.get(ROW);
				temp.set(2, Double.toString(newValue));
				temp.set(3, "");
				applyDif(dif);
			} else if (isWithdraw) {
				oldValue = Amounts.get(ROW) * -1;
				dif = newValue + oldValue;
				Amounts.set(ROW, newValue);
				Vector<String> temp = data.get(ROW);
				temp.set(3, Double.toString(newValue));
				temp.set(2, "");
				data.set(ROW, temp);
				applyDif(dif);
			}
		} else {
			dif = (newValue > oldValue ? newValue - oldValue
					: (oldValue - newValue) * -1);
			Amounts.set(ROW, newValue);
			Vector<String> temp = data.get(ROW);
			temp.set(COL, Double.toString(newValue));
			data.set(ROW, temp);
			applyDif(dif);
		}

	}

	private void applyDif(double difference) {
		int trow = ROW;
		for (; trow < data.size(); ++trow) {
			Vector<String> temp = data.get(trow);
			Balances.set(trow, (Balances.get(trow) + difference));
			temp.set(4, Double.toString(Balances.get(trow)));
			data.set(trow, temp);
		}
	}

	private void swapData() {
		int choice = 0;
		double newValue = 0.0;

		if (COL == 2)
			isDeposit = true;
		else
			isWithdraw = true;

		String phrase = (isDeposit ? "Withdraw amount to Deposit?\n"
				: "Deposit amount to Withdraw?\n");

		choice = JOptionPane.showConfirmDialog(null,
				"You have selected an empty cell.\n"
						+ "Would you like to switch " + phrase
						+ "(Note: No option will ask for new value)",
				"Edit Option", JOptionPane.YES_NO_CANCEL_OPTION);

		if (choice == JOptionPane.YES_OPTION) {
			if (isDeposit) {
				Vector<String> temp = data.get(ROW);
				newValue = Double
						.parseDouble(dtm.getValueAt(ROW, 3).toString());
				Amounts.set(ROW, (newValue * -1));
				temp.set(3, "");
				temp.set(2, Double.toString(newValue * -1));
				data.set(ROW, temp);
				applyDif(newValue * -2);
			} else {
				Vector<String> temp = data.get(ROW);
				newValue = Double
						.parseDouble(dtm.getValueAt(ROW, 2).toString());
				Amounts.set(ROW, (newValue * -1));
				temp.set(2, "");
				temp.set(3, Double.toString(newValue * -1));
				data.set(ROW, temp);
				applyDif(newValue * -2);
			}
		} else if (choice == JOptionPane.NO_OPTION) {

			findDif(getNewAmount());
		} else
			return;
	}

	public void setSelectedCell(int row, int col) {
		ROW = row;
		COL = col;

		if (dtm.getValueAt(ROW, COL) == "" && (COL == 2 || COL == 3)) {
			emptyCellEdit = true;
			swapData();
		} else {
			if (COL == 0 || COL == 1) {
				getNewCellData();
			} else if (COL == 2 || COL == 3) {
				findDif(getNewAmount());
			}
		}
		isDeposit = false;
		isWithdraw = false;
		emptyCellEdit = false;
		updateTable();
	}

	public void loadFileData() {
		if (rwf.fileHasContents() == false) {
			Dates = rwf.getDates();
			Descriptions = rwf.getDescs();
			Amounts = rwf.getAmounts();
			Balances = rwf.getBals();

			if (Balances != null) {
				currentBalance = Balances.get(Balances.size() - 1);
				setNewData();
			}
		}
	}
	
	//adds data to table
	public void addData(String date, String desc, double amount) {
		if (dtm.getValueAt(0, 0) == "") {
			data.clear();
			updateTable();
		}
		getCurrentBalance(amount);
		addToArrayLists(date, desc, amount);
		row = new Vector<String>();

		row.add(date);
		row.add(desc);
		row.add(amount > 0 ? Double.toString(amount) : "");
		row.add(amount < 0 ? Double.toString(amount) : "");
		row.add(Double.toString(currentBalance));

		data.add(row);

		updateTable();
	}
	
	//Calls rwf.setArrayLists which calls rwf.writeFile
	public void saveData() {
		rwf.setArrayLists(Dates, Descriptions, Amounts, Balances);
	}
}
