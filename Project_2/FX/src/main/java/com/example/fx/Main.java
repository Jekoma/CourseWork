package com.example.fx;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.scene.shape.Box;
import javafx.embed.swing.SwingFXUtils;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main extends Application {
    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;


    // NEW NEW NEW NEW NEW
    private FlowPane topPane;
    private BorderPane webCamPane;
    private ImageView imgWebCamCapturedImage;
    private Webcam webCam = null;
    private boolean stopCamera = false;
    private FlowPane bottomCameraControlPane;
    private Button btnCameraStop;
    private Button btnCameraStart;
    private BufferedImage grabbedImage;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
    // NEW NEW NEW NEW NEW


    @Override
    public void start(Stage stage) throws IOException {
        /*FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();*/

        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        WebcamPanel webcamPanel = new WebcamPanel(webcam);
        webcamPanel.setImageSizeDisplayed(true);
        webcamPanel.setFPSDisplayed(true);
        webcamPanel.setMirrored(true);
        webcamPanel.setDisplayDebugInfo(true);

        JFrame frame = new JFrame();
        frame.add(webcamPanel);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);



        Box box = prepareBox();
        SmartGroup group = new SmartGroup();
        group.getChildren().add(box);

        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(group,WIDTH, HEIGHT);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        group.translateXProperty().set(WIDTH / 2);
        group.translateYProperty().set(HEIGHT / 2);
        group.translateZProperty().set(-1200);

        stage.addEventHandler(KeyEvent.KEY_PRESSED, event ->{
            switch (event.getCode()) {
                case W:
                    group.translateZProperty().set(group.getTranslateZ() + 100);
                    break;
                case S:
                    group.translateZProperty().set(group.getTranslateZ() - 100);
                    break;
                case NUMPAD2:
                    group.rotateByX(10);
                    break;
                case NUMPAD8:
                    group.rotateByX(-10);
                    break;
                case NUMPAD4:
                    group.rotateByY(10);
                    break;
                case NUMPAD6:
                    group.rotateByY(-10);
                    break;
            }
        });


        // NEW NEW NEW NEW NEW

        BorderPane root = new BorderPane();
        topPane = new FlowPane();
        topPane.setAlignment(Pos.CENTER);
        topPane.setHgap(20);
        topPane.setOrientation(Orientation.HORIZONTAL);
        topPane.setPrefHeight(40);
        root.setTop(topPane);
        // d
//        group.getChildren().add(topPane);

        createTopPanel();

        webCamPane = new BorderPane();
        webCamPane.setStyle("-fx-background-color: #ccc;");
        webCamPane.setPrefSize(500, 600);
        imgWebCamCapturedImage = new ImageView();
        webCamPane.setCenter(imgWebCamCapturedImage);
        root.setCenter(webCamPane);
        // d
//        group.getChildren().add(webCamPane);

        bottomCameraControlPane = new FlowPane();
        bottomCameraControlPane.setOrientation(Orientation.HORIZONTAL);
        bottomCameraControlPane.setAlignment(Pos.CENTER);
        bottomCameraControlPane.setDisable(true);
        createCameraControls();
        root.setBottom(bottomCameraControlPane);
        // d
//        group.getChildren().add(bottomCameraControlPane);

        FlowPane leftPane = create3DPane(imageProperty);
        root.setLeft(leftPane);
        // d
//        group.getChildren().add(leftPane);


        Platform.runLater(this::setImageViewSize);
        // NEW NEW NEW NEW NEW

        /*stage.setTitle("JavaFX 3D Web Camera TV");
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();*/

        stage.setTitle("JavaFX 3D Web Camera TV");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

    // NEW NEW NEW NEW
    private FlowPane create3DPane(ObjectProperty<Image> imageProperty){
        return new JavaFX3DWorld(imageProperty).get3DWorldPane();
    }

    private void createCameraControls() {
        btnCameraStop = new Button();
        btnCameraStop.setOnAction(arg0 -> stopWebCamCamera());
        btnCameraStop.setText("Stop Camera");
        btnCameraStart = new Button();
        btnCameraStart.setOnAction(arg0 -> startWebCamCamera());
        btnCameraStart.setText("Start Camera");
        /*Button btnCameraDispose = new Button();
        btnCameraDispose.setText("Dispose Camera");
        btnCameraDispose.setOnAction(arg0 -> disposeWebCamCamera());*/
        bottomCameraControlPane.getChildren().add(btnCameraStart);
        bottomCameraControlPane.getChildren().add(btnCameraStop);/*
        bottomCameraControlPane.getChildren().add(btnCameraDispose);*/
    }

    private void setImageViewSize() {
        double height = webCamPane.getHeight();
        double width  = webCamPane.getWidth();
        imgWebCamCapturedImage.setFitHeight(height);
        imgWebCamCapturedImage.setFitWidth(width);
        imgWebCamCapturedImage.prefHeight(height);
        imgWebCamCapturedImage.prefWidth(width);
        imgWebCamCapturedImage.setPreserveRatio(true);
    }

    private void createTopPanel() {
        int webCamCounter = 0;
        Label lbInfoLabel = new Label("Select Your WebCam Camera");
        ObservableList<WebCamInfo> options = FXCollections.observableArrayList();

        topPane.getChildren().add(lbInfoLabel);
        for(Webcam webcam:Webcam.getWebcams()) {
            WebCamInfo webCamInfo = new WebCamInfo();
            webCamInfo.setWebCamIndex(webCamCounter);
            webCamInfo.setWebCamName(webcam.getName());
            options.add(webCamInfo);
            webCamCounter ++;
        }
        ComboBox<WebCamInfo> cameraOptions = new ComboBox<>();
        cameraOptions.setItems(options);
        String cameraListPromptText = "Choose Camera";
        cameraOptions.setPromptText(cameraListPromptText);
        cameraOptions.getSelectionModel().selectedItemProperty().addListener((arg0, arg1, arg2) -> {
            if (arg2 != null) {
//                System.out.println("WebCam Index: " + arg2.getWebCamIndex()+": WebCam Name:"+ arg2.getWebCamName());
                initializeWebCam(arg2.getWebCamIndex());
            }
        });
        topPane.getChildren().add(cameraOptions);
    }
    private void startWebCamStream() {
        stopCamera  = false;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!stopCamera) {
                    try {
                        if ((grabbedImage = webCam.getImage()) != null) {
//                        System.out.println("Captured Image height * width: " + grabbedImage.getWidth() + " * " + grabbedImage.getHeight());
                            Platform.runLater(() -> {
                                final Image mainimage = SwingFXUtils.toFXImage(grabbedImage, null);
                                imageProperty.set(mainimage);
                            });
                            grabbedImage.flush();
                        }
                    } catch (Exception ignored) {
                    }
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        imgWebCamCapturedImage.imageProperty().bind(imageProperty);
    }

    class WebCamInfo {
        private String webCamName ;
        private int webCamIndex ;

//        String getWebCamName() {
//            return webCamName;
//        }

        void setWebCamName(String webCamName) {
            this.webCamName = webCamName;
        }
        int getWebCamIndex() {
            return webCamIndex;
        }
        void setWebCamIndex(int webCamIndex) {
            this.webCamIndex = webCamIndex;
        }

        @Override
        public String toString() {
            return webCamName;
        }
    }

    private void initializeWebCam(final int webCamIndex) {
        Task<Void> webCamTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (webCam != null) {
                    /*disposeWebCamCamera();*/
                    webCam = Webcam.getWebcams().get(webCamIndex);
                    webCam.open();
                } else {
                    webCam = Webcam.getWebcams().get(webCamIndex);
                    webCam.open();
                }
                startWebCamStream();
                return null;
            }
        };

        Thread webCamThread = new Thread(webCamTask);
        webCamThread.setDaemon(true);
        webCamThread.start();
        bottomCameraControlPane.setDisable(false);
        btnCameraStart.setDisable(true);
    }
    /*private void disposeWebCamCamera() {
        stopCamera = true;
        webCam.close();
        btnCameraStart.setDisable(true);
        btnCameraStop.setDisable(true);
    }*/
    private void startWebCamCamera() {
        stopCamera = false;
        startWebCamStream();
        btnCameraStop.setDisable(false);
        btnCameraStart.setDisable(true);
    }
    private void stopWebCamCamera() {
        stopCamera = true;
        btnCameraStart.setDisable(false);
        btnCameraStop.setDisable(true);
    }

    // NEW NEW NEW NEW

    private Box prepareBox() {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image("D:\\Java\\AAA\\Project_2\\FX\\src\\main\\resources\\тиранда.jpg"));
        Box box = new Box(100,100,100);
        box.setMaterial(material);
        return box;
    }

    static class SmartGroup extends Group {
        Rotate r;
        Transform t = new Rotate();

        void rotateByX(int ang) {
            r = new Rotate(ang, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        void rotateByY(int ang) {
            r = new Rotate(ang, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
    }
}
