package com.nghiatv.quizapp.utils;

import android.os.CountDownTimer;

import com.nghiatv.quizapp.fragment.QuestionFragment;
import com.nghiatv.quizapp.model.Category;
import com.nghiatv.quizapp.model.CurrentQuestion;
import com.nghiatv.quizapp.model.Question;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.TreeSet;

public class Common {
    public static final int TOTAL_TIME = 5 * 60 * 1000; //5 minute
    public static final String KEY_BACK_FOR_RESULT = "BackForResult";
    public static final String KEY_GO_TO_QUESTION = "GoToQuestion";
    public static final String ACTION = "action";
    public static final String DO_QUIZ_AGAIN = "doquizagain";
    public static final String VIEW_ANSWER = "viewanswer";

    public static Category categorySelected = new Category();
    public static List<Question> questionList = new ArrayList<>();
    public static List<CurrentQuestion> answerSheetList = new ArrayList<>();
    public static List<CurrentQuestion> answerSheetListFilter = new ArrayList<>();
    public static CountDownTimer countDownTimer;
    public static int timer;
    public static int rightAnswerCount = 0;
    public static int wrongAnswerCount = 0;
    public static int noAnswerCount = 0;
    public static StringBuilder dataQuestion = new StringBuilder();
    public static List<QuestionFragment> fragmentList = new ArrayList<>();
    public static TreeSet<String> selectedValue = new TreeSet<>();

    public enum ANSWER_TYPE {
        NO_ANSWER,
        WRONG_ANSWER,
        RIGHT_ANSWER
    }
}
