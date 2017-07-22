package edu.duke.compsci290.quizmaster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickListener{

    private static String ANSWER = "ANSWER";
    private List<String> mOptions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            Toast.makeText(MainActivity.this,
                    "create and saved != null",
                    Toast.LENGTH_SHORT).show();
        }
        mOptions.add("Image Quiz");
        mOptions.add("Duke Quiz");
        mOptions.add("CS290 Quiz");
        MainAdapter mainAdapter = new MainAdapter(this, mOptions, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main);
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

//    public void easyClick(View button) {
//        Intent i = new Intent(MainActivity.this, ImageQuizActivity.class);
//        i.putExtra(ANSWER, "http://people.duke.edu/~yc221/image_quiz.xml");
//        startActivity(i);
//    }
//
//    public void hardClick(View button) {
//        Intent i = new Intent(MainActivity.this, TextQuizActivity.class);
//        i.putExtra(ANSWER, "http://people.duke.edu/~yc221/duke_quiz.xml");
//        startActivity(i);
//    }

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
                Intent easy = new Intent(MainActivity.this, TextQuizActivity.class);
                easy.putExtra(ANSWER, "http://people.duke.edu/~yc221/math_quiz.xml");
                startActivity(easy);
                break;
            case R.id.hard_quiz:
                Intent hard = new Intent(MainActivity.this, TextQuizActivity.class);
                hard.putExtra(ANSWER, "http://people.duke.edu/~yc221/duke_quiz.xml");
                startActivity(hard);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    @Override
    public void recyclerViewItemClicked(int position) {
        if (mOptions.get(position).equals("Image Quiz")) {
            Intent easy = new Intent(MainActivity.this, ImageQuizActivity.class);
            easy.putExtra(ANSWER, "http://people.duke.edu/~yc221/image_quiz.xml");
            startActivity(easy);
        }
        else if (mOptions.get(position).equals("Duke Quiz")) {
            Intent hard = new Intent(MainActivity.this, TextQuizActivity.class);
            hard.putExtra(ANSWER, "http://people.duke.edu/~yc221/duke_quiz.xml");
            startActivity(hard);
        }
        else {
            Intent easy = new Intent(MainActivity.this, TextQuizActivity.class);
            easy.putExtra(ANSWER, "http://people.duke.edu/~yc221/cs290.xml");
            startActivity(easy);
        }
    }
}