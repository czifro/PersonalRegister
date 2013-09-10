import javax.swing.DefaultComboBoxModel;

public class ComboBoxData {
	private String[] shortMonth = new String[29];
	private String[] midMonth = new String[31];
	private String[] longMonth = new String[32];
	private DefaultComboBoxModel Days;
	private DefaultComboBoxModel Months;

	public ComboBoxData() {
		setMonths();
		setDaysData();
	}

	private void setMonths() {
		String[] monthsData = new String[13];
		monthsData[0] = "---";
		for (int i = 1; i < 13; ++i) {
			monthsData[i] = Integer.toString(i);
		}
		Months = new DefaultComboBoxModel(monthsData);
	}

	private void setDaysData() {
		shortMonth[0] = "---";
		midMonth[0] = "---";
		longMonth[0] = "---";

		for (int i = 1; i < 29; ++i)
			shortMonth[i] = Integer.toString(i);
		for (int i = 1; i < 31; ++i)
			midMonth[i] = Integer.toString(i);
		for (int i = 1; i < 32; ++i)
			longMonth[i] = Integer.toString(i);
	}

	public DefaultComboBoxModel getMonths() {
		return Months;
	}

	public DefaultComboBoxModel getDays(int month) {
		if (month == 2) 
			Days = new DefaultComboBoxModel(shortMonth);
		else if (month == 4 || month == 6 || month == 9 || month == 11)
			Days = new DefaultComboBoxModel(midMonth);
		else
			Days = new DefaultComboBoxModel(longMonth);
		return Days;
	}
	
	public DefaultComboBoxModel getDefaultData(){
		Days = new DefaultComboBoxModel(new String[] {"---"});
		return Days;
	}
}
