package com.aif.ui.javafx;

import com.aif.model.exceptions.InputOpenException;
import com.aif.model.memory.short_time.Text;
import com.aif.model.memory.short_time.Word;
import com.aif.ui.javafx.FileManager.FileWrapper;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FXText extends Application {

	private Map<String, Text> textData = new HashMap<>();
	private Text curTextData = null;

	public static void main(String[] args) {

		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) {
		primaryStage.setTitle("Chooser test");

		Scene scene = new Scene(new VBox(), 500, 450);
		scene.setFill(Color.OLDLACE);
		//
		MenuBar menuBar = new MenuBar();

		// --- Menu File
		Menu menuFile = new Menu("File");
		MenuItem openFileItem = new MenuItem("Open File");
		menuFile.getItems().add(openFileItem);
		MenuItem closeItem = new MenuItem("Close");
		menuFile.getItems().add(closeItem);

		closeItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				primaryStage.close();
			}
		});

		openFileItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage myDialog = new Stage();
				myDialog.initModality(Modality.WINDOW_MODAL);

				FileChooser fileChooser = new FileChooser();

				fileChooser.setTitle("Select File");

				fileChooser.getExtensionFilters().add(
						new FileChooser.ExtensionFilter("*", "*"));

				File file = fileChooser.showOpenDialog(null);

				if (file != null) {
					primaryStage.setTitle(file.getPath());
					FileManager.filesProperty().add(
							new FileWrapper(file.getPath(), "test"));

					try {
						Text text = new Text(file.getPath());
						textData.put(file.getPath(), text);
					} catch (InputOpenException e) {
						e.printStackTrace();
					}

				}
			}
		});

		menuBar.getMenus().addAll(menuFile);
		((VBox) scene.getRoot()).getChildren().addAll(menuBar);
		GridPane pane = makeTableView();

		((VBox) scene.getRoot()).getChildren().add(pane);
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(5));
		final TextArea textArea = new TextArea();
		
		textArea.setEditable(false);
		textArea.setPrefWidth(400);
		textArea.setPrefHeight(80);

		Button textCalculation = new Button("Current\ntext theme");
		textCalculation.setTextAlignment(TextAlignment.CENTER);
		textCalculation.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(curTextData != null) {
					List<Word> theme = curTextData.getThem();
					if(theme != null){
						StringBuilder sb = new StringBuilder();
						for(int i = 0 ; i < theme.size(); ++i)
							sb.append(theme.get(i) + " ");
						textArea.setText(sb.toString());
					}
				}
			}
		});

		hbox.getChildren().add(textCalculation);

		hbox.getChildren().add(textArea);

		HBox.setMargin(textArea, new Insets(5));
		HBox.setMargin(textCalculation, new Insets(4));
		((VBox) scene.getRoot()).getChildren().add(hbox);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private GridPane makeTableView() {
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(5));
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		Label candidatesLbl = new Label("Files");
		GridPane.setHalignment(candidatesLbl, HPos.CENTER);
		gridPane.add(candidatesLbl, 0, 0);

		final ListView<FileWrapper> resultListView = new ListView<>(
				FileManager.filesProperty());
		resultListView.setPrefWidth(150);
		resultListView.setPrefHeight(150);

		resultListView
				.setCellFactory(new Callback<ListView<FileWrapper>, ListCell<FileWrapper>>() {
					public ListCell<FileWrapper> call(
							ListView<FileWrapper> param) {
						final Label leadLbl = new Label();
						final Tooltip tooltip = new Tooltip();
						final ListCell<FileWrapper> cell = new ListCell<FileWrapper>() {
							@Override
							public void updateItem(FileWrapper item,
									boolean empty) {
								super.updateItem(item, empty);
								if (item != null) {
									leadLbl.setText(item.getFilePath());
									setText(item.getFilePath());
									tooltip.setText(item.getFilePath());
									setTooltip(tooltip);
								}
							}
						};
						return cell;
					}
				});
		gridPane.add(resultListView, 0, 1);
		Label resLbl = new Label("Results");
		gridPane.add(resLbl, 2, 0);
		GridPane.setHalignment(resLbl, HPos.CENTER);
		final TableView<WordWrapper> wordsTableView = new TableView<>();
		wordsTableView.setPrefWidth(300);
		final ObservableList<WordWrapper> results = FXCollections
				.observableArrayList();
		wordsTableView.setItems(results);
		TableColumn<WordWrapper, String> aliasNameCol = new TableColumn<>(
				"Word");
		aliasNameCol.setEditable(true);

		aliasNameCol.setCellValueFactory(new PropertyValueFactory(
				WordWrapper.WORD));
		aliasNameCol.setPrefWidth(wordsTableView.getPrefWidth() / 3);
		TableColumn<WordWrapper, String> firstNameCol = new TableColumn<>(
				"Weight");
		firstNameCol.setCellValueFactory(new PropertyValueFactory(
				WordWrapper.FIRST_VALUE));
		firstNameCol.setPrefWidth(wordsTableView.getPrefWidth() / 3);
		TableColumn<WordWrapper, String> lastNameCol = new TableColumn<>(
				"Count");
		lastNameCol.setCellValueFactory(new PropertyValueFactory(
				WordWrapper.SECOND_VALUE));
		lastNameCol.setPrefWidth(wordsTableView.getPrefWidth() / 3);
		wordsTableView.getColumns().setAll(aliasNameCol, firstNameCol,
				lastNameCol);
		gridPane.add(wordsTableView, 2, 1);

		resultListView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<FileWrapper>() {
					public void changed(
							ObservableValue<? extends FileWrapper> observable,
							FileWrapper oldValue, FileWrapper newValue) {
						if (observable != null && observable.getValue() != null) {
							results.clear();
							Text text = textData.get(newValue.getFilePath());
							if (text != null) {
								List<Word> words = text.getWords();
								for (Word word : words) {

                                    double maxConn = 0;
                                    Set<Word> connections = word.getConnectedWords();
                                    for (Word connWord : connections){
                                        double currConn =
                                                text.getConnectionLevelBetweenWords(word, connWord);
                                        if (currConn > maxConn)
                                            maxConn = currConn;
                                    }

									results.add(new WordWrapper(
											word.toString(),
                                            "["+
                                            String.valueOf(word.getConnectedWords().size())+":"+
                                            String.format("%.2f",maxConn)+"]"+
											String.valueOf(text
													.getWordWeight(word)),
											String.valueOf(word.getCount())+
                                            "/"+String.valueOf(word.getWordFormsStrings().size()) + "(" +
                                            String.format("%.2f", word.getAbstractoinLevel())+")"));
								}
								curTextData = text;
							} else {
								// TODO: do something
								curTextData = null;
							}
						}
					}

				});

		wordsTableView
				.setRowFactory(new Callback<TableView<WordWrapper>, TableRow<WordWrapper>>() {
					public TableRow<WordWrapper> call(
							TableView<WordWrapper> param) {

						final TableRow<WordWrapper> cell = new TableRow<WordWrapper>() {

							@Override
							public void updateSelected(boolean shouldBeUpdated) {
								super.updateSelected(shouldBeUpdated);
								if (shouldBeUpdated && isPressed()) {
									final TableView<WordWrapper> tableView = new TableView<>();
									tableView.setPrefWidth(150);
									final ObservableList<WordWrapper> results = FXCollections
											.observableArrayList();

									if (curTextData != null) {
										int index = getIndex();
										if (index < curTextData.getWords()
												.size()) {
											Word word = curTextData.getWords()
													.get(index);
											for (String form : word
													.getWordFormsStrings())
												results.add(new WordWrapper(
														form, "", ""));
										}

										tableView.setItems(results);
										TableColumn<WordWrapper, String> wordCol = new TableColumn<>(
												"Word");
										wordCol.setEditable(false);
										wordCol.setCellValueFactory(new PropertyValueFactory(
												WordWrapper.WORD));
										wordCol.setPrefWidth(tableView
												.getPrefWidth());
										tableView.getColumns().setAll(wordCol);

										Stage dialogStage = new Stage();
										dialogStage
												.initModality(Modality.WINDOW_MODAL);
										dialogStage.setScene(new Scene(
												VBoxBuilder.create()
														.children(tableView)
														.alignment(Pos.CENTER)
														.padding(new Insets(5))
														.build()));
										dialogStage.show();
									}
								}
							}

						};
						return cell;
					}

				});

		return gridPane;
	}

}
