package com.example.flashcardapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView Question;
    TextView Answer;

    FlashcardDatabase flashCardDataBase;
    List<Flashcard> allFlashCard;
    int cardNumber=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Question = findViewById(R.id.flashCardQuestion);
        Answer = findViewById(R.id.answer);

        Answer.setVisibility(View.INVISIBLE);
        Question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cx = Question.getWidth() / 2;
                int cy = Answer.getHeight() / 2;

                // get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);

                // create the animator for this view (the start radius is zero)
                Animator anim = ViewAnimationUtils.createCircularReveal(Answer, cx, cy, 0f, finalRadius);

                // hide the question and show the answer to prepare for playing the animation!
               Question .setVisibility(View.INVISIBLE);
                Answer.setVisibility(View.VISIBLE);

                anim.setDuration(3000);
                anim.start();

            }
        });
        Answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Answer.setVisibility(View.INVISIBLE);
                Question.setVisibility(View.VISIBLE);
            }
        });
        ImageView addIcon = findViewById(R.id.add_icon);
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCard.class);
                startActivityForResult(intent, 100);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        flashCardDataBase = new FlashcardDatabase(getApplicationContext());
        allFlashCard = flashCardDataBase.getAllCards();

        if(allFlashCard != null && allFlashCard.size()>0) {
            Flashcard firstCard = allFlashCard.get(0);
            Question.setText(firstCard.getQuestion());
            Answer.setText(firstCard.getAnswer());

        }
        ImageView nextIcon = findViewById(R.id.next_icon);
        nextIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardNumber += 1;
                if (cardNumber >= allFlashCard.size()) {
                    Snackbar.make(view, "you have reached" +
                            " the end of the flash card.", Snackbar.LENGTH_SHORT)
                            .show();
                    cardNumber = 0; //reset the card index.
                }
                if(allFlashCard.isEmpty()) {
                Snackbar.make(view,"you don't have any flash cads.",
                        Snackbar.LENGTH_SHORT)
                        .show();

                }

                final Animation leftOutAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.left_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.right_in);
                Question.startAnimation(leftOutAnim);
                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // this method is called when the animation first starts
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        Flashcard currentCard = allFlashCard.get(cardNumber);
                        Question.setText(currentCard.getQuestion());
                        Answer.setText(currentCard.getAnswer());
                        Question.setVisibility(View.VISIBLE);
                        Answer.setVisibility(View.INVISIBLE);
                        Question.startAnimation(rightInAnim);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // we don't need to worry about this method
                    }
                });

            }
        });
        ImageView backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardNumber -=1;
                if(cardNumber<=-1){
                    Snackbar.make(view, "you have reached" +
                            " the end of the flash card.", Snackbar.LENGTH_SHORT)
                            .show();
                    cardNumber = allFlashCard.size()-1; //reset the card index.
                }
                if(allFlashCard.isEmpty()) {
                    Snackbar.make(view,"you don't have any flash cads.",
                            Snackbar.LENGTH_SHORT)
                            .show();

                }

                final Animation rightOutAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.right_out);
                final Animation leftInAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.left_in);

                rightOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // this method is called when the animation first starts
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                       Question.startAnimation(leftInAnim);
                        Flashcard currentCard = allFlashCard.get(cardNumber);
                        Question.setText(currentCard.getQuestion());
                        Answer.setText(currentCard.getAnswer());
                        Question.setVisibility(View.VISIBLE);
                        Answer.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // we don't need to worry about this method
                    }
                });
                Question.startAnimation(rightOutAnim);
            }
        });
        ImageView deleteIcon = findViewById(R.id.delete_icon);
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashCardDataBase.deleteCard(Question.getText().toString());
                allFlashCard = flashCardDataBase.getAllCards();
                if (allFlashCard.isEmpty()) {
                    Question.setText("Empty \n click on + button to add a new flash card");

                } else {
                    cardNumber--;

                    if (cardNumber == -1) {
                        cardNumber = allFlashCard.size() - 1;
                    }

                    Flashcard flashcard = allFlashCard.get(cardNumber);
                    Question.setText(flashcard.getQuestion());
                    Answer.setText(flashcard.getAnswer());

                }
            }

        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data != null) {
                String questionString = data.getExtras().getString("QUESTION_KEY");
                String answerString = data.getExtras().getString("ANSWER_KEY");
                Question.setText(questionString);
                Answer.setText(answerString);

                Flashcard flashcard = new Flashcard(questionString,answerString);
                flashCardDataBase.insertCard(flashcard);
                allFlashCard = flashCardDataBase.getAllCards();

            }
        }
    }


}