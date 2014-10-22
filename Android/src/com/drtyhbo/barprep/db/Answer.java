package com.drtyhbo.barprep.db;

public class Answer {
	private String answer;
	private int id;
	private boolean isCorrect;

	public String getAnswer() {
		return answer;
	}
	public int getId() {
		return id;
	}
	public boolean isCorrect() {
		return isCorrect;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
	public void setId(int id) {
		this.id = id;
	}
}
