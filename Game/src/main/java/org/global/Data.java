package org.global;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Data {
    public static int scorePlayer = 0;
    public static int scoreIa = 0;
    public static double maxScore = 10;
    public static ArrayList<String> dictionnary = new ArrayList<>();
    public static int spamMin = 10;
    public static int spamMax = 15;
    public static String currentWord;


    public static void resetScore(){
        scoreIa = 0;
        scorePlayer = 0;
    }

    public static void playerWin(){
        scorePlayer++;
    }

    public static void playerLose(){
        scoreIa++;
    }

    public static boolean hasPlayerWon(){
        if(scorePlayer == maxScore)
            return true;
        return false;
    }

    public static boolean hasIaWon(){
        if(scoreIa == maxScore)
            return true;
        return false;
    }

}
