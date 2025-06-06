package com.internshala.connectfour;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {
	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	private static final int Cdiameter = 80;
	private static final String color1 = "#24303E";
	private static final String color2 = "#4CAA88";
	private static String PlayOne = "Player One";
	private static String PlayTwo = "Player Two";
	private boolean isplay1 = true;
	private Disc[][] insertedDiscsarr = new Disc[ROWS][COLUMNS];

	@FXML
	public GridPane rootgridpane;
	@FXML
	public Pane insertedDiscspane;
	@FXML
	public Label playernamelabel;
	@FXML
	public Button gamebutton;
	@FXML
	public TextField field1;
	@FXML
	public TextField field2;

	private boolean isAllowedtoInsert = true;

	public void createplayground(){
		Shape rech = createGameStructuralGrid();
		rootgridpane.add(rech,0,1);
		List<Rectangle> rectangleList = createClickableColumns();
		for (Rectangle rectangle:rectangleList){
			rootgridpane.add(rectangle,0,1);

		}
	}
	private Shape createGameStructuralGrid(){
		Shape rech = new Rectangle((COLUMNS + 1)*Cdiameter,Cdiameter*(ROWS + 1));
		for (int rows = 0;rows<ROWS;rows++){
			for(int cols = 0;cols<COLUMNS;cols++){
				Circle circle = new Circle();
				circle.setRadius(Cdiameter / 2);
				circle.setCenterX(Cdiameter / 2);
				circle.setCenterY(Cdiameter / 2);
				circle.setSmooth(true);

				circle.setTranslateX(cols*(Cdiameter + 5) + Cdiameter / 4);
				circle.setTranslateY(rows*(Cdiameter + 5) + Cdiameter / 4);
				rech = Shape.subtract(rech,circle);

			}
		}

		rech.setFill(Color.WHITE);
		return rech;
	}
	private List<Rectangle> createClickableColumns(){
		List<Rectangle> rectangleList = new ArrayList<>();

		for (int col = 0;col<COLUMNS;col++){
			Rectangle rectangle = new Rectangle(Cdiameter,Cdiameter*(ROWS + 1));
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setTranslateX(col * (Cdiameter + 5) + Cdiameter / 4);
			rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee26")));
			rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));
			final int column = col;
			rectangle.setOnMouseClicked(event -> {
				if (isAllowedtoInsert){
					isAllowedtoInsert = false;
					insertDisc(new Disc(isplay1),column);

				}
			});
			rectangleList.add(rectangle);
		}
		return rectangleList;
	}
	private void insertDisc(Disc disc,int column){
		int row = ROWS - 1;
		while (row >= 0) {
			if (getDiscIfPresent(row,column) == null)
				break;
			row--;
		}
		if (row < 0)
			return;

		insertedDiscsarr[row][column] = disc;
		insertedDiscspane.getChildren().add(disc);
		disc.setTranslateX(column * (Cdiameter + 5) + Cdiameter / 4);
		int currRow = row;
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5),disc);
		translateTransition.setToY(row * (Cdiameter + 5) + Cdiameter / 4);
		translateTransition.setOnFinished(event -> {
			isAllowedtoInsert = true;
			if (gameEnded(currRow,column)){
				gameOver();
			}
			isplay1 = !isplay1;
			playernamelabel.setText(isplay1 ? PlayOne : PlayTwo);
		});
		translateTransition.play();
	}
	private boolean gameEnded(int row,int column){
		List<Point2D> verticalPoints = IntStream.rangeClosed(row - 3,row + 3)
				.mapToObj(r -> new Point2D(r,column))
				.collect(Collectors.toList());
		List<Point2D> horizontalPoints = IntStream.rangeClosed(column - 3,column + 3)
				.mapToObj(col -> new Point2D(row,col))
				.collect(Collectors.toList());

		Point2D startPoint1 = new Point2D(row-3,column+3);
		List<Point2D> diagonal1points = IntStream.rangeClosed(0,6)
				.mapToObj(i ->startPoint1.add(i,-i))
				.collect(Collectors.toList());
		Point2D startPoint2 = new Point2D(row-3,column-3);
		List<Point2D> diagonal2points = IntStream.rangeClosed(0,6)
				.mapToObj(i ->startPoint2.add(i,i))
				.collect(Collectors.toList());


		boolean isEnded = checkCombinations(verticalPoints) || checkCombinations(horizontalPoints)
				|| checkCombinations(diagonal1points) || checkCombinations(diagonal2points);
		return isEnded;
	}
	private boolean checkCombinations(List<Point2D> points) {
		int chain = 0;
		for (Point2D point : points){
			int rowIndexForarr = (int) point.getX();
			int columnIndexForarr = (int) point.getY();
			Disc disc = getDiscIfPresent(rowIndexForarr,columnIndexForarr);
			if (disc != null && disc.isplay1move == isplay1){
				chain++;
				if (chain == 4){
					return true;
				}
			}else {
				chain = 0;
			}
		}
		return false;
	}
	private Disc getDiscIfPresent(int row,int column){
		if(row >= ROWS || row<0 || column >= COLUMNS || column < 0)
			return null;

		return insertedDiscsarr[row][column];

	}
	private void gameOver(){
		String winner = isplay1 ? PlayOne : PlayTwo;
		System.out.println("Winner Is : " + winner);
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Connect4");
		alert.setHeaderText("The Winner is " + winner);
		alert.setContentText("Want to Play Again ? ");
		ButtonType yesButton = new ButtonType("Yes");
		ButtonType noButton = new ButtonType("No,Exit");
		alert.getButtonTypes().setAll(yesButton,noButton);

		Platform.runLater(() -> {
			Optional<ButtonType> btnClicked = alert.showAndWait();
			if (btnClicked.isPresent() && btnClicked.get() == yesButton){
				resetGame();
			}else {
				Platform.exit();
				System.exit(0);
			}

		});
	}
	public void resetGame() {
		insertedDiscspane.getChildren().clear();
		for (int row = 0;row < insertedDiscsarr.length;row++){
			for (int col = 0;col < insertedDiscsarr[row].length;col++){
				insertedDiscsarr[row][col] = null;
			}
		}
		isplay1 = true;
		playernamelabel.setText(PlayOne);
		createplayground();
	}

	private static class Disc extends Circle{
		private final boolean isplay1move;
		public Disc(boolean isplay1move){
			this.isplay1move = isplay1move;
			setRadius(Cdiameter / 2);
			setFill(isplay1move ? Color.valueOf(color1) : Color.valueOf(color2));
			setCenterX(Cdiameter / 2);
			setCenterY(Cdiameter / 2);
		}
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		gamebutton.setOnAction(event -> game());

	}
	private void game(){
		PlayOne = field1.getText();
		PlayTwo = field2.getText();

	}

}