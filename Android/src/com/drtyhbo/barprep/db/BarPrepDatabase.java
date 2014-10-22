package com.drtyhbo.barprep.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.drtyhbo.barprep.db.Answer;
import com.drtyhbo.barprep.db.Question;

import com.drtyhbo.barprep.res.ResAnswer;
import com.drtyhbo.barprep.res.ResAnswers;
import com.drtyhbo.barprep.res.ResQuestion;
import com.drtyhbo.barprep.res.ResQuestions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.SAXException;

public class BarPrepDatabase {	
	private static final String DATABASE_NAME = "BarPrep";
	private static final int DATABASE_VERSION = 2;

	private static final String KEY_ANSWER = "answer";
	private static final String KEY_ANSWER_ID = "answer_id";
	private static final String KEY_ANSWER_INDEX = "answer_index";
	private static final String KEY_EXPLANATION = "explanation";
	private static final String KEY_IS_CORRECT = "is_correct";
	private static final String KEY_QUESTION = "question";
	private static final String KEY_QUESTION_ID = "question_id";
	private static final String KEY_ROWID = "rowid";

	private static final String ANSWER_TABLE = "answer";
	private static final String ANSWER_TABLE_CREATE = "CREATE TABLE `" + ANSWER_TABLE + "` (" +
    	"`" + KEY_ANSWER + "` text NOT NULL," +
    	"`" + KEY_ANSWER_INDEX + "` integer NOT NULL," +
    	"`" + KEY_IS_CORRECT + "` bool NOT NULL," +
    	"`" + KEY_QUESTION_ID + "` integer NOT NULL)";

	private static final String QUESTION_TABLE = "question";
	private static final String QUESTION_TABLE_CREATE = "CREATE TABLE `" + QUESTION_TABLE + "` (" +
		"`" + KEY_EXPLANATION + "` text NOT NULL," +
		"`" + KEY_QUESTION + "` text NOT NULL)";
	
	private static final String USER_ANSWER_TABLE = "user_answer";
	private static final String USER_ANSWER_TABLE_CREATE = "CREATE TABLE `" + USER_ANSWER_TABLE + "` (" +
		"`" + KEY_QUESTION_ID + "` integer NOT NULL," +
		"`" + KEY_ANSWER_ID + "` integer NOT NULL," +
		"`" + KEY_IS_CORRECT + "` bool NOT NULL)";

	private final BarPrepOpenHelper barPrepOpenHelper;
	
	public BarPrepDatabase(Context context) {
		barPrepOpenHelper = new BarPrepOpenHelper(context);
		barPrepOpenHelper.getReadableDatabase();
	}

	private final List<Answer> getAnswersForQuestion(int rowid) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(ANSWER_TABLE);

        Cursor cursor = builder.query(barPrepOpenHelper.getReadableDatabase(),
        		new String[] {KEY_ANSWER, KEY_IS_CORRECT, KEY_ROWID}, "question_id=?",
        		new String[] {String.valueOf(rowid)}, null, null, KEY_ROWID, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        
        ArrayList<Answer> answers = new ArrayList<Answer>();
        do {
        	Answer answer = new Answer();
        	answer.setAnswer(cursor.getString(0));
        	answer.setCorrect(cursor.getInt(1) == 1);
        	answer.setId(cursor.getInt(2));
        	answers.add(answer);
        } while(cursor.moveToNext());

        return answers;
	}

	private final int getLastAnsweredQuestionId() {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(USER_ANSWER_TABLE);

        Cursor cursor = builder.query(barPrepOpenHelper.getReadableDatabase(),
        		new String[] {KEY_QUESTION_ID},
        		null, null, null, null, KEY_QUESTION_ID + " DESC", "1");

        if (cursor == null) {
            return -1;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return -1;
        }
        
        return cursor.getInt(0);
	}
	
	public final Question getNextQuestion() {		
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(QUESTION_TABLE);

        Cursor cursor = builder.query(barPrepOpenHelper.getReadableDatabase(),
        		new String[] {KEY_EXPLANATION, KEY_QUESTION, KEY_ROWID},
        		"rowid > ?", new String[] {String.valueOf(getLastAnsweredQuestionId())},
        		null, null, KEY_ROWID, "1");

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        
        Question question = new Question();
        question.setExplanation(cursor.getString(0));
        question.setQuestion(cursor.getString(1));
        question.setId(cursor.getInt(2));
        question.setAnswers(getAnswersForQuestion(question.getId()));

        return question;
	}
	
	public final int getNumAnswered(boolean isCorrect) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(USER_ANSWER_TABLE);

        Cursor cursor = builder.query(barPrepOpenHelper.getReadableDatabase(),
        		new String[] {KEY_ANSWER_ID},
        		KEY_IS_CORRECT + " = ?", new String[] {String.valueOf(isCorrect)},
        		null, null, null);

        if (cursor == null) {
            return 0;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return 0;
        }
        
        return cursor.getColumnCount();
	}

	public final void saveAnswer(Question question, Answer answer) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ANSWER_ID, answer.getId());
		initialValues.put(KEY_IS_CORRECT, answer.isCorrect());
		initialValues.put(KEY_QUESTION_ID, question.getId());
		barPrepOpenHelper.getWritableDatabase().insert(USER_ANSWER_TABLE, null, initialValues);
	}
	
	private static class BarPrepOpenHelper extends SQLiteOpenHelper {
		private final Context helperContext;
				
		BarPrepOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			helperContext = context;
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(QUESTION_TABLE_CREATE);
			db.execSQL(ANSWER_TABLE_CREATE);
			db.execSQL(USER_ANSWER_TABLE_CREATE);
			load(db);
		}
		
		private void load(SQLiteDatabase db) {
			try {
				loadQuestions(db);
				loadAnswers(db);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			} catch (SAXException ex) {
				throw new RuntimeException(ex);
			}
		}
		
		private void loadQuestions(SQLiteDatabase db)
				throws IOException, SAXException {
			final ResQuestions questions = new ResQuestions();
			questions.load(helperContext);

			final List<ResQuestion> questionList = questions.getQuestions();
			for (Iterator<ResQuestion> i = questionList.iterator(); i.hasNext();) {
				final ResQuestion question = i.next();

				ContentValues initialValues = new ContentValues();
				initialValues.put(KEY_EXPLANATION, question.getExplanation());
				initialValues.put(KEY_QUESTION, question.getQuestion());
				initialValues.put(KEY_ROWID, question.getId());
				db.insert(QUESTION_TABLE, null, initialValues);
			}
		}

		private void loadAnswers(SQLiteDatabase db)
				throws IOException, SAXException {
			final ResAnswers answers = new ResAnswers();
			answers.load(helperContext);
			
			final List<ResAnswer> answerList = answers.getAnswers();
			for (Iterator<ResAnswer> i = answerList.iterator(); i.hasNext();) {
				final ResAnswer answer = i.next();

				ContentValues initialValues = new ContentValues();
				initialValues.put(KEY_ANSWER, answer.getAnswer());
				initialValues.put(KEY_ANSWER_INDEX, answer.getAnswerIndex());
				initialValues.put(KEY_IS_CORRECT, answer.isCorrect());
				initialValues.put(KEY_QUESTION_ID, answer.getQuestionId());
				initialValues.put(KEY_ROWID, answer.getId());
				db.insert(ANSWER_TABLE, null, initialValues);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + QUESTION_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + ANSWER_TABLE);
			onCreate(db);
		}
	}
}
