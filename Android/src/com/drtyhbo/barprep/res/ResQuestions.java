package com.drtyhbo.barprep.res;

import android.content.Context;
import android.content.res.Resources;
import android.util.Xml;

import com.drtyhbo.barprep.res.QuestionHandler;
import com.drtyhbo.barprep.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.xml.sax.SAXException;

public class ResQuestions {
	private List<ResQuestion> questions;

	public final List<ResQuestion> getQuestions() {
		return questions;
	}
	
	public final void load(Context context)
			throws IOException, SAXException {

		final Resources resources = context.getResources();
		InputStream inputStream = resources.openRawResource(R.raw.questions);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		QuestionHandler questionHandler = new QuestionHandler();
		Xml.parse(reader, questionHandler);

		questions = questionHandler.getQuestions();
	}
}
