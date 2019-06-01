package com.nghiatv.quizapp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.nghiatv.quizapp.R;
import com.nghiatv.quizapp.adapter.ResultGridAdapter;
import com.nghiatv.quizapp.utils.Common;
import com.nghiatv.quizapp.utils.SpaceDecoration;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ResultActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtTimer, txtResult, txtRightAnswer;
    Button btnFilterTotal, btnFilterRightAnswer, btnFilterWrongAnswer, btnFilterNoAnswer;
    RecyclerView recyclerResult;

    ResultGridAdapter adapter, filterAdapter;

    BroadcastReceiver backToQuestion = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().toString().equals(Common.KEY_BACK_FOR_RESULT)) {
                int question = intent.getIntExtra(Common.KEY_BACK_FOR_RESULT, -1);
                goBackActivityWithQuestion(question);
            }
        }
    };

    private void goBackActivityWithQuestion(int question) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Common.KEY_BACK_FOR_RESULT, question);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        LocalBroadcastManager.getInstance(this).registerReceiver(backToQuestion,
                new IntentFilter(Common.KEY_BACK_FOR_RESULT));

        //Init view
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("RESULT");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtResult = (TextView) findViewById(R.id.txtResult);
        txtRightAnswer = (TextView) findViewById(R.id.txtRightAnswer);
        txtTimer = (TextView) findViewById(R.id.txtTimer);

        btnFilterTotal = (Button) findViewById(R.id.btnFilterTotal);
        btnFilterRightAnswer = (Button) findViewById(R.id.btnFilterRightAnswer);
        btnFilterWrongAnswer = (Button) findViewById(R.id.btnFilterWrongAnswer);
        btnFilterNoAnswer = (Button) findViewById(R.id.btnFilterNoAnswer);

        recyclerResult = (RecyclerView) findViewById(R.id.recyclerResult);
        recyclerResult.setHasFixedSize(true);
        recyclerResult.setLayoutManager(new GridLayoutManager(this, 3));

        adapter = new ResultGridAdapter(this, Common.answerSheetList);
        recyclerResult.addItemDecoration(new SpaceDecoration(4));
        recyclerResult.setAdapter(adapter);

        txtTimer.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(Common.timer),
                TimeUnit.MILLISECONDS.toSeconds(Common.timer) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Common.timer))));

        txtRightAnswer.setText(new StringBuilder("").append(Common.rightAnswerCount)
                .append("/").append(Common.questionList.size()));

        btnFilterTotal.setText(new StringBuilder("").append(Common.questionList.size()));
        btnFilterRightAnswer.setText(new StringBuilder("").append(Common.rightAnswerCount));
        btnFilterWrongAnswer.setText(new StringBuilder("").append(Common.wrongAnswerCount));
        btnFilterNoAnswer.setText(new StringBuilder("").append(Common.noAnswerCount));

        //Caculate result
        int percent = (Common.rightAnswerCount * 100 / Common.questionList.size());
        if (percent > 80) {
            txtResult.setText("EXCELLENT");
        } else if (percent > 70) {
            txtResult.setText("GOOD");
        } else if (percent > 60) {
            txtResult.setText("FAIR");
        } else if (percent > 50) {
            txtResult.setText("POOR");
        } else if (percent > 40) {
            txtResult.setText("BAD");
        } else {
            txtResult.setText("FALL");
        }

        //Event filter
        btnFilterTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter == null) {
                    adapter = new ResultGridAdapter(ResultActivity.this, Common.answerSheetList);
                    recyclerResult.setAdapter(adapter);
                } else {
                    recyclerResult.setAdapter(adapter);
                }
            }
        });

        btnFilterNoAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.answerSheetListFilter = new ArrayList<>();
                for (int i = 0; i < Common.answerSheetList.size(); i++) {
                    if (Common.answerSheetList.get(i).getType() == Common.ANSWER_TYPE.NO_ANSWER) {
                        Common.answerSheetListFilter.add(Common.answerSheetList.get(i));
                    }
                }
                filterAdapter = new ResultGridAdapter(ResultActivity.this, Common.answerSheetListFilter);
                recyclerResult.setAdapter(filterAdapter);
            }
        });

        btnFilterWrongAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.answerSheetListFilter = new ArrayList<>();
                for (int i = 0; i < Common.answerSheetList.size(); i++) {
                    if (Common.answerSheetList.get(i).getType() == Common.ANSWER_TYPE.WRONG_ANSWER) {
                        Common.answerSheetListFilter.add(Common.answerSheetList.get(i));
                    }
                }
                filterAdapter = new ResultGridAdapter(ResultActivity.this, Common.answerSheetListFilter);
                recyclerResult.setAdapter(filterAdapter);
            }
        });

        btnFilterRightAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.answerSheetListFilter = new ArrayList<>();
                for (int i = 0; i < Common.answerSheetList.size(); i++) {
                    if (Common.answerSheetList.get(i).getType() == Common.ANSWER_TYPE.RIGHT_ANSWER) {
                        Common.answerSheetListFilter.add(Common.answerSheetList.get(i));
                    }
                }
                filterAdapter = new ResultGridAdapter(ResultActivity.this, Common.answerSheetListFilter);
                recyclerResult.setAdapter(filterAdapter);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuViewAnswer:
                viewQuizAnswer();
                break;

            case R.id.menuDoQuizAgain:
                doQuizAgain();
                break;

            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //Delete all activity
                startActivity(intent);
                break;

            default:
                break;
        }

        return true;
    }

    private void viewQuizAnswer() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Common.ACTION, Common.VIEW_ANSWER);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void doQuizAgain() {
        new MaterialStyledDialog.Builder(this)
                .setTitle("Do quiz again?")
                .setIcon(R.drawable.ic_mood_white_24dp)
                .setDescription("Do you really want to delete this data?")
                .setNegativeText("No")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveText("Yes")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(Common.ACTION, Common.DO_QUIZ_AGAIN);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                })
                .show();
    }
}
