package edu.duke.compsci290.quizmaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class FinishActivity extends AppCompatActivity implements RecyclerViewClickListener{
    private static String ANSWER = "ANSWER";
    private static String SCORE = "SCORE";
    private static String SIZE = "SIZE";
    private List<String> mOptions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finish);
        TextView try_again = (TextView) findViewById(R.id.tryagain);
        try_again.setText(R.string.try_again);
        TextView final_score_view = (TextView) findViewById(R.id.finalscore);
        String final_score = final_score_view.getText().toString();
        final_score_view.setText(final_score+
                String.format(" %d/%d ",
                        getIntent().getIntExtra(SCORE,0), getIntent().getIntExtra(SIZE,0)));
        mOptions.add("Image Quiz");
        mOptions.add("Duke Quiz");
        mOptions.add("CS290 Quiz");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.finish);
        MainAdapter adapter = new MainAdapter(this, mOptions, this);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(FinishActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.easy_quiz:
                Intent easy = new Intent(FinishActivity.this, ImageQuizActivity.class);
                easy.putExtra(ANSWER, "http://people.duke.edu/~yc221/image_quiz.xml");
                startActivity(easy);
                break;
            case R.id.hard_quiz:
                Intent i = new Intent(FinishActivity.this, TextQuizActivity.class);
                i.putExtra(ANSWER, "http://people.duke.edu/~yc221/duke_quiz.xml");
                startActivity(i);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public void recyclerViewItemClicked(int position) {
        if (mOptions.get(position).equals("Image Quiz")) {
            Intent easy = new Intent(FinishActivity.this, ImageQuizActivity.class);
            easy.putExtra(ANSWER, "http://people.duke.edu/~yc221/image_quiz.xml");
            startActivity(easy);
        }
        else if (mOptions.get(position).equals("Duke Quiz")){
            Intent hard = new Intent(FinishActivity.this, TextQuizActivity.class);
            hard.putExtra(ANSWER, "http://people.duke.edu/~yc221/duke_quiz.xml");
            startActivity(hard);
        }
        else {
            Intent easy = new Intent(FinishActivity.this, TextQuizActivity.class);
            easy.putExtra(ANSWER, "http://people.duke.edu/~yc221/cs290.xml");
            startActivity(easy);
        }
    }
}
