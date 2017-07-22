package edu.duke.compsci290.quizmaster;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class URLToQuizTask extends AsyncTask<String, Void, Quiz> {
    private InputStream is;
    private QuizLoadedListener quizLoadedListener;

    public URLToQuizTask(QuizLoadedListener callback){
        quizLoadedListener = callback;
    }

    @Override
    protected Quiz doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL(params[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            Log.d("HTTP", "RETURNED INPUT STREAM");
            is = conn.getInputStream();
            XMLQuizParser quizParser = new XMLQuizParser();
            Quiz q = null;
            try {
                q = quizParser.parse(is);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return q;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Quiz quiz) {
        quizLoadedListener.onQuizLoaded(quiz);
    }
}