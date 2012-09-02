package raytracer.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame {

	private static final long serialVersionUID = 8024739998726352379L;
	private final JPanel renderButton;
	private final JTextField fileField;
	private final JFileChooser fileChooser = new JFileChooser();

	public static void main(String[] args) {

		// Set Nimbus look and feel
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager
						.getCrossPlatformLookAndFeelClassName());
			} catch (Exception ex) {
			}
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
		setTitle("Raytracer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 350);
		renderButton = new JPanel();
		renderButton.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(renderButton);
		renderButton.setLayout(null);

		fileField = new JTextField();
		fileField.setEditable(false);
		fileField.setBounds(12, 9, 317, 28);
		renderButton.add(fileField);
		fileField.setColumns(10);

		JButton fileSelectButton = new JButton("Scene File");
		Icon folderIcon = UIManager.getIcon("FileChooser.directoryIcon");
		fileSelectButton.setIcon(folderIcon);
		fileSelectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(Main.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					String fileName = file.getAbsolutePath();
					fileField.setText(fileName);
				}

			}
		});
		fileSelectButton.setBounds(341, 9, 137, 28);
		renderButton.add(fileSelectButton);

		JLabel statusLabel = new JLabel("Please select a scene file...");
		statusLabel.setBounds(12, 48, 466, 22);
		renderButton.add(statusLabel);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setEnabled(false);
		progressBar.setBounds(12, 286, 466, 22);
		renderButton.add(progressBar);

		JButton btnNewButton = new JButton("Render");
		btnNewButton.setEnabled(false);
		btnNewButton.setBounds(186, 249, 117, 25);
		renderButton.add(btnNewButton);
	}
}
