package edu.duke.compsci290.quizmaster;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.util.List;



public class XMLQuizGenerator {

    private List<Question> mQuestions;
    private String mFile;
    private Quiz quiz;
    private static String url = "http://people.duke.edu/~yc221/duke_quiz.xml";

    public XMLQuizGenerator (Context context, String file) {
        mFile = file;
        createQuiz(context, mFile);
        mQuestions = quiz.getQuestions();
    }

    private void createQuiz(Context context, String file){
        try {
            InputStream in = context.getAssets().open(file);
            quiz = new XMLQuizParser().parse(in);
            Log.d("Question", quiz.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Quiz getQuiz() { return quiz; }

}