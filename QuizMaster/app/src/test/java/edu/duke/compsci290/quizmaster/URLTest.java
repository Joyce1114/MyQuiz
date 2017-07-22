package edu.duke.compsci290.quizmaster;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



public class URLTest {
    @Test
    public void test_URL() throws Exception {
        URL url = new URL("http://people.duke.edu/~yc221/duke_quiz.xml");
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        System.out.println(is);
    }
}
