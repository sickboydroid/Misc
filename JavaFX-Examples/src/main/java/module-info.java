module com.tangledbytes.javafxcollectionsexample {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tangledbytes.collectionsexample to javafx.fxml;
    exports com.tangledbytes.collectionsexample;
}