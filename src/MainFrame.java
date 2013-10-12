import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.GroupLayout;
import javax.swing.UIManager;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.util.Date;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import javax.swing.JCheckBoxMenuItem;
import java.awt.Toolkit;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

public class MainFrame extends JFrame {

	static MainFrame frame;
	private DetectOS dos;
	private LoadUserSettings lus;
	private ReadWriteFile rwf;
	private TableData td;
	private ComboBoxData cbData;
	private TableColor tc;
	private JPanel contentPane;
	JTable table;
	private double amount = 0.0;
	private String date = "";
	private String desc = "";
	int ROW = 0;
	int COL = 0;
	int delay = 3000;
	boolean autoSave = true;
	boolean enteringData = false;
	private boolean EditMode = false;
	private JTextField txtDescription;
	private JTextField txtAmount;
	java.net.URL url = MainFrame.class.getResource("persregicon.png");
	ImageIcon icon = new ImageIcon(url);

	/**
	 * Launch the application.
	 */

	// updates
	public void saveChanges() {
		lus.setRowColor(tc.getColor());
		lus.writeFile();
	}

	// get current date
	private void setDate() {
		int month = 0;
		int day = 0;
		int year = 0;
		Date temp = new Date();
		month = temp.getMonth() + 1;
		day = temp.getDate();
		year = temp.getYear() + 1900;
		date = Integer.toString(month) + "/" + Integer.toString(day) + "/"
				+ Integer.toString(year);
	}

	// opens new file
	private void getNewData() {
		if (rwf.fileHasContents() == false) {
			td.loadFileData();
		}
	}

	// retrieves stored data
	private void getTableData() {

		if (rwf.fileHasContents() == false) {
			td.loadFileData();
		}
	}

	// resets variables
	private void resetVariables() {
		txtDescription.setText("Description");
		txtAmount.setText("Amount");
		amount = 0.0;
		date = "";
		desc = "";
	}

	// retrieves inputed text
	public void getInputtedText() {
		if (amount == 0)
			amount = (txtAmount.getText() == "Amount" ? 0 : Double
					.parseDouble(txtAmount.getText()));
		if (date == "")
			setDate();
		if (desc == "")
			desc = txtDescription.getText();
	}

	// Checks that user inputed values in all fields
	private boolean checkInputFields() {
		if (amount == 0 || desc == "" || date == "") {
			getInputtedText();
			if (amount == 0 || desc == "Description" || date == "Date") {
				JOptionPane
						.showMessageDialog(
								null,
								"One or more fields were left blank."
										+ "\nPlease check that all fields have values inputted into them.",
								"Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			return true;
		}
		return true;
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						"E:\\Programming\\eclipse_workspace\\MyProjects\\MoneyRegisterBetaII\\persregicon.png"));
		setTitle("Personal Register (Beta 3.1)");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);
		setLocationByPlatform(true);
		setLocation(10, 10);

		dos = new DetectOS();
		lus = new LoadUserSettings(dos);
		rwf = new ReadWriteFile(dos, lus, frame);
		td = new TableData(rwf);
		cbData = new ComboBoxData();
		tc = new TableColor();

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rwf.findFile();
				if (rwf.fileHasContents() == false) {
					getNewData();
					lus.writeFile();
				}
			}
		});
		mnFile.add(mntmOpen);

		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				td.saveData();
			}
		});
		mnFile.add(mntmSave);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int option;
				option = JOptionPane
						.showConfirmDialog(null,
								"Would you like to end your session?", "Exit",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.YES_OPTION) {
					while (rwf.getWritingData())
						;
					System.exit(0);
				}
			}
		});
		mnFile.add(mntmExit);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		// Toggles Edit Mode
		final JCheckBoxMenuItem EditToggle = new JCheckBoxMenuItem("Edit Mode");
		EditToggle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (EditToggle.isSelected()) {
					EditMode = true;
				} else
					EditMode = false;
			}
		});
		mnEdit.add(EditToggle);

		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);

		JMenuItem Properties = new JMenuItem("Table Color");
		mnView.add(Properties);
		Properties.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SettingsScreen sScreen = new SettingsScreen(tc, table, frame);
				//sScreen.setVisible(true);
				sScreen.runWindow();

			}
		});

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAboutPersonalRegister = new JMenuItem(
				"About Personal Register");
		mntmAboutPersonalRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane
						.showMessageDialog(
								null,
								"Personal Register\n\n"
										+ "Version: 0.3.1\n\n"
										+ "This Program includes software developed by\n"
										+ "EJ Technologies http://www.ej-technologies.com/index.html\n",
								"About Personal Register",
								JOptionPane.INFORMATION_MESSAGE, icon);
			}
		});
		mnHelp.add(mntmAboutPersonalRegister);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		final JPanel panel = new JPanel();
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("clicked panel");
				if (txtDescription.getText().equals(""))
					txtDescription.setText("Description");
				if (txtAmount.getText().equals(""))
					txtAmount.setText("Amount");
			}
		});

		panel.setBorder(null);
		contentPane.add(panel, BorderLayout.CENTER);

		txtDescription = new JTextField();
		txtDescription.setText("Description");
		txtDescription.setColumns(10);

		txtAmount = new JTextField();
		txtAmount.setText("Amount");
		txtAmount.setColumns(10);

		getTableData();

		tc.setColor(lus.getRowColor());

		// Creates new table and overrides isCellEditable and prepareRenderer
		table = new JTable(td.getDTM()) {
			// toggles cell edit ability
			@Override
			public boolean isCellEditable(int row, int column) {
				if (EditMode)
					return true;
				else if (COL == 4)
					return false;
				else
					return false;
			}

			@Override
			public Component prepareRenderer(TableCellRenderer r, int row,
					int column) {
				Component c = super.prepareRenderer(r, row, column);

				int trow = table.convertRowIndexToModel(row);

				if (trow % 2 == 0)
					c.setBackground(Color.WHITE);
				else {
					c.setBackground(tc.getColor());
				}

				return c;
			}
		};

		saveChanges();

		// Action listener checks if a cell has been clicked,
		// performs updateTable() if cell can be edited
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//
				ROW = table.getSelectedRow();
				COL = table.getSelectedColumn();

				if (table.isCellSelected(ROW, COL)) {
					if (COL != 4) {
						if (table.isCellEditable(ROW, COL)) {
							td.setSelectedCell(ROW, COL);
						} else {
							JOptionPane.showMessageDialog(null,
									"Editing Mode has not been enabled\n"
											+ "To enable: Edit -> Edit Mode\n",
									"Access Denied",
									JOptionPane.WARNING_MESSAGE);
						}
					} else
						JOptionPane.showMessageDialog(null,
								"Balance values cannot be edited!",
								"Access Denied", JOptionPane.WARNING_MESSAGE);
					table.clearSelection();
				}

			}
		});
		table.setPreferredScrollableViewportSize(getSize());
		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportView(table);

		JLabel lblRecentTransactions = new JLabel("Recent Transactions");
		lblRecentTransactions.setFont(new Font("Tahoma", Font.PLAIN, 14));

		final JComboBox Month = new JComboBox(cbData.getMonths());

		final JComboBox Day = new JComboBox(cbData.getDefaultData());

		Month.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Month.getSelectedIndex() != 0)
					Day.setModel(cbData.getDays(Integer.parseInt(Month
							.getSelectedItem().toString())));
				else
					Day.setModel(cbData.getDefaultData());
			}
		});

		Date year = new Date();

		final JLabel lblYear = new JLabel(
				Integer.toString(year.getYear() + 1900));

		final JCheckBox chckbxUseCurrentDate = new JCheckBox("Use Current Date");
		chckbxUseCurrentDate.setSelected(true);
		Month.setEnabled(false);
		Day.setEnabled(false);
		lblYear.setEnabled(false);
		chckbxUseCurrentDate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxUseCurrentDate.isSelected()) {
					Month.setEnabled(false);
					Day.setEnabled(false);
					lblYear.setEnabled(false);

				} else {
					Month.setEnabled(true);
					Day.setEnabled(true);
					lblYear.setEnabled(true);
				}
			}
		});

		// button action listeners, adds data to table
		JButton btnDeposit = new JButton("Deposit");
		btnDeposit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					amount = Double.parseDouble(txtAmount.getText());
					if (txtDescription.getText().equals("Deposit")) {
						if (chckbxUseCurrentDate.isSelected()) {
							setDate();
						} else {
							if (Month.getSelectedIndex() != 0
									|| Day.getSelectedIndex() != 0) {
								date = Month.getSelectedItem().toString() + "/"
										+ Day.getSelectedItem().toString()
										+ "/" + lblYear.getText();
							} else
								JOptionPane
										.showMessageDialog(
												null,
												"One or more fields were left blank."
														+ "\nPlease check that all fields have values inputted into them.",
												"Error",
												JOptionPane.ERROR_MESSAGE);
						}
						if (checkInputFields()) {
							td.addData(date, desc, amount);
							resetVariables();
						}
					} else {
						JOptionPane
								.showMessageDialog(
										null,
										"You defined description as \""
												+ txtDescription.getText()
												+ "\",\n"
												+ "please do one of the following:\n1. Change description to \"Deposit\"\n"
												+ "2. Choose the \"Transaction\" button",
										"Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null,
							"This field will only accept numeric values.\n"
									+ "(Decimal (\".\") points are allowed)",
							"Invalid Input", JOptionPane.ERROR_MESSAGE);
					txtAmount.setText("");
				}
			}
		});
		// button action listener, adds data to table
		JButton btnTransaction = new JButton("Transaction");
		btnTransaction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					amount = Double.parseDouble(txtAmount.getText());
					if (chckbxUseCurrentDate.isSelected()) {
						setDate();
					} else {
						if (Month.getSelectedIndex() != 0
								|| Day.getSelectedIndex() != 0) {
							date = Month.getSelectedItem().toString() + "/"
									+ Day.getSelectedItem().toString() + "/"
									+ lblYear.getText();
						} else
							JOptionPane
									.showMessageDialog(
											null,
											"One or more fields were left blank."
													+ "\nPlease check that all fields have values inputted into them.",
											"Error", JOptionPane.ERROR_MESSAGE);
					}
					if (checkInputFields()) {
						String temp = txtDescription.getText().toLowerCase();
						if (temp.equals("deposit")) {
							System.out.println("temp is equal to deposit");
							JOptionPane
									.showMessageDialog(
											null,
											"You defined \"Deposit\" as your description,\n"
													+ "please do one of the following:\n1. Change description to a withdraw type\n"
													+ "2. Choose the \"Deposit\" button",
											"Error", JOptionPane.ERROR_MESSAGE);
						} else {
							if (amount > 0)
								amount *= -1;
							td.addData(date, desc, amount);
							resetVariables();
						}
					}
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null,
							"This field will only accept numeric values.\n"
									+ "(Decimal (\".\") points are allowed)",
							"Invalid Input", JOptionPane.ERROR_MESSAGE);
					txtAmount.setText("");
				}
			}
		});
		txtDescription.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				enteringData = true;
				if (txtDescription.getText().equals("Description"))
					txtDescription.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
			}

		});
		txtAmount.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				enteringData = true;
				if (txtAmount.getText().equals("Amount"))
					txtAmount.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
			}

		});
		txtDescription.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				desc = txtDescription.getText();
				txtAmount.setText("");
				txtAmount.grabFocus();
			}
		});
		txtAmount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					amount = Double.parseDouble(txtAmount.getText());
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null,
							"This field will only accept numeric values.\n"
									+ "(Decimal (\".\") points are allowed)",
							"Invalid Input", JOptionPane.ERROR_MESSAGE);
					txtAmount.setText("");
				}
			}
		});

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						gl_panel.createSequentialGroup()
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.LEADING)
												.addGroup(
														gl_panel.createSequentialGroup()
																.addGap(222)
																.addComponent(
																		lblRecentTransactions))
												.addGroup(
														gl_panel.createSequentialGroup()
																.addGap(161)
																.addComponent(
																		btnDeposit)
																.addGap(89)
																.addComponent(
																		btnTransaction))
												.addGroup(
														gl_panel.createSequentialGroup()
																.addGap(60)
																.addComponent(
																		scrollPane,
																		GroupLayout.PREFERRED_SIZE,
																		457,
																		GroupLayout.PREFERRED_SIZE))
												.addGroup(
														gl_panel.createSequentialGroup()
																.addGap(127)
																.addGroup(
																		gl_panel.createParallelGroup(
																				Alignment.LEADING,
																				false)
																				.addGroup(
																						gl_panel.createSequentialGroup()
																								.addComponent(
																										chckbxUseCurrentDate)
																								.addGap(44)
																								.addComponent(
																										Month,
																										GroupLayout.PREFERRED_SIZE,
																										GroupLayout.DEFAULT_SIZE,
																										GroupLayout.PREFERRED_SIZE)
																								.addGap(18)
																								.addComponent(
																										Day,
																										GroupLayout.PREFERRED_SIZE,
																										GroupLayout.DEFAULT_SIZE,
																										GroupLayout.PREFERRED_SIZE)
																								.addGap(18)
																								.addComponent(
																										lblYear))
																				.addComponent(
																						txtAmount,
																						GroupLayout.DEFAULT_SIZE,
																						346,
																						Short.MAX_VALUE)
																				.addComponent(
																						txtDescription))))
								.addContainerGap(67, Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(gl_panel
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						gl_panel.createSequentialGroup()
								.addGap(41)
								.addComponent(lblRecentTransactions)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(scrollPane,
										GroupLayout.PREFERRED_SIZE, 127,
										GroupLayout.PREFERRED_SIZE)
								.addGap(74)
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.BASELINE)
												.addComponent(
														Month,
														GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(
														chckbxUseCurrentDate)
												.addComponent(
														Day,
														GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(lblYear))
								.addGap(36)
								.addComponent(txtDescription,
										GroupLayout.PREFERRED_SIZE, 35,
										GroupLayout.PREFERRED_SIZE)
								.addGap(33)
								.addComponent(txtAmount,
										GroupLayout.PREFERRED_SIZE, 36,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED,
										24, Short.MAX_VALUE)
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.BASELINE)
												.addComponent(btnDeposit)
												.addComponent(btnTransaction))
								.addGap(60)));

		panel.setLayout(gl_panel);
	}
}
