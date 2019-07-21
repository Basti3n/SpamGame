package org.openjfx;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import org.global.Data;

public class GameController {

    @FXML
    public Label currentSpam;

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

    public Timer timer = new Timer();

    @FXML
    public void initialize(){
        keyBoardEvent();
        setListener();
        setRandom();
        setCurrentWord();
        setTimeRemaining();
    }

    @FXML
    private void switchToLobby() throws IOException {
        App.setRoot("lobby");
    }

    public void keyBoardEvent(){
        App.getScene().setOnKeyReleased(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.SPACE)
                currentSpam.setText(Integer.toString(Integer.parseInt(currentSpam.getText())+1));
        });
    }

    public void setListener(){
        currentSpam.textProperty().addListener((ov, t, t1) -> {
            if(currentSpam.getText().equals(Integer.toString(random))){
                currentSpam.setText("0");
                noKeyBoardEvent();
                word.setText(Data.currentWord);
                input.setDisable(false);
            }

        });
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
            input.setStyle("-fx-border-color: red");
            RotateTransition rt = new RotateTransition(Duration.millis(100),input);
            rt.setByAngle(5);
            rt.setFromAngle(0);
            rt.setCycleCount(4);
            rt.setAutoReverse(true);
            rt.play();
            rt.setOnFinished( (envent )-> input.setStyle("-fx-border-color: blue"));

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
            Data.timer = (long)(Data.currentWord.length()*0.4)+(long)random/2 + Data.currentHandicap;
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
                Thread.sleep(Data.timer*1000);
                if(leaveThread != 1){
                    System.out.println("You lose after "+Data.timer);
                    Platform.runLater(this::reset);
                    Platform.runLater( () ->   doesUserWon(false));
                    System.out.println("Thread stop");
                    t.interrupt();
                    leaveThread = 0;
                }
            } catch (InterruptedException e) {
                System.out.println("Thread stop");
                leaveThread=1;
                //e.printStackTrace();
            }
        });
        System.out.println("Thread start");
        t.start();

    }

    public void reset(){
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
        timeRemaining.setText(Long.toString(Data.timer));
        timer = new Timer();
        timer.schedule(new TimerTask() {
            int counter = 0;
            @Override
            public void run() {
                //call the method
                counter++;
                if (counter >= Data.timer+1){
                    timer.cancel();
                }
                if(!timeRemaining.getText().equals("0")) {
                    Platform.runLater(() -> timeRemaining.setText(Integer.toString(Integer.parseInt(timeRemaining.getText()) - 1)));

                }
            }
        }, 0, 1000);


    }


}