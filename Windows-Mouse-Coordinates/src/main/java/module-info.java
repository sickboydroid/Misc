module com.tangledbytes.mousecoordinates {
	requires javafx.controls;
	requires javafx.fxml;


	opens com.tangledbytes.mousecoordinates to javafx.fxml;
	exports com.tangledbytes.mousecoordinates;
}