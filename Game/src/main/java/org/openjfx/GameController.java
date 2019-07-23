package org.openjfx;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import org.global.Data;

public class GameController {

//    @FXML
//    public Label currentSpam;

    public int currentSpam = 0;

    @FXML
    public TextField input;

    @FXML
    public ProgressBar scorePlayer;

    @FXML
    public ProgressBar scoreIA;

    @FXML
    public Label word;

    @FXML
    public Label timeRemaining;

    public int random;

    public Thread t;

    public int leaveThread = 0;

    public int x = 0;
    public int y = 0;

    public static ScheduledThreadPoolExecutor timer = new ScheduledThreadPoolExecutor(1);
    static ScheduledFuture<?> timerFutur;


    @FXML
    public void initialize(){
        keyBoardEvent();
        setListener();
        setRandom();
        setCurrentWord();
        setTimeRemaining();
        App.getScene().getWindow().setOnCloseRequest(event -> {
            System.out.println("oook");
            exitApplication();
        });
    }

    @FXML
    private void switchToLobby() throws IOException {
        App.setRoot("lobby");
    }

    public void keyBoardEvent(){
        App.getScene().setOnKeyReleased(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.SPACE){
                currentSpam++;
                shakeStage();
                word.setText(Integer.toString(currentSpam));
                if(currentSpam == random){
                    currentSpam = 0;
                    noKeyBoardEvent();
                    word.setText(Data.currentWord);
                    input.setDisable(false);
                }
            }


//              currentSpam.setText(Integer.toString(Integer.parseInt(currentSpam.getText())+1));

        });
    }

    public void setListener(){
//        currentSpam.textProperty().addListener((ov, t, t1) -> {
//            if(currentSpam.getText().equals(Integer.toString(random))){
//                currentSpam.setText("0");
//                noKeyBoardEvent();
//                word.setText(Data.currentWord);
//                input.setDisable(false);
//            }
//
//        });
    }

    public void noKeyBoardEvent(){
        App.getScene().setOnKeyReleased(keyEvent -> {

        });
    }

    @FXML
    public void removeSpace(){
        input.setText(input.getText().replaceAll("\\s+",""));
        input.positionCaret(input.getText().length());
    }

    @FXML
    public void compareWord() throws IOException {
        if(input.getText().toLowerCase().equals(Data.currentWord.toLowerCase() )){
            doesUserWon(true);
        }else{
            doesUserWon(false);
            wrongInput();

        }
        win();
        reset();
    }

    public void win() throws IOException {
        if(Data.hasPlayerWon()){
            System.out.println("Player win");
            switchToLobby();
        }

        else if(Data.hasIaWon()){
            System.out.println("IA win");
            switchToLobby();
        }
    }

    public void setCurrentWord(){
        try {
            Data.currentWord = new String(Data.dictionnary.get(new Random().nextInt(Data.dictionnary.size())).getBytes(),"UTF-8");
            Data.timer = (long)(Data.currentWord.length()*0.4)+(long)random/4 + Data.currentHandicap;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        setTimerThread();
    }

    public void setRandom(){
        random = Data.spamMin + (int)(Math.random() * (Data.spamMax -  Data.spamMin));
    }

    public void setTimerThread(){
        t = new Thread(() -> {
            try {
                Thread.sleep((long)Data.timer*1000);
                if(leaveThread != 1){
                    System.out.println("You lose after "+Data.timer);
                    Platform.runLater(this::reset);
                    Platform.runLater( () ->   doesUserWon(false));
                    t.interrupt();
                    leaveThread = 0;
                }
            } catch (InterruptedException e) {
                leaveThread=1;
            }
        });
        t.start();

    }

    public void reset(){
        timerFutur.cancel(false);
        t.interrupt();
        input.setDisable(true);
        keyBoardEvent();
        input.setText("");
        word.setText("");
        setRandom();
        setCurrentWord();
        setTimeRemaining();


    }

    public void doesUserWon(Boolean b){
        if(b){
            Data.playerWin();
            scorePlayer.setProgress(Data.scorePlayer/Data.maxScore);
        }else{
            Data.playerLose();
            scoreIA.setProgress(Data.scoreIa/Data.maxScore);
        }
    }

    public void setTimeRemaining(){
        timeRemaining.setText(String.format("%.2f",Data.timer));
        Runnable createValue = (() -> {
            if(Double.parseDouble(timeRemaining.getText().replace(",",".")) < 3.20){
                timeRemaining.setVisible(false);
            }
            if( !timeRemaining.getText().equals("0,20") ) {
                Platform.runLater(() -> timeRemaining.setText(String.format("%.2f",Double.parseDouble(timeRemaining.getText().replace(",",".")) - 0.20)));
            }
            else{
                System.out.println("You lose after "+Data.timer);
                Platform.runLater(this::reset);
                Platform.runLater( () ->   doesUserWon(false));
                timeRemaining.setVisible(true);
            }
        } );
        timerFutur = timer.scheduleAtFixedRate(createValue,0,200, TimeUnit.MILLISECONDS);
    }

    public void exitApplication() {
        timerFutur.cancel(false);
        timer.shutdownNow();
        t.interrupt();
        Platform.exit();
    }

    public void wrongInput(){
        input.setStyle("-fx-border-color: red");
        RotateTransition rt = new RotateTransition(Duration.millis(100),input);
        rt.setByAngle(5);
        rt.setFromAngle(0);
        rt.setCycleCount(4);
        rt.setAutoReverse(true);
        rt.play();
        rt.setOnFinished( (envent )-> input.setStyle("-fx-border-color: blue"));
    }

    public void shakeStage() {
        Timeline timelineX = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            if (x == 0) {
                App.getScene().getWindow().setX(App.getScene().getWindow().getX() + 10);
                x = 1;
            } else {
                App.getScene().getWindow().setX(App.getScene().getWindow().getX() - 10);
                x = 0;
            }
            if (y == 0) {
                App.getScene().getWindow().setY(App.getScene().getWindow().getY() + 10);
                y = 1;
            } else {
                App.getScene().getWindow().setY(App.getScene().getWindow().getY() - 10);
                y = 0;
            }
        }));

        timelineX.setCycleCount(20);
        timelineX.setAutoReverse(true);
        timelineX.play();
    }

}