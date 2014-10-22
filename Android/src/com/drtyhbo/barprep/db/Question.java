package com.drtyhbo.barprep.db;

import java.util.ArrayList;
import java.util.List;

public class Question {
	private int id;
	private String explanation;
	private String question;
	private List<Answer> answers;

	public List<Answer> getAnswers() {
		return answers;
	}
	public int getId() {
		return id;
	}
	public String getExplanation() {
		return explanation;
	}
	public String getQuestion() {
		return question;
	}
	
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
}
