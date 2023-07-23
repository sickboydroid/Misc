package com.tangledbytes.studytimer;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class StudyTimer {
	volatile long timerEndTime = -1;
	volatile long timerStartTime = -1;
	Label labelTimeShower;
	boolean playSoundOnCompletion;
	Runnable completionRunnable;
	volatile boolean timerRunning;

	public StudyTimer(Label labelTimeShower) {
		this.labelTimeShower = labelTimeShower;
	}

	public void resetTimer(long durationMillis, boolean playSoundOnCompletion, Runnable completionRunnable) {
		timerEndTime = -1;
		while (timerRunning) Thread.onSpinWait();
		timerStartTime = System.currentTimeMillis();
		timerEndTime = timerStartTime + durationMillis;
		this.playSoundOnCompletion = playSoundOnCompletion;
		this.completionRunnable = completionRunnable;
		startTimer();
	}

	private void startTimer() {
		Timer timer = new Timer("Study Timer", true);
		timerRunning = true;
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (System.currentTimeMillis() >= timerEndTime) {
					timer.cancel();
					timerRunning = false;
					if (timerEndTime == -1) {
						// forcefully stopped
						return;
					}
					setLabelTime(0, 0, 0);
					onTimerEnded();
					return;
				}
				long remainingTime = timerEndTime - System.currentTimeMillis();
				long minutes = (remainingTime / 1000) / 60;
				long seconds = (remainingTime / 1000) % 60;
				long millis = remainingTime % 1000;
				setLabelTime(minutes, seconds, millis);
			}
		}, 5, 5);
	}

	private void onTimerEnded() {
		if (playSoundOnCompletion)
			playCompletionSound();
		if (completionRunnable != null) {
			completionRunnable.run();
		}
	}

	public long getTimerEndTime() {
		return timerEndTime;
	}

	public long getTimerStartTime() {
		return timerEndTime;
	}

	public boolean isTimerRunning() {
		return timerRunning;
	}

	public void setLabelTime(long minutes, long seconds, long millis) {
		DecimalFormat formatMinutes = new DecimalFormat("00");
		DecimalFormat formatSeconds = new DecimalFormat("00");
		DecimalFormat formatMillis = new DecimalFormat("000");
		String remainingTime = String.format("%s:%s:%s",
				formatMinutes.format(minutes), formatSeconds.format(seconds), formatMillis.format(millis));
		Platform.runLater(() -> labelTimeShower.setText(remainingTime));
	}

	public void playCompletionSound() {
		try {
			Media media = new Media(StudyTimer.class.getResource("timer-ended.mp3").toURI().toString());
			MediaPlayer player = new MediaPlayer(media);
			player.setAutoPlay(true);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
