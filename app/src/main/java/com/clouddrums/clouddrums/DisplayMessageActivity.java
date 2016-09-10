package com.clouddrums.clouddrums;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class DisplayMessageActivity extends AppCompatActivity {
int highScore = 0;
    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput("scores.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = openFileInput("scores.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView textView1 = new TextView(this);
        textView1.setTextSize(40);
        super.onCreate(savedInstanceState);
        if (readFromFile().equals("")){
            writeToFile("0");
        }

        highScore = Integer.parseInt(readFromFile());
        Intent intent1 = getIntent();
        String message1 = intent1.getStringExtra(MainActivity.EXTRAMESSAGE1);
        //if really high score
        if (Integer.parseInt(message1)>highScore) {
            highScore = Integer.parseInt(message1);
            writeToFile(Integer.toString(highScore));
        }

        textView1.setText ("Your speed is " + message1 + " words per minute."+
                "\n\n Your Highscore: "+highScore);
        textView1.setTextColor(getResources().getColor(R.color.textColor));
        setContentView(textView1);

    }
}
