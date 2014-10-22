package com.drtyhbo.barprep.res;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AnswerHandler extends DefaultHandler {
	private static final String MODEL = "model";
	private static final String ANSWER = "answer";
	private static final String ANSWER_INDEX = "answer_index";
	private static final String ID = "id";
	private static final String IS_CORRECT = "is_correct";
	private static final String QUESTION_ID = "question_id";
	
	private ResAnswer answer;
	private List<ResAnswer> answers;
	private StringBuilder builder;

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		builder.append(ch, start, length);
	}
	
	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		super.endElement(uri, localName, name);

		if (answer != null) {
			if (localName.equalsIgnoreCase(ANSWER)) {
				answer.setAnswer(builder.toString());
			} else if (localName.equalsIgnoreCase(ANSWER_INDEX)) {
				answer.setAnswerIndex(Integer.valueOf(builder.toString()));
			} else if (localName.equalsIgnoreCase(ID)) {
				answer.setId(Integer.valueOf(builder.toString()));
			} else if (localName.equalsIgnoreCase(IS_CORRECT)) {
				answer.setCorrect(Integer.valueOf(builder.toString()) != 0);
			} else if (localName.equalsIgnoreCase(QUESTION_ID)) {
				answer.setQuestionId(Integer.valueOf(builder.toString()));
			} else if (localName.equalsIgnoreCase(MODEL)) {
				answers.add(answer);
				answer = null;
			}
		}
		builder.setLength(0);
	}

	public List<ResAnswer> getAnswers() {
		return answers;
	}
	
	@Override
	public void startDocument()
			throws SAXException {
		super.startDocument();
		answers = new ArrayList<ResAnswer>();
		builder = new StringBuilder();
	}
	
	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);

		if (localName.equalsIgnoreCase(MODEL)) {
			answer = new ResAnswer();
		}
	}
}
