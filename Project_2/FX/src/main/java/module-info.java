module com.example.fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;
    requires webcam.capture;
    requires javafx.swing;


    opens com.example.fx to javafx.fxml;
    exports com.example.fx;
}