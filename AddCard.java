package com.example.flashcardapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class AddCard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        ImageView cancelIcon = findViewById(R.id.cancel_icon);
        cancelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ImageView saveIcon = findViewById(R.id.save_icon);
        saveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                String userInputQuestion = ((EditText) findViewById(R.id.userQuestion)).getText().toString();
                String userInputAnswer = ((EditText) findViewById(R.id.userAnswer)).getText().toString();
                data.putExtra("QUESTION_KEY", userInputQuestion);
                data.putExtra("ANSWER_KEY", userInputAnswer);
                setResult(RESULT_OK, data);
                finish();
            }
        });

    }
}