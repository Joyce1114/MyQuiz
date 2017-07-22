package edu.duke.compsci290.quizmaster;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Quiz implements Parcelable {

    private int mIndex = 0;
    private int mScore;
    private String mTitle;
    private List<Question> mQuestions;

    public Quiz (String title, List<Question> questions){
        mTitle = title;
        Collections.shuffle(questions);
        mQuestions = questions;
    }

    protected Quiz(Parcel in) {
        mTitle = in.readString();
        in.readTypedList(mQuestions, Question.CREATOR);
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    public void updateIndex(){mIndex++;}

    public int getIndex(){return mIndex;}

    public String getTitle() {return mTitle;}

    public List<Question> getQuestions() {return mQuestions;}

    public void updateScore(){
        mScore++;
    }

    public int getScore() {return mScore;}

    public void setScore(int score){mScore = score;}

    public int size(){return mQuestions.size();}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeList(mQuestions);
    }
}
