package com.dnerd.dipty.dicee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Button mRollButton;
    private ImageView mLeftDice;
    private ImageView mRightDice;
    private int[] mDiceArray = new int[] {
            R.drawable.dice1,
            R.drawable.dice2,
            R.drawable.dice3,
            R.drawable.dice4,
            R.drawable.dice5,
            R.drawable.dice6};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRollButton = findViewById(R.id.roll_button);
        mLeftDice = findViewById(R.id.image_left_dice);
        mRightDice = findViewById(R.id.image_right_dice);

        mRollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create a random number generator
                Random randomNumberGenerator = new Random();

                // Make the random number generator spit out a new random number
                int number = randomNumberGenerator.nextInt(6);

                // Print this random number to the logcat to see it in the Android monitor
                Log.d("Dicee", "The number is " + number );

                // grab a random dice image from the diceArray. Then tell the ImageView to display
                // this image
                mLeftDice.setImageResource(mDiceArray[number]);

                // Create a new random number
                number = randomNumberGenerator.nextInt(6);

                // Print this random number to the logcat to see it in the Android monitor
                Log.d("Dicee", "The number is " + number );

                // Set the right dice image using an image from the diceArray.
                mRightDice.setImageResource(mDiceArray[number]);

            }
        });

    }
}
