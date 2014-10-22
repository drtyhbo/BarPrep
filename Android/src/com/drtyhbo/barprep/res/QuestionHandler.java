package com.drtyhbo.barprep.res;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class QuestionHandler extends DefaultHandler {
	private static final String MODEL = "model";
	private static final String EXPLANATION = "explanation";
	private static final String ID = "id";
	private static final String QUESTION = "question";
	
	private ResQuestion question;
	private List<ResQuestion> questions;
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

		if (question != null) {
			if (localName.equalsIgnoreCase(ID)) {
				question.setId(Integer.valueOf(builder.toString()));
			} else if (localName.equalsIgnoreCase(QUESTION)) {
				question.setQuestion(builder.toString());
			} else if (localName.equalsIgnoreCase(EXPLANATION)) {
				question.setExplanation(builder.toString());
			} else if (localName.equalsIgnoreCase(MODEL)) {
				questions.add(question);
				question = null;
			}
		}
		builder.setLength(0);
	}
	
	public final List<ResQuestion> getQuestions() {
		return questions;
	}

	@Override
	public void startDocument()
			throws SAXException {
		super.startDocument();
		builder = new StringBuilder();
		questions = new ArrayList<ResQuestion>();
	}
	
	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);

		if (localName.equalsIgnoreCase(MODEL)) {
			question = new ResQuestion();
		}
	}
}
