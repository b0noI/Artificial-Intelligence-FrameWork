package com.aif.ui.javafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class WordWrapper {
	public static final String WORD = "word";
	public static final String FIRST_VALUE = "firstValue";
	public static final String SECOND_VALUE = "secondValue";
	
	private StringProperty word;
	private StringProperty firstValue;
	private StringProperty secondValue;

	public final void setWord(String value) {
		wordProperty().set(value);
	}

	public final String getWord() {
		return wordProperty().get();
	}

	public StringProperty wordProperty() {
		if (word == null) {
			word = new SimpleStringProperty();
		}
		return word;
	}

	public final void setFirstValue(String value) {
		firstValueProperty().set(value);
	}

	public final String getFirstValue() {
		return firstValueProperty().get();
	}

	public StringProperty firstValueProperty() {
		if (firstValue == null) {
			firstValue = new SimpleStringProperty();
		}
		return firstValue;
	}
	
	private void setSecondValue(String secondValue) {
		secondValue().set(secondValue);
	}
	
	public final String getSecondValue() {
		return secondValue().get();
	}

	public StringProperty secondValue() {
		if (secondValue == null) {
			secondValue = new SimpleStringProperty();
		}
		return secondValue;
	}


	public WordWrapper() {
	};

	public WordWrapper(String word, String firstValue, String secondValue) {
		setWord(word);
		setFirstValue(firstValue);
		setSecondValue(secondValue);
	}


}