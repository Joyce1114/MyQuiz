package edu.duke.compsci290.quizmaster;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Question implements Parcelable{

    private String mQuery;
    private String mCorrect;
    private List<String> mAnswers;

    /**
     * Empty @param constructor
     */
    public Question() {
        mQuery = null;
        mCorrect = null;
    }

    /**
     * Instantiates question object
     * @param q is the query
     * @param a is the answer
     */
    public Question (String q, String a, ArrayList<String> answers) {
        mQuery = q;
        mCorrect = a;
        mAnswers = answers;
        Collections.shuffle(mAnswers);
    }

    protected Question(Parcel in) {
        mQuery = in.readString();
        mCorrect = in.readString();
        mAnswers = in.createStringArrayList();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    /**
     * @return the query
     */
    public String getQuery() {
        return mQuery;
    }

    /**
     * @return correct answer
     */
    public String getCorrectAnswer() {
        return mCorrect;
    }

    public List<String> getAnswers() { return mAnswers; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mQuery);
        dest.writeString(mCorrect);
        dest.writeStringList(mAnswers);
    }
}
