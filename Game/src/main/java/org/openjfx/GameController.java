package org.openjfx;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    public int random;


    @FXML
    public void initialize(){
        keyBoardEvent();
        setListener();
        setRandom();
        setCurrentWord();

    }

    @FXML
    private void switchToLobby() throws IOException {
        App.setRoot("lobby");
    }

    public void keyBoardEvent(){
        App.getScene().setOnKeyReleased(keyEvent -> {
            System.out.println(keyEvent.getCode());
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
        input.setDisable(true);
        keyBoardEvent();
        if(input.getText().toLowerCase().equals(Data.currentWord.toLowerCase() )){
            Data.playerWin();
            scorePlayer.setProgress(Data.scorePlayer/Data.maxScore);
        }else{
            Data.playerLose();
            scoreIA.setProgress(Data.scoreIa/Data.maxScore);
        }
        input.setText("");
        word.setText("");
        win();
        setRandom();
        setCurrentWord();
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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void setRandom(){
        random = Data.spamMin + (int)(Math.random() * (Data.spamMax -  Data.spamMin));
    }


}