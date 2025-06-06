package com.internshala.connectfour;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
	private Controller controller;

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
		GridPane rootgridpane = loader.load();
		controller = loader.getController();
		controller.createplayground();
		MenuBar menubar=createmenu();
		menubar.prefWidthProperty().bind(primaryStage.widthProperty());
		Pane mpane= (Pane) rootgridpane.getChildren().get(0);
		mpane.getChildren().add(menubar);
		Scene scene = new Scene(rootgridpane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Connect Four");
		primaryStage.setResizable(false);
		primaryStage.show();

	}
	private MenuBar createmenu(){
		Menu file = new Menu("File");
		MenuItem newgame = new MenuItem("New game");
		newgame.setOnAction(event -> controller.resetGame());

		MenuItem reset = new MenuItem("Reset game");
		reset.setOnAction(event -> controller.resetGame());

		SeparatorMenuItem separate = new SeparatorMenuItem();

		MenuItem exitGame = new MenuItem("Exit game");
		exitGame.setOnAction(event -> exitgame());

		file.getItems().addAll(newgame,reset,separate,exitGame);
		Menu help = new Menu("Help");
		MenuItem aboutgame = new MenuItem("About Connect4");
		aboutgame.setOnAction(event -> aboutconnect());
		SeparatorMenuItem separatee = new SeparatorMenuItem();
		MenuItem aboutme = new MenuItem("About Me");
		aboutme.setOnAction(event -> aboutMe());

		help.getItems().addAll(aboutgame,separatee,aboutme);

		MenuBar menubar = new MenuBar();
		menubar.getMenus().addAll(file,help);
		return menubar;

	}

	private void aboutMe() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About The Developer");
		alert.setHeaderText("Kritika");
		alert.setContentText("I love to play around codes and create games." +
				"Connect4 is one of them.");
		alert.show();
	}

	private void aboutconnect() {
		Alert alert =new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About Connect Four");
		alert.setHeaderText("How to Play ?");
		alert.setContentText("Connect Four is a two-player connection game in which the players" +
				" first choose a color and then take turns dropping colored discs from the top" +
				" into a seven-column, six-row vertically suspended grid. The pieces fall " +
				"straight down, occupying the next available space within the column. The" +
				" objective of the game is to be the first to form a horizontal, vertical, or " +
				"diagonal line of four of one's own discs. Connect Four is a solved game. The " +
				"first player can always win by playing the right moves.");
		alert.show();
	}

	private void exitgame() {
		Platform.exit();
		System.exit(0);
	}

	private void resetGame() {


	}


	public static void main(String[] args) {

		launch(args);

	}
}