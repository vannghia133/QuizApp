package com.nghiatv.quizapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.gson.Gson;
import com.nghiatv.quizapp.IRecyclerHelperClick;
import com.nghiatv.quizapp.R;
import com.nghiatv.quizapp.adapter.AnswerSheetAdapter;
import com.nghiatv.quizapp.adapter.AnswerSheetHelperAdapter;
import com.nghiatv.quizapp.adapter.QuestionFragmentAdapter;
import com.nghiatv.quizapp.fragment.QuestionFragment;
import com.nghiatv.quizapp.helper.DBHelper;
import com.nghiatv.quizapp.model.CurrentQuestion;
import com.nghiatv.quizapp.utils.Common;
import com.nghiatv.quizapp.utils.SpaceDecoration;

import java.util.concurrent.TimeUnit;

public class QuestionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int CODE_GET_RESULT = 2000;

    int timePlay = Common.TOTAL_TIME;
    boolean isAnswerModeView = false;
    TextView txtRightAnswer, txtTimer, txtWrongAnser;
    Button btnDone;

    RecyclerView recyclerGridAnswerSheet;
    RecyclerView recyclerHelperAnswerSheet;
    AnswerSheetAdapter answerSheetAdapter;
    AnswerSheetHelperAdapter answerSheetHelperAdapter;

    ViewPager viewPager;
    TabLayout tabLayout;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(Common.categorySelected.getName());
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        recyclerHelperAnswerSheet = (RecyclerView) navigationView.getHeaderView(0).findViewById(R.id.recyclerHelperAnswerSheet);
        recyclerHelperAnswerSheet.setHasFixedSize(true);
        recyclerHelperAnswerSheet.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerHelperAnswerSheet.addItemDecoration(new SpaceDecoration(4));
        answerSheetHelperAdapter = new AnswerSheetHelperAdapter(this, Common.answerSheetList);
        recyclerHelperAnswerSheet.setAdapter(answerSheetHelperAdapter);

        btnDone = (Button) navigationView.getHeaderView(0).findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                finishGame();
            }
        });

        //Take questions from DB
        takeQuestions();

        if (Common.questionList.size() > 0) {
            //Show TextView right answer and text view time
            txtRightAnswer = (TextView) findViewById(R.id.txtQuestionRight);
            txtTimer = (TextView) findViewById(R.id.txtTimer);

            txtRightAnswer.setVisibility(View.VISIBLE);
            txtTimer.setVisibility(View.VISIBLE);
            txtRightAnswer.setText(new StringBuilder(String.format("%d/%d", Common.rightAnswerCount, Common.questionList.size())));


            countTimer();


            //Init view
            recyclerGridAnswerSheet = (RecyclerView) findViewById(R.id.recyclerGridAnswer);
            recyclerGridAnswerSheet.setHasFixedSize(true);
            if (Common.questionList.size() > 5) {
                recyclerGridAnswerSheet.setLayoutManager(new GridLayoutManager(this, Common.questionList.size() / 2));
            }
            answerSheetAdapter = new AnswerSheetAdapter(this, Common.answerSheetList);
            recyclerGridAnswerSheet.setAdapter(answerSheetAdapter);

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            tabLayout = (TabLayout) findViewById(R.id.slidingTabs);

            generateFragmentList();

            QuestionFragmentAdapter questionFragmentAdapter = new QuestionFragmentAdapter(getSupportFragmentManager(),
                    this,
                    Common.fragmentList);
            viewPager.setAdapter(questionFragmentAdapter);
            tabLayout.setupWithViewPager(viewPager);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                int SCROLLING_RIGHT = 0;
                int SCROLLING_LEFT = 1;
                int SCROLLING_UNDETERMINED = 2;
                int currentScrollDirection = 2;

                private void setCurrentScrollDirection(float positionOffset) {
                    if ((1 - positionOffset) >= 0.5) {
                        this.currentScrollDirection = SCROLLING_RIGHT;
                    } else if ((1 - positionOffset) <= 0.5) {
                        this.currentScrollDirection = SCROLLING_LEFT;
                    }
                }

                private boolean isScrollDirectUndetermined() {
                    return currentScrollDirection == SCROLLING_UNDETERMINED;
                }

                private boolean isScrollingRight() {
                    return currentScrollDirection == SCROLLING_RIGHT;
                }

                private boolean isScrollingLeft() {
                    return currentScrollDirection == SCROLLING_LEFT;
                }

                @Override
                public void onPageScrolled(int i, float v, int i1) {
                    if (isScrollDirectUndetermined()) {
                        setCurrentScrollDirection(v);
                    }
                }

                @Override
                public void onPageSelected(int i) {
                    QuestionFragment questionFragment;
                    int position = 0;
                    if (i > 0) {
                        if (isScrollingRight()) {
                            questionFragment = Common.fragmentList.get(i - 1);
                            position = i - 1;
                        } else if (isScrollingLeft()) {
                            questionFragment = Common.fragmentList.get(i + 1);
                            position = i + 1;
                        } else {
                            questionFragment = Common.fragmentList.get(position);
                        }
                    } else {
                        questionFragment = Common.fragmentList.get(0);
                        position = 0;
                    }

                    //If you want to show correct answer just call function here
                    CurrentQuestion questionState = questionFragment.getSelectedAnswer();
                    Common.answerSheetList.set(position, questionState);
                    answerSheetAdapter.notifyDataSetChanged();
                    answerSheetHelperAdapter.notifyDataSetChanged();

                    countCorrectAnswer();

                    txtRightAnswer.setText(new StringBuilder(String.format("%d", Common.rightAnswerCount))
                            .append("/")
                            .append(String.format("%d", Common.questionList.size()))
                            .toString());
                    txtWrongAnser.setText(String.valueOf(Common.wrongAnswerCount));

                    if (questionState.getType() == Common.ANSWER_TYPE.NO_ANSWER) {
                        questionFragment.showCorrectAnswer();
                        questionFragment.disableAnswer();
                    }
                }

                @Override
                public void onPageScrollStateChanged(int i) {
                    if (i == ViewPager.SCROLL_STATE_IDLE) {
                        this.currentScrollDirection = SCROLLING_UNDETERMINED;
                    }
                }
            });
        }
    }

    private void countCorrectAnswer() {
        //Reset variable
        Common.rightAnswerCount = Common.wrongAnswerCount = 0;
        for (CurrentQuestion item : Common.answerSheetList) {
            if (item.getType() == Common.ANSWER_TYPE.RIGHT_ANSWER) {
                Common.rightAnswerCount++;
            } else if (item.getType() == Common.ANSWER_TYPE.WRONG_ANSWER) {
                Common.wrongAnswerCount++;
            }
        }
    }

    private void generateFragmentList() {
        for (int i = 0; i < Common.questionList.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            QuestionFragment questionFragment = new QuestionFragment();
            questionFragment.setArguments(bundle);
            Common.fragmentList.add(questionFragment);
        }
    }

    private void countTimer() {
        if (Common.countDownTimer == null) {
            Common.countDownTimer = new CountDownTimer(Common.TOTAL_TIME, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    txtTimer.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    timePlay -= 1000;

                }

                @Override
                public void onFinish() {
                    //Finish game
                    finishGame();
                }
            }.start();
        } else {
            Common.countDownTimer.cancel();
            Common.countDownTimer = new CountDownTimer(Common.TOTAL_TIME, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    txtTimer.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(1),
                            TimeUnit.MILLISECONDS.toSeconds(1),
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(1))));
                    timePlay -= 1000;

                }

                @Override
                public void onFinish() {
                    //Finish game
                }
            }.start();
        }
    }

    private void takeQuestions() {
        Common.questionList = DBHelper.getInstance(this).getQuestionByCategory(Common.categorySelected.getId());
        if (Common.questionList.size() == 0) {
            //If no question
            new MaterialStyledDialog.Builder(this)
                    .setTitle("Opps!")
                    .setIcon(R.drawable.ic_sentiment_very_dissatisfied_black_24dp)
                    .setDescription("We don't have any question in this " + Common.categorySelected.getName() + " category")
                    .setPositiveText("OK")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();
        } else {
            if (Common.answerSheetList.size() > 0) {
                Common.answerSheetList.clear();
            }

            //Get answer sheet item for  question
            for (int j = 0; j < Common.questionList.size(); j++) {
                Common.answerSheetList.add(new CurrentQuestion(j, Common.ANSWER_TYPE.NO_ANSWER));
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (Common.countDownTimer != null) {
            Common.countDownTimer.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menuWrongAnswer);
        LinearLayout linearLayout = (LinearLayout) item.getActionView();
        txtWrongAnser = (TextView) linearLayout.findViewById(R.id.txtWrongAnswer);
        txtWrongAnser.setText(String.valueOf(0));

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuFinishGame) {
            if (!isAnswerModeView) {
                new MaterialStyledDialog.Builder(this)
                        .setTitle("Finish?")
                        .setIcon(R.drawable.ic_mood_white_24dp)
                        .setDescription("Do you really want to finish?")
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
                                finishGame();
                            }
                        })
                        .show();
            } else {
                finishGame();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void finishGame() {
        int position = viewPager.getCurrentItem();
        QuestionFragment questionFragment = Common.fragmentList.get(position);
        CurrentQuestion questionState = questionFragment.getSelectedAnswer();
        Common.answerSheetList.set(position, questionState);
        answerSheetAdapter.notifyDataSetChanged();
        answerSheetHelperAdapter.notifyDataSetChanged();

        countCorrectAnswer();

        txtRightAnswer.setText(new StringBuilder(String.format("%d", Common.rightAnswerCount))
                .append("/")
                .append(String.format("%d", Common.questionList.size()))
                .toString());
        txtWrongAnser.setText(String.valueOf(Common.wrongAnswerCount));

        if (questionState.getType() == Common.ANSWER_TYPE.NO_ANSWER) {
            questionFragment.showCorrectAnswer();
            questionFragment.disableAnswer();
        }

        Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
        Common.timer = Common.TOTAL_TIME - timePlay;
        Common.noAnswerCount = Common.questionList.size() - Common.rightAnswerCount - Common.wrongAnswerCount;
        Common.dataQuestion = new StringBuilder(new Gson().toJson(Common.answerSheetList));

        startActivityForResult(intent, CODE_GET_RESULT);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_GET_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                String action = data.getStringExtra(Common.ACTION);
                if (action == null || TextUtils.isEmpty(action)) {
                    int questionNumber = data.getIntExtra(Common.KEY_BACK_FOR_RESULT, -1);
                    viewPager.setCurrentItem(questionNumber);

                    isAnswerModeView = true;
                    Common.countDownTimer.cancel();

                    txtWrongAnser.setVisibility(View.GONE);
                    txtRightAnswer.setVisibility(View.GONE);
                    txtTimer.setVisibility(View.GONE);

                } else {
                    if (action.equals(Common.VIEW_ANSWER)) {
                        viewPager.setCurrentItem(0);

                        isAnswerModeView = true;
                        Common.countDownTimer.cancel();

                        txtWrongAnser.setVisibility(View.GONE);
                        txtRightAnswer.setVisibility(View.GONE);
                        txtTimer.setVisibility(View.GONE);

                        for (int i = 0; i < Common.fragmentList.size(); i++) {
                            Common.fragmentList.get(i).showCorrectAnswer();
                            Common.fragmentList.get(i).disableAnswer();
                        }
                    } else if (action.equals(Common.DO_QUIZ_AGAIN)) {
                        viewPager.setCurrentItem(0);

                        isAnswerModeView = false;
                        countTimer();

                        txtWrongAnser.setVisibility(View.VISIBLE);
                        txtRightAnswer.setVisibility(View.VISIBLE);
                        txtTimer.setVisibility(View.VISIBLE);

                        for (CurrentQuestion item : Common.answerSheetList) {
                            item.setType(Common.ANSWER_TYPE.NO_ANSWER);
                        }
                        answerSheetAdapter.notifyDataSetChanged();
                        answerSheetHelperAdapter.notifyDataSetChanged();

                        for (int j = 0; j < Common.fragmentList.size(); j++) {
                            Common.fragmentList.get(j).resetQuestion();
                        }
                    }
                }
            }
        }
    }
}
