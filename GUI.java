import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class GUI {
	
	JTextField field1, field2;
	JFileChooser fc = new JFileChooser();
	JPanel north_panel;
	BrowseSeqGenerator gen = new BrowseSeqGenerator();

	public void createGUI() {
		
		// Set basic properties for the window
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Browse Sequence Generator");
		
		// These panels will contain the inputs
		north_panel = new JPanel();
		north_panel.setLayout(new BoxLayout(north_panel, BoxLayout.Y_AXIS));
		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		// This panel will contain the Generate button
		JPanel south_panel = new JPanel();
		south_panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		// First label, field and button (to select table of contents)
		JLabel label1 = new JLabel("Base browse sequence on: ");
		panel1.add(label1);
		field1 = new JTextField(FIELD1_INITIAL_TEXT, FIELD_LENGTH);
		field1.setEditable(false);
		panel2.add(field1);
		JButton button1 = new JButton(new ImageIcon(getClass().getResource("Open16.gif")));
		panel2.add(button1);
		button1.addActionListener(new OpenButtonListener());
		
		// Second label, field and button (to select browse sequence)
		JLabel label2 = new JLabel("Save browse sequence as: ");
		panel3.add(label2);
		field2 = new JTextField(FIELD2_INITIAL_TEXT, FIELD_LENGTH);
		field2.setEditable(false);
		panel4.add(field2);
		JButton button2 = new JButton(new ImageIcon(getClass().getResource("Save16.gif")));
		panel4.add(button2);
		button2.addActionListener(new SaveButtonListener());
		
		// Generate button for creating the browse sequence
		JButton button3 = new JButton("Generate");
		south_panel.add(button3);
		button3.addActionListener(new GenerateButtonListener());
		
		// Close button
		JButton button4 = new JButton("Close");
		south_panel.add(button4);
		button4.addActionListener(new CloseButtonListener());
		
		// Add all components to window
		north_panel.add(panel1);
		north_panel.add(panel2);
		north_panel.add(panel3);
		north_panel.add(panel4);
		window.getContentPane().add(BorderLayout.NORTH, north_panel);
		window.getContentPane().add(BorderLayout.SOUTH, south_panel);
		
		window.pack(); // Set window size to fit everything
		window.setVisible(true); // Display window
	}
	
	
	/*
	 * User browses to a file that is then opened for reading by the
	 * program. The selected file is displayed in a text field.
	 */
	class OpenButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (fc.showOpenDialog(north_panel) == JFileChooser.APPROVE_OPTION) {
				try {
					gen.read(fc.getSelectedFile());
					field1.setText(fc.getSelectedFile().getAbsolutePath());
				} catch (IOException ex) {
					System.err.println(ex.getMessage());
				}
			}
		}
	}
	
	/*
	 * Sets name of file that the browse sequence will be saved as.
	 */
	class SaveButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (fc.showSaveDialog(north_panel) == JFileChooser.APPROVE_OPTION) {
				gen.setBSName(fc.getSelectedFile());
				field2.setText(fc.getSelectedFile().getAbsolutePath());
			}
		}
	}

	/*
	 * When the Generate button is clicked, the specified browse sequence file is 
	 * created based on the selected table of contents file.
	 */
	class GenerateButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				if (gen.createBRS()) {
					JOptionPane.showMessageDialog(north_panel, "Browse sequence created.");
				} else {
					JOptionPane.showMessageDialog(north_panel, "Inputs missing.");
				}
			} catch (IOException ex) {
			        JOptionPane.showMessageDialog(north_panel, ex.getMessage());
				System.err.println(ex.getMessage());
			} finally {
				try { 
					gen.close(); 
					field1.setText(FIELD1_INITIAL_TEXT); 
					field2.setText(FIELD2_INITIAL_TEXT);} 
				catch (IOException ex) { System.err.println(ex.getMessage()); }
			}
		}
	}
	
	class CloseButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try { gen.close(); }
			catch (IOException ex) { System.err.println(ex.getMessage()); }
			finally { System.exit(0); }
		}
	}
	
	private static final String FIELD1_INITIAL_TEXT = "<Select *.hhc file>";
	private static final String FIELD2_INITIAL_TEXT = "<Select *.brs file>";
	private static final int FIELD_LENGTH = 30;
}
