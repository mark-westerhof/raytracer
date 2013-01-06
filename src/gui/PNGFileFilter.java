package gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class PNGFileFilter extends FileFilter {

	public boolean accept(File f) {

		return f.getName().toLowerCase().endsWith(".png") || f.isDirectory();
	}

	public String getDescription() {
		return "PNG Image Files (*.png)";
	}
}
