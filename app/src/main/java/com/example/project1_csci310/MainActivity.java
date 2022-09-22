package com.example.project1_csci310;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private int clock = 0;
    private int flag_count = 4;
    private boolean running = true;
    private boolean mining = true;
    private boolean winning = true;
    private boolean playing = true;
    private int clicked_index;
    Set<Integer> set;
    private ArrayList<TextView> cell_tvs;

    private int dpToPixel(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cell_tvs = new ArrayList<TextView>();

        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout01);
        if (savedInstanceState != null) {
            clock = savedInstanceState.getInt("clock");
            running = savedInstanceState.getBoolean("running");
        }

        runTimer();

        // Method: add four dynamically created cells with LayoutInflater
        LayoutInflater li = LayoutInflater.from(this);
        for (int i = 0; i<10; i++) {
            for (int j=0; j<8; j++) {
                TextView tv = (TextView) li.inflate(R.layout.custom_cell_layout, grid, false);
                tv.setText("");
//                tv.setTextColor(Color.GREEN);
                tv.setBackgroundColor(Color.GREEN);
                tv.setOnClickListener(this::onClickTV);

                GridLayout.LayoutParams lp = (GridLayout.LayoutParams) tv.getLayoutParams();
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                grid.addView(tv, lp);

                cell_tvs.add(tv);
            }
        }
        placeMines(cell_tvs);
    }

    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<cell_tvs.size(); n++) {
            if (cell_tvs.get(n) == tv)
                return n;
        }
        return -1;
    }

    public void onClickTV(View view){
        TextView tv = (TextView) view;
        final TextView flagView = (TextView) findViewById(R.id.textView01);
        int n = findIndexOfCellTextView(tv);

        tv.setText(String.valueOf(n));
        clicked_index = n;
        tv.setTextColor(Color.GRAY);
        tv.setBackgroundColor(Color.LTGRAY);
        int num_mines = checkNeighboringCells(clicked_index);
        if(num_mines == 0 && mining && (tv.getText() != getString(R.string.mine))) {
            revealNeighboringCells(clicked_index);
        }

        if(set.contains(clicked_index)){
            cell_tvs.get(clicked_index).setText(getString(R.string.mine));
            winning = false;
            playing = false;
            running = false;
        }
        if(!mining) {
            if(flag_count == 1) {
                mining = true;
            }
            cell_tvs.get(clicked_index).setText(getString(R.string.flag));
            cell_tvs.get(clicked_index).setBackgroundColor(Color.GREEN);
            flag_count--;
            flagView.setText(String.valueOf(flag_count));
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("clock", clock);
        savedInstanceState.putBoolean("running", running);
    }

    public void onToggle(View view) {
        final TextView modeView = (TextView) findViewById(R.id.textView31);
        if(mining && (flag_count > 0)) {
            mining = false;
            modeView.setText(getString(R.string.flag));
        }
        else {
            mining = true;
            modeView.setText(getString(R.string.pick));
        }

    }

    private void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.textView03);
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = clock / 3600;
                int minutes = (clock % 3600) / 60;
                int seconds = clock % 60;
                String time = String.format("%d:%02d:%02d", hours, minutes, seconds);
                timeView.setText(time);

                if (running) {
                    clock++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private void placeMines(ArrayList<TextView> cell_tvs) {
        set = new HashSet<Integer>();
        Random random = new Random();
        while(set.size() < 4) {
            set.add(random.nextInt(80));
        }
        for (int i: set) {
            cell_tvs.get(i).setText(getString(R.string.mine));
        }
    }

    private int checkNeighboringCells(int index) {
//        CharSequence text =  cell_tvs.get(index).getText();
//        String mine = getString(R.string.mine);
        int mine_count = 0;
        if(set.contains(index)) {
            //end game
            cell_tvs.get(index).setText(getString(R.string.mine));
        }
        // top and bottom
        if(index > 7) {
            if(set.contains((index-8))) {
                mine_count++;
            }
        }
        if(index < 72) {
            if(set.contains((index+8))) {
                mine_count++;
            }
        }

        // - _ -
        if(index > 0 && ((index % 8) != 0)) {
            if(set.contains((index-1))) {
                mine_count++;
            }
        }
        if(index < 79 && ((index - 7) % 8 != 0)) {
            if(set.contains((index+1))) {
                mine_count++;
            }
        }

        // /
        if(index > 7 && ((index - 7) % 8 != 0)) {
            if(set.contains((index-7))) {
                mine_count++;
            }
        }
        if(index < 72 && (index % 8 != 0)) {
            if(set.contains((index+7))) {
                mine_count++;
            }
        }

        // \
        if(index > 7 && ((index % 8) != 0)) {
            if(set.contains((index-9))) {
                mine_count++;
            }
        }

        if(index < 72 && ((index - 7) % 8 != 0)) {
            if(set.contains((index+9))) {
                mine_count++;
            }
        }

        if(mine_count != 0) {
            cell_tvs.get(index).setText(String.valueOf(mine_count));
        }
        else {
            cell_tvs.get(index).setText("");
        }

        return mine_count;
    }

    private void revealNeighboringCells(int index) {
        if(index > 7) {
            cell_tvs.get((index - 8)).setBackgroundColor(Color.LTGRAY);
            cell_tvs.get((index - 8)).setTextColor(Color.GRAY);
            checkNeighboringCells(index-8);
        }
        if(index < 72) {
            cell_tvs.get((index + 8)).setBackgroundColor(Color.LTGRAY);
            cell_tvs.get((index + 8)).setTextColor(Color.GRAY);
            checkNeighboringCells(index+8);
        }

        // - _ -
        if(index > 0 && ((index % 8) != 0)) {
            cell_tvs.get((index - 1)).setBackgroundColor(Color.LTGRAY);
            cell_tvs.get((index - 1)).setTextColor(Color.GRAY);
            checkNeighboringCells(index-1);
        }
        if(index < 79 && ((index - 7) % 8 != 0)) {
            cell_tvs.get((index + 1)).setBackgroundColor(Color.LTGRAY);
            cell_tvs.get((index + 1)).setTextColor(Color.GRAY);
            checkNeighboringCells(index+1);
        }

        // /
        if(index > 7 && ((index - 7) % 8 != 0)) {
            cell_tvs.get((index - 7)).setBackgroundColor(Color.LTGRAY);
            cell_tvs.get((index - 7)).setTextColor(Color.GRAY);
            checkNeighboringCells(index-7);
        }
        if(index < 72 && (index % 8 != 0)) {
            cell_tvs.get((index + 7)).setBackgroundColor(Color.LTGRAY);
            cell_tvs.get((index + 7)).setTextColor(Color.GRAY);
            checkNeighboringCells(index+7);
        }

        // \
        if(index > 7 && (index % 8 != 0)) {
            cell_tvs.get((index - 9)).setBackgroundColor(Color.LTGRAY);
            cell_tvs.get((index - 9)).setTextColor(Color.GRAY);
            checkNeighboringCells(index-9);
        }

        if(index < 72 && ((index - 7) % 8 != 0)) {
            cell_tvs.get((index + 9)).setBackgroundColor(Color.LTGRAY);
            cell_tvs.get((index + 9)).setTextColor(Color.GRAY);
            checkNeighboringCells(index+9);
        }
    }
}

