package com.tangledbytes.studytimer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class App extends Application {
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("timer.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		stage.setTitle("Timer");
		stage.setResizable(false);
		stage.setAlwaysOnTop(true);
		stage.setScene(scene);
		stage.show();
	}
}