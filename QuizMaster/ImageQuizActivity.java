package edu.duke.compsci290.quizmaster;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ImageQuizActivity extends AppCompatActivity implements RecyclerViewClickListener {
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
    private String jsonquiz;
    private String jsonquestions;
    private boolean sharedchecked;

    private static String ANSWERINDEXES = "ANSWER INDEXES";
    private static String SCORE = "SCORE";
    private static String SIZE = "SIZE";
    private static String QINDEX = "QUESTION INDEX";
    private static String FLIPPED = "FLIPPED";
    private static String QUIZ = "QUIZ";
    private static String ANSWER = "ANSWER";
    private static String SharedP = "Quizsharedpreference";
    private static String JSON1 = "JSONQuiz";
    private static String JSON2= "JSONOPTIONS";
    private static String SPChecked = "Sharedpreferencechecked";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        SharedPreferences checked = getPreferences(Context.MODE_PRIVATE);
        sharedchecked = checked.getBoolean(SPChecked, false);
//        sharedchecked = false;
        Log.d("SHAREDPREFERENCES", String.valueOf(sharedchecked));
        mQuestionView = (TextView) this.findViewById(R.id.question_text);
        mScoreView = (TextView) this.findViewById(R.id.score_view);
        mScoreBase = mScoreView.getText().toString();

//        if(sharedchecked)
//        {
//            initView(savedInstanceState);
//            getsharedpreference();
//            askQuestion();
//        }
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
        } else {
            initView(savedInstanceState);
        }

    }
    private void initView(Bundle savedInstanceState) {
        if (sharedchecked) {
            getsharedpreference();
            sharedchecked = false;
        } else {
            mQuestionIndex = mQuiz.getIndex();
            askQuestion();
        }

        if (savedInstanceState != null) {
            Toast.makeText(ImageQuizActivity.this,
                    "create and saved != null",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mQuiz = savedInstanceState.getParcelable(QUIZ);
        mCorrect = savedInstanceState.getInt(SCORE);
        mQuestionIndex = savedInstanceState.getInt(QINDEX);
        mAnswerIndexes = savedInstanceState.getStringArrayList(ANSWERINDEXES);
        mFlipped = savedInstanceState.getBoolean(FLIPPED);
        updateScore();
        askQuestion();
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
        Toast.makeText(ImageQuizActivity.this,
                "that's correct! :-)",
                Toast.LENGTH_SHORT).show();
        mCorrect += 1;
    }
    public void wrong(){
        Toast.makeText(ImageQuizActivity.this,
                "that's wrong :-(",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop()
    {
        Gson gson = new Gson();
        jsonquiz = gson.toJson(mQuiz);
        jsonquestions = gson.toJson(mAnswers);
        savesharedpreference();
        super.onStop();

    }

    public void savesharedpreference()
    {
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(SCORE,mCorrect);
        Log.d(getClass().getSimpleName(), "We saved mQuestionIndex = " + mQuestionIndex);
        editor.putInt(QINDEX, mQuestionIndex);
        editor.putString(JSON1, jsonquiz);
        editor.putString(JSON2,jsonquestions);
        sharedchecked = true;
        editor.putBoolean(SPChecked,sharedchecked);
        editor.commit();


    }

    public void getsharedpreference()
    {
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        mCorrect = sp.getInt(SCORE, 0);
        mQuestionIndex = sp.getInt(QINDEX, 0);
        Log.d(getClass().getSimpleName(), "We got mQuestionIndex = " + mQuestionIndex);
        jsonquiz = sp.getString(JSON1, null);
        jsonquestions = sp.getString(JSON2, null);
        Gson gson = new Gson();
        mQuiz = gson.fromJson(jsonquiz, Quiz.class);
        mAnswers = gson.fromJson(jsonquestions, new TypeToken<ArrayList<String>>(){}.getType());
        updateScore();

    }
    protected void updateIndex() {
        mQuiz.updateIndex();
        mQuestionIndex = mQuiz.getIndex();
        if (mQuestionIndex==mQuiz.size()){
            onFinish();
        }
    }
    protected void updateScore() {
        int remaining = mQuiz.size() - mQuestionIndex;
        String s = String.format(" %d/%d with %d to go", mCorrect, mQuiz.size(), remaining);
        mScoreView.setText(mScoreBase + s);
        if (mQuestionIndex < mQuiz.getQuestions().size()) {
            askQuestion();
        } else {
            Toast.makeText(ImageQuizActivity.this,
                    "quiz is over",
                    Toast.LENGTH_SHORT).show();
        }
        mFlipped = false;
    }
    protected void onFinish(){
        Intent i = new Intent(ImageQuizActivity.this, FinishActivity.class);
        i.putExtra(SCORE, mCorrect);
        i.putExtra(SIZE, mQuiz.size());
        startActivity(i);
    }
    /**
     * If screen is flipped, the order of the answers do not change either.
     * **/
    protected void askQuestion() {

        mQuestion = mQuiz.getQuestions().get(mQuestionIndex);
        mAnswers = (ArrayList<String>) mQuestion.getAnswers();
        if (mFlipped){
            mAnswers = mAnswerIndexes;
        }
        ImageAnswerAdapter imageAnswerAdapter = new ImageAnswerAdapter(this, mAnswers, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvItems);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.setAdapter(imageAnswerAdapter);
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
