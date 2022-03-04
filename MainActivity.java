package com.example.flashcardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView Question;
    private TextView Answer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView Question = findViewById(R.id.flashCardQuestion);
        TextView Answer = findViewById(R.id.answer);
        Answer.setVisibility(View.GONE);
        Question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Question.setVisibility(View.GONE);
                Answer.setVisibility(View.VISIBLE);
            }
        });
    }
}