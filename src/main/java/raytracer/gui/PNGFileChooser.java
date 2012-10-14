package raytracer.gui;

import java.awt.Component;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import raytracer.scene.Scene;

public class PNGFileChooser extends JFileChooser {

	private static final long serialVersionUID = 2781518252046790019L;

	private Scene scene;

	public PNGFileChooser(Scene scene) {
		super();
		this.scene = scene;
		setFileFilter(new PNGFileFilter());
		setAcceptAllFileFilterUsed(false);
	}

	public PNGFileChooser(File originalSceneFile, Scene scene) {
		this(scene);
		String sceneFilePath = originalSceneFile.getAbsolutePath();
		File defaultImage = new File(sceneFilePath.substring(0, sceneFilePath.lastIndexOf('.')) + ".png");
		setSelectedFile(defaultImage);
	}

	public File getSelectedFile() {
		if (getDialogType() != SAVE_DIALOG) {
			return super.getSelectedFile();
		}
		else {
			File selectedFile = super.getSelectedFile();

			if (selectedFile != null && !selectedFile.getName().contains(".")) {
				return new File(selectedFile.getAbsolutePath() + ".png");
			}
			else {
				return selectedFile;
			}
		}
	}

	public void approveSelection() {
		if (getDialogType() == SAVE_DIALOG) {
			File selectedFile = getSelectedFile();

			if ((selectedFile != null) && selectedFile.exists()) {
				String message = "The file " + selectedFile.getName()
						+ " already exists. Do you want to replace the existing file?";

				int response = JOptionPane.showConfirmDialog(this, message, "Overwrite Exisiting File",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response != JOptionPane.YES_OPTION) {
					return;
				}
			}

			if (!isValidExtension(selectedFile)) {
				String message = selectedFile.getName() + " is not a valid PNG extension";
				JOptionPane.showMessageDialog(this, message, "Invalid PNG Extension", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				ImageIO.write(scene.getImage(), "png", selectedFile);
			}
			catch (Exception e) {
				String message = "Failed to write " + selectedFile.getAbsolutePath();
				JOptionPane.showMessageDialog(this, message, "Failed To Write File", JOptionPane.ERROR_MESSAGE);
			}
		}
		super.approveSelection();
	}

	private boolean isValidExtension(File selectedFile) {
		return selectedFile.getName().toLowerCase().endsWith(".png");
	}

	public void selectAndSave(Component parent) {
		showSaveDialog(parent);
	}
}
