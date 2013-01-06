package gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class SceneFileFilter extends FileFilter {

	public boolean accept(File f) {

		return f.getName().toLowerCase().endsWith(".json") || f.isDirectory();
	}

	public String getDescription() {
		return "Scene Files (*.json)";
	}
}
