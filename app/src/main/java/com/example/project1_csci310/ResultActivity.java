//create intent in main to jump to result page
//intent.putExtra to pass in time
package com.example.project1_csci310;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
    }
}