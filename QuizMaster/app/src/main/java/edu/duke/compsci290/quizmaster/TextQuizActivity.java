package edu.duke.compsci290.quizmaster;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

public class TextQuizActivity extends AppCompatActivity implements RecyclerViewClickListener {
    private RecyclerView mRecyclerView;
    private Quiz mQuiz;
    private Question mQuestion;
    private ArrayList<String> mAnswers;
    private TextView mQuestionView;
    private TextView mScoreView;
    private int mQuestionIndex;
    private int mCorrect;
    private String mScoreBase;
    private ArrayList<String> mAnswerIndexes;
    private boolean mFlipped = false;
    private String mJSonQuiz;
    private String mJSonQuestions;
    private boolean mSharedChecked;

    private static String ANSWERINDEXES = "ANSWER INDEXES";
    private static String SCORE = "SCORE";
    private static String SIZE = "SIZE";
    private static String QINDEX = "QUESTION INDEX";
    private static String FLIPPED = "FLIPPED";
    private static String QUIZ = "QUIZ";
    private static String ANSWER = "ANSWER";
    private static String JSONQUIZ = "JSONQuiz";
    private static String JSONANSWERS = "JSONOPTIONS";
    private static String SPChecked = "Sharedpreferencechecked";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        SharedPreferences checked = getPreferences(Context.MODE_PRIVATE);
        mSharedChecked = checked.getBoolean(SPChecked, false);
        mQuestionView = (TextView) this.findViewById(R.id.question_text);
        mScoreView = (TextView) this.findViewById(R.id.score_view);
        mScoreBase = mScoreView.getText().toString();
        if (savedInstanceState == null) {
            URLToQuizTask task = new URLToQuizTask(new QuizLoadedListener() {
                @Override
                public void onQuizLoaded(Quiz quiz) {
                    mQuiz = quiz;
                    initView(savedInstanceState);
                    Log.d("QUIZ", "Now it's loaded.");
                }
            });
            task.execute(getIntent().getStringExtra(ANSWER));
        }
        else
        {
            initView(savedInstanceState);
        }
    }

    private void initView(Bundle savedInstanceState) {
        if(mSharedChecked) {
            alertDialog();
        }
        else {
            mQuestionIndex = mQuiz.getIndex();
            askQuestion();
        }
        if (savedInstanceState != null) {
            Toast.makeText(TextQuizActivity.this,
                    "create and saved != null",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void alertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Continue Quiz?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getsharedpreference();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mQuestionIndex = mQuiz.getIndex();
                        askQuestion();
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onStop() {
        Gson gson = new Gson();
        mJSonQuiz = gson.toJson(mQuiz);
        mJSonQuestions = gson.toJson(mAnswers);
        savesharedpreference();
        super.onStop();
    }

    public void savesharedpreference() {
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(SCORE,mCorrect);
        Log.d(getClass().getSimpleName(), "We saved mQuestionIndex = " + mQuestionIndex);
        editor.putInt(QINDEX, mQuestionIndex);
        editor.putString(JSONQUIZ, mJSonQuiz);
        editor.putString(JSONANSWERS, mJSonQuestions);
        editor.putBoolean(SPChecked, mSharedChecked);
        editor.commit();
    }

    public void getsharedpreference() {
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        mCorrect = sp.getInt(SCORE, 0);
        mQuestionIndex = sp.getInt(QINDEX, 0);
        Log.d(getClass().getSimpleName(), "We got mQuestionIndex = " + mQuestionIndex);
        mJSonQuiz = sp.getString(JSONQUIZ, null);
        mJSonQuestions = sp.getString(JSONANSWERS, null);
        Gson gson = new Gson();
        mQuiz = gson.fromJson(mJSonQuiz, Quiz.class);
        mAnswers = gson.fromJson(mJSonQuestions, new TypeToken<ArrayList<String>>(){}.getType());
        updateScore();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mQuiz = savedInstanceState.getParcelable(QUIZ);
        mCorrect = savedInstanceState.getInt(SCORE);
        mQuestionIndex = savedInstanceState.getInt(QINDEX);
        mAnswerIndexes = savedInstanceState.getStringArrayList(ANSWERINDEXES);
        mFlipped = savedInstanceState.getBoolean(FLIPPED);
        askQuestion();
        updateScore();
    }
    @Override
    public void onSaveInstanceState(Bundle state) {
        state.putInt(SCORE, mCorrect);
        state.putInt(QINDEX, mQuestionIndex);
        state.putStringArrayList(ANSWERINDEXES, mAnswers);
        state.putBoolean(FLIPPED, true);
        state.putParcelable(QUIZ, mQuiz);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    public void correct(){
        Toast.makeText(TextQuizActivity.this,
                "that's correct! :-)",
                Toast.LENGTH_SHORT).show();
        mCorrect += 1;
    }
    public void wrong(){
        Toast.makeText(TextQuizActivity.this,
                "that's wrong :-(",
                Toast.LENGTH_SHORT).show();
    }
    protected void updateIndex() {
        mQuiz.updateIndex();
        mQuestionIndex = mQuiz.getIndex();
        if (mQuestionIndex==mQuiz.getQuestions().size()){
            onFinish();
        }
    }
    protected void updateScore() {
        int remaining = mQuiz.getQuestions().size() - mQuestionIndex;
        String s = String.format(" %d/%d with %d to go", mCorrect, mQuiz.size(), remaining);
        mScoreView.setText(mScoreBase + s);
        if (mQuestionIndex < mQuiz.getQuestions().size()) {
            askQuestion();
        } else {
            Toast.makeText(TextQuizActivity.this,
                    "quiz is over",
                    Toast.LENGTH_SHORT).show();
        }
        mFlipped = false;
    }
    protected void onFinish(){
        mSharedChecked = false;
        Intent i = new Intent(TextQuizActivity.this, FinishActivity.class);
        i.putExtra(SCORE, mCorrect);
        i.putExtra(SIZE, mQuiz.size());
        startActivity(i);
    }
    /**
     * If screen is flipped, the order of the answers do not change either.
     * **/
    protected void askQuestion() {
        mSharedChecked = true;
        mQuestion = mQuiz.getQuestions().get(mQuestionIndex);
        mAnswers = (ArrayList<String>) mQuestion.getAnswers();
        if (mFlipped){
            mAnswers = mAnswerIndexes;
        }
        TextAnswerAdapter textAnswerAdapter = new TextAnswerAdapter(this, mAnswers,this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvItems);
        mRecyclerView.setAdapter(textAnswerAdapter);
        mQuestionView.setText(mQuestion.getQuery());
    }
    @Override
    public void recyclerViewItemClicked(int position) {
        if (mAnswers.get(position).equals(mQuestion.getCorrectAnswer())){
            correct();
        }
        else {
            wrong();
        }
        updateIndex();
        updateScore();
    }
}