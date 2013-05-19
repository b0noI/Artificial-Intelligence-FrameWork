package com.aif.ui.javafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FileManager {

	private static ObservableList<FileWrapper> files = FXCollections
			.observableArrayList();

	public static ObservableList<FileWrapper> filesProperty() {
		return files;
	}

	public static class FileWrapper {
		private StringProperty filePath;
		private StringProperty fileName;

		public final void setFilePath(String value) {
			filePathProperty().set(value);
		}

		public final String getFilePath() {
			return filePathProperty().get();
		}

		public StringProperty filePathProperty() {
			if (filePath == null) {
				filePath = new SimpleStringProperty();
			}
			return filePath;
		}

		public final void setFileName(String value) {
			fileNameProperty().set(value);
		}

		public final String getFileName() {
			return fileNameProperty().get();
		}

		public StringProperty fileNameProperty() {
			if (fileName == null) {
				fileName = new SimpleStringProperty();
			}
			return fileName;
		}

		public FileWrapper() {
		};

		public FileWrapper(String filePath, String fileName) {
			setFilePath(filePath);
			setFileName(fileName);
		}
	}
}