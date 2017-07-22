package edu.duke.compsci290.quizmaster;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class XMLQuizParser {

    private static final String ns = null;
    public static String t;
    public static String filename;

    public Quiz parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readQuiz(parser);
        } finally{
            in.close();
        }
    }

    private Quiz readQuiz(XmlPullParser parser) throws  XmlPullParserException, IOException{
        List<Question> questionList = new ArrayList<>();
        String title = "";

        parser.require(XmlPullParser.START_TAG, ns, "quiz");
        while(parser.next()!=XmlPullParser.END_TAG){
            if (parser.getEventType()!=XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("file")) {
                filename = readFileName(parser);
            } else if (name.equals("question")){
                questionList.add(readQuestion(parser));
            } else {
                skip(parser);
            }
        }
        return new Quiz(title, questionList);
    }

    private String readFileName(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, ns, "file");
        String filename = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "file");
        return filename;
    }

    private Question readQuestion(XmlPullParser parser) throws XmlPullParserException, IOException {
        String q = "";
        String correct = "";
        List<String> answerList = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "question");
        while(parser.next()!=XmlPullParser.END_TAG) {
            if(parser.getEventType()!= XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("text")) {
                q = readText(parser);
            }
            else if (name.equals("answer")) {
                String answer = parser.getAttributeValue(ns, "correct");
                if (answer.equals("true")){
                    correct = readText(parser);
                    answerList.add(correct);
                }
                else {
                    answerList.add(readText(parser));
                }
            }
            else if (name.equals("correctanswer")){
                correct = readText(parser);
                answerList.add(correct);
            }
            else if (name.equals("wronganswer")){
                answerList.add(readText(parser));
            }
            else {
                skip(parser);
            }
        }
        return new Question(q, correct, (ArrayList<String>) answerList);
    }

    private String readText(XmlPullParser parser) throws XmlPullParserException, IOException{
        String text = "";
        if (parser.next() == XmlPullParser.TEXT){
            text = parser.getText();
            parser.nextTag();
        }
        return text;
    }

    public String readTitle(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        t = title;
        return title;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while( depth != 0 ) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
