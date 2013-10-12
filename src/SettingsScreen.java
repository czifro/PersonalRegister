import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.JCheckBox;
import javax.swing.JSlider;

public class SettingsScreen extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private int index = 0;
	Vector<Vector> data = new Vector<Vector>();
	Vector<String> row = new Vector<String>();
	Vector<String> columns = new Vector<String>();
	int r, g, b = 0;
	Color color = Color.LIGHT_GRAY;
	DefaultTableModel dtm;
	String[] colors = { "Select Color", "Light Grey", "Light Blue",
			"Light Purple", "Pink" };
	String sColor;
	java.net.URL url = SettingsScreen.class.getResource("persregicon.png");
	ImageIcon icon = new ImageIcon(url);

	// creates preview table
	public void createPreview() {
		columns.add("Preview");
		for (int i = 0; i < 8; ++i) {
			row = new Vector<String>();
			row.add("Preview");
			data.add(row);
		}
		dtm = new DefaultTableModel(data, columns);
	}

	// sets color of table, returns Color type
	private void setColor(int index) {
		// switch statement to determine color
		switch (index) {
		case 1:
			color = Color.LIGHT_GRAY;
			break;
		case 2:
			color = Color.CYAN;
			break;
		case 3:
			color = Color.MAGENTA;
			break;
		case 4:
			color = Color.PINK;
			break;
		default:
			color = Color.WHITE;
		}
	}
	public void runWindow(){
		setVisible(true);
	}

	/**
	 * Launch the application.
	 * 
	 * Create the frame.
	 */
	public SettingsScreen(final TableColor tc, final JTable mTable,
			final MainFrame frame) {
		setTitle("Table Color");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 582, 378);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						"E:\\Programming\\eclipse_workspace\\MyProjects\\MoneyRegisterBetaII\\persregicon.png"));

		createPreview();

		table = new JTable(dtm) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			// sets color to rows
			@Override
			public Component prepareRenderer(TableCellRenderer r, int row,
					int column) {
				Component c = super.prepareRenderer(r, row, column);

				int trow = table.convertRowIndexToModel(row);

				if (trow % 2 == 0) {
					c.setBackground(Color.WHITE);
				} else {
					c.setBackground(color);
				}

				return c;
			}
		};
		table.setPreferredScrollableViewportSize(getSize());
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.setCellSelectionEnabled(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);

		final JComboBox ColorSelector = new JComboBox(colors);
		ColorSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				index = ColorSelector.getSelectedIndex();
				setColor(index);
				table.repaint();
				sColor = colors[index];
			}
		});

		JButton btnSaveChanges = new JButton("Save Changes");
		btnSaveChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tc.setColor(color);
				frame.saveChanges();
				frame.table.repaint();
				//mTable.repaint();
				dispose();
			}
		});

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JLabel lblPreview = new JLabel("Preview");
		lblPreview.setFont(new Font("Tahoma", Font.PLAIN, 14));

		
		final JLabel lblRed = new JLabel("Red");
		final JSlider redSlider = new JSlider();
		redSlider.setMaximum(255);
		redSlider.setPaintTicks(true);
		final JSlider greenSlider = new JSlider();
		greenSlider.setMaximum(255);
		greenSlider.setPaintTicks(true);
		final JSlider blueSlider = new JSlider();
		blueSlider.setMaximum(255);
		blueSlider.setPaintTicks(true);
		final JLabel lblGreen = new JLabel("Green");
		final JLabel lblBlue = new JLabel("Blue");
		final JButton btnPreview = new JButton("Preview");
		btnPreview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				r = redSlider.getValue();
				g = greenSlider.getValue();
				b = blueSlider.getValue();
				color = new Color(r, g, b);
				table.repaint();
			}
		});
		
		lblRed.setVisible(false);
		redSlider.setVisible(false);
		greenSlider.setVisible(false);
		blueSlider.setVisible(false);
		lblBlue.setVisible(false);
		lblGreen.setVisible(false);
		btnPreview.setVisible(false);
		
		final JCheckBox CreateCustomColor = new JCheckBox("Create Custom Color");
		CreateCustomColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (CreateCustomColor.isSelected()){
					lblRed.setVisible(true);
					redSlider.setVisible(true);
					greenSlider.setVisible(true);
					blueSlider.setVisible(true);
					lblBlue.setVisible(true);
					lblGreen.setVisible(true);
					btnPreview.setVisible(true);
					ColorSelector.setVisible(false);
				} else {
					lblRed.setVisible(false);
					redSlider.setVisible(false);
					greenSlider.setVisible(false);
					blueSlider.setVisible(false);
					lblBlue.setVisible(false);
					lblGreen.setVisible(false);
					btnPreview.setVisible(false);
					ColorSelector.setVisible(true);
				}
			}
		});
		

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(0)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(45)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(CreateCustomColor)
									.addGap(42)
									.addComponent(ColorSelector, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
									.addComponent(lblPreview)
									.addGap(37))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
											.addGroup(gl_contentPane.createSequentialGroup()
												.addComponent(lblRed)
												.addGap(28))
											.addGroup(gl_contentPane.createSequentialGroup()
												.addComponent(lblGreen)
												.addGap(18)))
										.addComponent(lblBlue))
									.addGap(36)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(greenSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(blueSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(redSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
									.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(279)
							.addComponent(btnSaveChanges)
							.addGap(18)
							.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(418)
							.addComponent(btnPreview)))
					.addGap(53))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(43)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(CreateCustomColor)
								.addComponent(ColorSelector, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblRed)
								.addComponent(redSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(128)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblGreen)
								.addComponent(greenSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblBlue)
						.addComponent(blueSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(131, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(40)
					.addComponent(lblPreview)
					.addGap(18)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnPreview)
					.addPreferredGap(ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnSaveChanges))
					.addGap(19))
		);

		contentPane.setLayout(gl_contentPane);
	}
}
