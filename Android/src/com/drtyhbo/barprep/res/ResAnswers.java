package com.drtyhbo.barprep.res;

import android.content.Context;
import android.content.res.Resources;
import android.util.Xml;

import com.drtyhbo.barprep.res.AnswerHandler;
import com.drtyhbo.barprep.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.xml.sax.SAXException;

public class ResAnswers {
	private List<ResAnswer> answers;

	public final List<ResAnswer> getAnswers() {
		return answers;
	}
	
	public final void load(Context context)
			throws IOException, SAXException {

		final Resources resources = context.getResources();
		InputStream inputStream = resources.openRawResource(R.raw.answers);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		AnswerHandler answerHandler = new AnswerHandler();
		Xml.parse(reader, answerHandler);

		answers = answerHandler.getAnswers();
	}
}
