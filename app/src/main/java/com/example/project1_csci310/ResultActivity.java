//create intent in main to jump to result page
//intent.putExtra to pass in time
package com.example.project1_csci310;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        final TextView resultView = (TextView) findViewById(R.id.resultView01);
        Intent intent = getIntent();
        String time = intent.getStringExtra("time");
        Boolean result = getIntent().getExtras().getBoolean("result");
//        String result = intent.getStringExtra("result");
        String status = "";
        if(result == true) {
            status = "You won!\nGood job.";
        }
        else {
            status = "You lost :(";
        }
        String message = "Used " + time + " seconds.\n" + status;
        resultView.setText(message);
    }


    public void onClickButton(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}