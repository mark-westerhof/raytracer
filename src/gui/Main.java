package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import raytrace.RayTracer;
import scene.Scene;
import scene.SceneReader;
import scene.exception.SceneException;

public class Main extends JFrame {

	private static final long serialVersionUID = 8024739998726352379L;

	private final JPanel panel;
	private final JTextField fileField;
	private final JButton fileSelectButton;
	private final JFileChooser sceneFileChooser;
	private final JProgressBar progressBar;
	private final JLabel statusLabel;
	private final JButton renderButton;
	private final JPanel optionsPanel;
	private final JCheckBox superSampleCheckBox;
	private final JSpinner traceDepthSpinner;
	private final JLabel traceDepthLabel;

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
		setSize(500, 350);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel();
		panel.setLayout(null);
		getContentPane().add(panel);
		// setIconImage(new ImageIcon(getClass().getResource("/picture.png")).getImage());

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
		progressBar.setStringPainted(true);
		progressBar.setBounds(12, 286, 474, 22);
		panel.add(progressBar);

		renderButton = new JButton("Render");
		renderButton.setEnabled(false);
		renderButton.setBounds(191, 249, 117, 25);
		renderButton.addActionListener(new Render());
		panel.add(renderButton);

		optionsPanel = new JPanel();
		optionsPanel.setEnabled(false);
		optionsPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Options",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		optionsPanel.setBounds(12, 83, 474, 154);
		panel.add(optionsPanel);
		optionsPanel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("8dlu:grow"), FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, },
				new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

		superSampleCheckBox = new JCheckBox("Super Sample");
		optionsPanel.add(superSampleCheckBox, "2, 2");
		superSampleCheckBox.setVerticalAlignment(SwingConstants.BOTTOM);
		superSampleCheckBox.setToolTipText("Do a 9 ray sample per pixel instead of 1");
		superSampleCheckBox.setEnabled(false);

		traceDepthLabel = new JLabel("MaxTrace Depth:");
		traceDepthLabel.setToolTipText("The maximum amount of bounces for a ray (reflection)");
		traceDepthLabel.setEnabled(false);
		optionsPanel.add(traceDepthLabel, "4, 2");
		traceDepthLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		traceDepthLabel.setVerticalAlignment(SwingConstants.BOTTOM);

		traceDepthSpinner = new JSpinner();
		traceDepthSpinner.setToolTipText("The maximum amount of bounces for a ray (reflection)");
		optionsPanel.add(traceDepthSpinner, "6, 2");
		traceDepthSpinner.setEnabled(false);
		traceDepthSpinner.setModel(new SpinnerNumberModel(6, 0, 15, 1));

		String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		sceneFileChooser = new JFileChooser(path);
		sceneFileChooser.setFileFilter(new SceneFileFilter());
		sceneFileChooser.setAcceptAllFileFilterUsed(false);
	}

	private class FileSelect implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			int returnVal = sceneFileChooser.showOpenDialog(Main.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				final String fileName = sceneFileChooser.getSelectedFile().getAbsolutePath();

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						fileField.setText(fileName);
						fileSelectButton.setEnabled(false);
						renderButton.setEnabled(false);
						progressBar.setEnabled(false);
						optionsPanel.setEnabled(false);
						superSampleCheckBox.setEnabled(false);
						traceDepthLabel.setEnabled(false);
						traceDepthSpinner.setEnabled(false);
						progressBar.setValue(0);
						statusLabel.setText("Loading scene file...");
						statusLabel.setForeground(Color.BLACK);
						setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					}
				});

				new SwingWorker<Void, Integer>() {
					protected Void doInBackground() throws Exception {
						try {
							scene = SceneReader.readSceneFile(fileName);
							updateStatusLabel("Valid scene file", new Color(0, 100, 0));
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									renderButton.setEnabled(true);
									progressBar.setEnabled(true);
									optionsPanel.setEnabled(true);
									superSampleCheckBox.setEnabled(true);
									traceDepthLabel.setEnabled(true);
									traceDepthSpinner.setEnabled(true);

								}
							});
						}
						catch (SceneException e1) {
							updateStatusLabel("Error: " + e1.getMessage(), Color.RED);
						}
						catch (IOException e1) {
							updateStatusLabel("Could not read file", Color.RED);
						}
						return null;
					}

					protected void done() {
						fileSelectButton.setEnabled(true);
						setCursor(null);
					}
				}.execute();
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

	private class Render implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			scene.setProgressBar(progressBar);
			scene.setSuperSampled(superSampleCheckBox.isSelected());
			scene.setTraceDepth((Integer) traceDepthSpinner.getValue());

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					progressBar.setValue(0);
					progressBar.setMaximum(scene.getCameraResolutionX() * scene.getCameraResolutionY());
					fileSelectButton.setEnabled(false);
					renderButton.setEnabled(false);
					optionsPanel.setEnabled(false);
					superSampleCheckBox.setEnabled(false);
					traceDepthLabel.setEnabled(false);
					traceDepthSpinner.setEnabled(false);
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				}
			});

			new SwingWorker<Void, Integer>() {
				long elapsedTime;

				protected Void doInBackground() throws Exception {
					long startTime = System.nanoTime();
					RayTracer.traceScene(scene);
					elapsedTime = System.nanoTime() - startTime;
					return null;
				}

				protected void done() {
					setCursor(null);
					DecimalFormat df = new DecimalFormat("###.###");
					String time = df.format((elapsedTime / 1000000000.0));
					final JFrame resultFrame = new JFrame("Rendered Image (" + time + "s)");
					// resultFrame.setIconImage(new ImageIcon(getClass().getResource("/picture.png")).getImage());
					JPanel resultPanel = new JPanel();
					resultFrame.getContentPane().add(resultPanel);

					resultFrame.addWindowListener(new WindowAdapter() {
						public void windowClosing(WindowEvent e) {
							progressBar.setValue(0);
							scene.resetProgressBarState();
							fileSelectButton.setEnabled(true);
							renderButton.setEnabled(true);
							optionsPanel.setEnabled(true);
							superSampleCheckBox.setEnabled(true);
							traceDepthLabel.setEnabled(true);
							traceDepthSpinner.setEnabled(true);
						}
					});

					JLabel image = new JLabel(new ImageIcon(scene.getImage()));
					image.setSize(scene.getCameraResolutionX(), scene.getCameraResolutionY());
					final JPopupMenu popup = new JPopupMenu();
					JMenuItem saveItem = new JMenuItem("Save as png...");
					saveItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							PNGFileChooser imageFileChooser = new PNGFileChooser(sceneFileChooser.getSelectedFile(),
									scene);
							imageFileChooser.selectAndSave(resultFrame);
						}
					});
					popup.add(saveItem);
					image.addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent e) {
							maybeShowPopup(e);
						}

						public void mouseReleased(MouseEvent e) {
							maybeShowPopup(e);
						}

						private void maybeShowPopup(MouseEvent e) {
							if (e.isPopupTrigger()) {
								popup.show(e.getComponent(), e.getX(), e.getY());
							}
						}
					});

					resultPanel.add(image);
					resultFrame.pack();
					resultFrame.setLocationRelativeTo(null);
					resultFrame.setVisible(true);
				}
			}.execute();
		}
	}
}
