package raytracer.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

import raytracer.scene.Scene;
import raytracer.scene.SceneReader;
import raytracer.scene.exception.SceneException;

public class Main extends JFrame {

	private static final long serialVersionUID = 8024739998726352379L;

	private final JPanel panel;
	private final JTextField fileField;
	private final JButton fileSelectButton;
	private final JFileChooser fileChooser = new JFileChooser();
	private final JProgressBar progressBar;
	private final JLabel statusLabel;
	private final JButton renderButton;

	private Scene scene;

	public static void main(String[] args) {

		// Set Nimbus look and feel
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}
		catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}
			catch (Exception ex) {
			}
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
		setTitle("Raytracer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 350);
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);
		panel.setLayout(null);

		fileField = new JTextField();
		fileField.setEditable(false);
		fileField.setBounds(12, 9, 317, 28);
		panel.add(fileField);
		fileField.setColumns(10);

		fileSelectButton = new JButton("Scene File");
		Icon folderIcon = UIManager.getIcon("FileChooser.directoryIcon");
		fileSelectButton.setIcon(folderIcon);
		fileSelectButton.addActionListener(new FileSelect());
		fileSelectButton.setBounds(341, 9, 145, 28);
		panel.add(fileSelectButton);

		statusLabel = new JLabel("Please select a scene file...");
		statusLabel.setForeground(Color.BLACK);
		statusLabel.setBounds(12, 49, 466, 22);
		panel.add(statusLabel);

		progressBar = new JProgressBar();
		progressBar.setEnabled(false);
		progressBar.setValue(0);
		progressBar.setBounds(12, 316, 474, 22);
		panel.add(progressBar);

		renderButton = new JButton("Render");
		renderButton.setEnabled(false);
		renderButton.setBounds(186, 283, 117, 25);
		panel.add(renderButton);
	}

	private class FileSelect implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			int returnVal = fileChooser.showOpenDialog(Main.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				final String fileName = file.getAbsolutePath();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						fileField.setText(fileName);
						fileSelectButton.setEnabled(false);
						renderButton.setEnabled(false);
						progressBar.setEnabled(false);
						statusLabel.setText("Loading scene file...");
						statusLabel.setForeground(Color.BLACK);
					}
				});
				Runnable readerRunnable = new Runnable() {
					public void run() {
						try {
							scene = SceneReader.readSceneFile(fileName);
							updateStatusLabel("Valid scene file", new Color(0, 100, 0));
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									renderButton.setEnabled(true);
									progressBar.setEnabled(true);
								}
							});
						}
						catch (SceneException e1) {
							updateStatusLabel("Error: " + e1.getMessage(), Color.RED);
						}
						catch (IOException e1) {
							updateStatusLabel("Could not read file", Color.RED);
						}
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								fileSelectButton.setEnabled(true);
							}
						});
					}
				};
				new Thread(readerRunnable).start();
			}
		}
	}

	private void updateStatusLabel(final String message, final Color color) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				statusLabel.setText(message);
				if (color != null) {
					statusLabel.setForeground(color);
				}
			}
		});
	}
}
