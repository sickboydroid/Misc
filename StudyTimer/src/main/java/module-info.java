module com.tangledbytes.studytimer {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.media;


	opens com.tangledbytes.studytimer to javafx.fxml;
	exports com.tangledbytes.studytimer;
}