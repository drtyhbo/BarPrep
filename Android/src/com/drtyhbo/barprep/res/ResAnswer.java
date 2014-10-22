package com.drtyhbo.barprep.res;

public class ResAnswer {
	private String answer;
	private int answerIndex;
	private int id;
	private boolean isCorrect;
	private int questionId;

	public String getAnswer() {
		return answer;
	}
	public int getAnswerIndex() {
		return answerIndex;
	}
	public int getId() {
		return id;
	}
	public boolean isCorrect() {
		return isCorrect;
	}
	public int getQuestionId() {
		return questionId;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public void setAnswerIndex(int answerIndex) {
		this.answerIndex = answerIndex;
	}
	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
}
