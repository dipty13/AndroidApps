package com.dnerd.dipty.courtcounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int mScoreTeamA = 0;
    private int mScoreTeamB = 0;
    private  TextView mScoreViewTeamA,mScoreViewTeamB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScoreViewTeamA = findViewById(R.id.team_a_score);
        mScoreViewTeamB = findViewById(R.id.team_b_score);

    }

    public void displayForTeamA(int score)
    {
        mScoreViewTeamA.setText(score+"");
    }

    public void displayForTeamB(int score)
    {
        mScoreViewTeamB.setText(score+"");
    }

    public void addThreeToTeamA(View view) {
        mScoreTeamA += 3;
        displayForTeamA(mScoreTeamA);
    }

    public void addTwoToTeamA(View view) {
        mScoreTeamA += 2;
        displayForTeamA(mScoreTeamA);
    }

    public void addOneToTeamA(View view) {
        mScoreTeamA++;
        displayForTeamA(mScoreTeamA);
    }

    public void addThreeToTeamB(View view) {
        mScoreTeamB += 3;
        displayForTeamB(mScoreTeamB);
    }

    public void addTwoToTeamB(View view) {
        mScoreTeamB += 2;
        displayForTeamB(mScoreTeamB);
    }

    public void addOneToTeamB(View view) {
        mScoreTeamB++;
        displayForTeamB(mScoreTeamB);
    }

    public void reset(View view) {

        mScoreTeamA = 0;
        mScoreTeamB = 0;

        displayForTeamA(mScoreTeamA);
        displayForTeamB(mScoreTeamB);
    }
}
