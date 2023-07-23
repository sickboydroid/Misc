package com.tangledbytes.studytimer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TimerController {
	final long STUDY_SESSION_DURATION = 5 * 60 * 1000; // = 5 minutes
	final long BREAK_SESSION_DURATION = 10 * 1000; // = 10 seconds
	//TODO: debugging
//	final long STUDY_SESSION_DURATION = 5 * 1000; // = 5 minutes
//	final long BREAK_SESSION_DURATION = 10 * 1000; // = 10 seconds

	StudyTimer timer;
	private long totalTimeFocused;
	private int sessionsCompleted;

	@FXML
	private Label labelCompletedSessions;

	@FXML
	private Label labelFocusedTime;

	@FXML
	private Label labelTime;

	public void initialize() {
		Platform.runLater(() -> labelTime.setTextFill(Color.GREEN));
		timer = new StudyTimer(labelTime);
	}

	@FXML
	void onClickResetTimer() {
		if (timer.isTimerRunning()) {
			totalTimeFocused += timer.getTimerEndTime() - System.currentTimeMillis();
			sessionsCompleted++;
			updateStatistics();
		}
		if (labelTime.getText().equals("Start"))
			labelTime.setText("Reset");
		timer.resetTimer(STUDY_SESSION_DURATION, true, runnableFiveMinuteTimerCompletion);
	}

	private void updateStatistics() {
		labelFocusedTime.setText((totalTimeFocused / 1000) + " Minutes");
		labelCompletedSessions.setText(String.valueOf(sessionsCompleted));
	}

	@FXML
	void onClickClose(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}


	Runnable runnableFiveMinuteTimerCompletion = new Runnable() {
		@Override
		public void run() {
			totalTimeFocused += STUDY_SESSION_DURATION;
			sessionsCompleted++;
			Platform.runLater(() -> {
				updateStatistics();
				labelTime.setTextFill(Color.RED);
			});
			timer.resetTimer(BREAK_SESSION_DURATION, false, runnableTenSecondTimerCompletion);
		}
	};

	Runnable runnableTenSecondTimerCompletion = new Runnable() {
		@Override
		public void run() {
			Platform.runLater(() -> labelTime.setTextFill(Color.GREEN));
			timer.resetTimer(STUDY_SESSION_DURATION, true, runnableFiveMinuteTimerCompletion);
		}
	};


}
