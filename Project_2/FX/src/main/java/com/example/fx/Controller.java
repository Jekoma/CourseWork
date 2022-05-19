package com.example.fx;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

public class Controller implements Initializable {
    @FXML
    private MediaView mediaView;
    @FXML
    private MediaView mediaView_two;
    @FXML
    private Button playButton, pauseButton, resetButton;

    private File file, sec_file;
    private Media media, sec_media;
    private MediaPlayer mediaPlayer, sec_mediaPlayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        file = new File("Videos/saved_2.mp4");
        sec_file = new File("Videos/saved_1.mp4");

        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        sec_media = new Media(sec_file.toURI().toString());
        sec_mediaPlayer = new MediaPlayer(sec_media);
        mediaView_two.setMediaPlayer(sec_mediaPlayer);

        /*Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        WebcamPanel webcamPanel = new WebcamPanel(webcam);
        webcamPanel.setImageSizeDisplayed(true);
        webcamPanel.setFPSDisplayed(true);
        webcamPanel.setMirrored(true);
        webcamPanel.setDisplayDebugInfo(true);*/

        /*JFrame frame = new JFrame();
        frame.add(webcamPanel);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);*/
    }

    public void playMedia() {
        mediaPlayer.play();
        sec_mediaPlayer.play();
    }
    public void pauseMedia() {
        mediaPlayer.pause();
        sec_mediaPlayer.pause();
    }
    public void resetMedia() {
        if(mediaPlayer.getStatus() != MediaPlayer.Status.READY) {
            mediaPlayer.seek(Duration.seconds(0.0));
        }
        if(sec_mediaPlayer.getStatus() != MediaPlayer.Status.READY) {
            sec_mediaPlayer.seek(Duration.seconds(0.0));
        }
    }
}
