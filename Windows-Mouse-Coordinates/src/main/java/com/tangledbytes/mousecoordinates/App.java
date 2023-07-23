package com.tangledbytes.mousecoordinates;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class App extends Application {
	private static Stage stage;

	public static Stage getStage() {
		return stage;
	}

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws IOException {
		App.stage = stage;
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("coordinates-shower.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		stage.setTitle("Mouse Coordinates");
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setResizable(false);
		stage.setAlwaysOnTop(true);
		stage.setScene(scene);
		stage.show();
	}
}