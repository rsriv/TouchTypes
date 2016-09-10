package com.clouddrums.clouddrums;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public int valid = 0;
    public int gamecount = 0;
    public int scoreInt;
    public int currentTime;
    public final static String EXTRAMESSAGE1 = "";
    public String word = "abc";
    public int start;
    public int complete = 0;
    public Thread t;

    public String[] words = {
            

                            };
    public int numWords = words.length;

    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public void newGame(View view) {
        start = 0;
        complete = 0;
        scoreInt = 0;
        TextView score = (TextView) findViewById(R.id.textView);
        score.setText(Integer.toString(scoreInt));
        TextView currentWord = (TextView) findViewById(R.id.currentWord);
        currentWord.setText("");
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setText("Play");
        valid = 0;
        if (gamecount!=0) t.interrupt();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText ansField = (EditText) findViewById(R.id.ansField);

                            ansField.setOnEditorActionListener(new TextView.OnEditorActionListener()
                        {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
                            {
                                if(actionId== EditorInfo.IME_ACTION_DONE)
                                {
                                    enter(v);
                                        return true; // close the keyboard
                                }
                                return true;
                            }
                        });


    }

    public void enter(View view) {
        EditText ansField = (EditText) findViewById(R.id.ansField);
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(ansField, InputMethodManager.SHOW_IMPLICIT);
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setText("Enter");
        final TextView time = (TextView) findViewById(R.id.time);
        if (valid == 0) {
            start = (int) System.currentTimeMillis();
            scoreInt = 0;
            complete = 0;
            gamecount++;
        }
        currentTime = (int) System.currentTimeMillis();
        currentTime = (currentTime - start) / 1000;
        imm.showSoftInput(ansField, InputMethodManager.SHOW_IMPLICIT);
        if (valid == 0){
            t = new Thread(new Runnable() {
                public void run() {
                    int i = 60;
                    while (i>=0 && valid ==1){
                        final int p = i;
                        time.post(new Runnable() {
                            public void run() {
                                int j = p;
                                String timeStr = Integer.toString(j);
                                time.setText(timeStr);
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException e){

                        }
                        i--;
                    }
                    if (complete == 0) {
                        time.post(new Runnable() {
                            public void run() {
                                String timeStr = Integer.toString(60);
                                time.setText(timeStr);
                            }
                        });
                    }
                    else {
                        time.post(new Runnable() {
                            public void run() {
                                String timeStr = Integer.toString(0);
                                time.setText(timeStr);
                            }
                        });
                    }
                    t.interrupt();
                }
            });
            t.start();
            valid = 1;
        }
        imm.showSoftInput(ansField, InputMethodManager.SHOW_IMPLICIT);
        TextView currentWord = (TextView) findViewById(R.id.currentWord);
        currentWord.setText(word);


        String textInput = ansField.getText().toString();

        TextView score = (TextView) findViewById(R.id.textView);
        String scoreString = score.getText().toString();

        if (textInput.equals(word)) {
            scoreInt = Integer.parseInt(scoreString);
            scoreInt++;
            score.setText(Integer.toString(scoreInt));

        }

        word = words[randInt(0, numWords - 1)];
        currentWord = (TextView) findViewById(R.id.currentWord);
        currentWord.setText(word);
        ansField.setText("");

        if (currentTime >= 60) {
            complete = 1;
            Intent scoreScreen = new Intent(this, DisplayMessageActivity.class);
            scoreScreen.putExtra(EXTRAMESSAGE1, scoreString);
            startActivity(scoreScreen);
        }

        imm.showSoftInput(ansField, InputMethodManager.SHOW_IMPLICIT);
        ansField.requestFocus();
    }



}
