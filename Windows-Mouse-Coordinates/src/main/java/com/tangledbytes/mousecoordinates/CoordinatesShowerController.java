package com.tangledbytes.mousecoordinates;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.robot.Robot;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class CoordinatesShowerController implements Initializable {
	private final Robot robot = new Robot();
	private Stage stage;
	@FXML
	private Label labelCoordinates;
	@FXML
	private AnchorPane anchorPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		stage = App.getStage();
		labelCoordinates.setOnMouseClicked(event -> {
			Clipboard clipboard = Clipboard.getSystemClipboard();
			ClipboardContent content = new ClipboardContent();
			content.putString(labelCoordinates.getText());
			clipboard.setContent(content);
		});


		Thread mouseFollower = new Thread(new MouseFollowerRunnable());
		mouseFollower.setDaemon(true);
		mouseFollower.start();
	}

	private class MouseFollowerRunnable implements Runnable {
		@Override
		public void run() {
			DecimalFormat formatter = new DecimalFormat("#####.#");
			double maxX = Screen.getPrimary().getVisualBounds().getMaxX();
			double maxY = Screen.getPrimary().getVisualBounds().getMaxY();
			double marginX = 20;
			double marginY = 20;
			while (true) {
				Platform.runLater(() -> {
					double mouseX = robot.getMouseX();
					double mouseY = robot.getMouseY();
					labelCoordinates.setText(String.format("%s, %s", formatter.format(mouseX), formatter.format(mouseY)));
					if ((mouseX + marginX) >= maxX)
						stage.setX(maxX - stage.getWidth() - marginX);
					else
						stage.setX(mouseX + marginX);
					if ((mouseY + marginY) >= maxY)
						stage.setY(maxY - stage.getHeight() - marginY);
					else
						stage.setY(robot.getMouseY() + marginY);
				});
				sleep(20);
			}
		}

		private void sleep(long delayMillis) {
			try {
				Thread.sleep(delayMillis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
