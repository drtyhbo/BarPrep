package com.drtyhbo.barprep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.drtyhbo.barprep.db.BarPrepDatabase;
import com.drtyhbo.barprep.db.Answer;
import com.drtyhbo.barprep.db.Question;
import com.drtyhbo.barprep.res.ResAnswer;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class BarPrepActivity extends Activity {
	private Question currentQuestion;
	private ScrollView currentView;
	private BarPrepDatabase db;
	private List<Button> buttons;
	
    private final OnClickListener onAnswerButtonClick = new OnClickListener() {
		public void onClick(View v) {
			Answer myAnswer = (Answer)v.getTag();
			db.saveAnswer(currentQuestion, myAnswer);

	    	for (Iterator<Button> i = buttons.iterator(); i.hasNext();) {
	    		Button button = i.next();
	    		button.setEnabled(false);

	    		Answer answer = (Answer)button.getTag();
	    		if (button == (Button)v || answer.isCorrect()) {
	    			button.getBackground().setColorFilter(answer.isCorrect()
						? 0xFF00FF00 : 0xFFFF0000, PorterDuff.Mode.MULTIPLY);
	    		}
	    	}

			renderExplanation(myAnswer);
		}
	};
    private final OnClickListener onNextQuestionButtonClick = new OnClickListener() {
		public void onClick(View v) {
			renderNextQuestion();
			animateFlipper((ViewFlipper)findViewById(R.id.main_flipper));
		}
	};
	
	private void animateFlipper(ViewFlipper flipper) {
    	flipper.setInAnimation(animateInFromRight());
    	flipper.setOutAnimation(animateOutToLeft());
    	flipper.showNext();		
	}
	
	private Animation animateInFromRight() {
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(200);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}

	private Animation animateOutToLeft() {
		Animation outToLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		outToLeft.setDuration(200) ;
		outToLeft.setInterpolator(new AccelerateInterpolator());
		return outToLeft;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        db = new BarPrepDatabase(this);
        renderNextQuestion();
    }
    
    private final void renderNextQuestion() {
    	currentQuestion = db.getNextQuestion();
    	
    	if (currentQuestion == null) {
        	currentView = (ScrollView)View.inflate(this, R.layout.results, null);

	    	TextView numCorrectView = (TextView)currentView.findViewById(R.id.num_correct);
	    	numCorrectView.setText(String.format("Correct: %d", db.getNumAnswered(true)));

	    	TextView numIncorrectView = (TextView)currentView.findViewById(R.id.num_incorrect);
	    	numIncorrectView.setText(String.format("Incorrect: %d", db.getNumAnswered(false)));

	    	TextView percentView = (TextView)currentView.findViewById(R.id.percent);
	    	percentView.setText("100%");
    	} else {
        	currentView = (ScrollView)View.inflate(this, R.layout.question, null);

    		// Render question
	    	TextView questionTextView = (TextView)currentView.findViewById(R.id.question);
	    	questionTextView.setText(currentQuestion.getQuestion());
	    	
	    	// Render answers
	    	ViewGroup buttonsView = (ViewGroup)currentView.findViewById(R.id.buttons);
	    	buttonsView.removeAllViews();
	    	buttons = new ArrayList<Button>();
	    	
	    	char answerLetter = 'a';
	    	for (Iterator<Answer> i = currentQuestion.getAnswers().iterator();
	    			i.hasNext(); answerLetter++) {
	    		Answer answer = i.next();
	  
	    		Button answerButton = new Button(this);
	    		answerButton.setText(String.format("%c. %s", answerLetter, answer.getAnswer()));
	    		answerButton.setTextSize(16);
	    		answerButton.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
	    		answerButton.setPadding(15, 5, 15, 15);
	    		answerButton.setTag(answer);
	    		answerButton.setOnClickListener(onAnswerButtonClick);
	    		buttonsView.addView(answerButton);
	    		
	    		buttons.add(answerButton);
	    	}
    	}
    	
    	ViewFlipper mainFlipper = (ViewFlipper)findViewById(R.id.main_flipper);
    	mainFlipper.addView(currentView);
    }
    
    private final void renderExplanation(Answer answer) {
    	View explanationView = View.inflate(this, R.layout.explanation, null);    	

    	TextView explanationTextView = (TextView)explanationView.findViewById(R.id.explanation);
    	explanationTextView.setText(currentQuestion.getExplanation());
    	Button nextQuestionButton = (Button)explanationView.findViewById(R.id.next_question);
   		nextQuestionButton.setOnClickListener(onNextQuestionButtonClick);

    	final LinearLayout explanationLayout = (LinearLayout)currentView.findViewById(R.id.explanation_layout);
    	explanationLayout.addView(explanationView);

    	TextView resultTextView;
    	if (answer.isCorrect()) {
    		resultTextView = (TextView)explanationView.findViewById(R.id.correct);
    	} else {
    		resultTextView = (TextView)explanationView.findViewById(R.id.incorrect);
    	}
    	resultTextView.setVisibility(0);
    	
    	new Handler().postDelayed(new Runnable() {
            public void run() {
            	currentView.smoothScrollTo(0, explanationLayout.getTop());
            }
        }, 50);
    }
}