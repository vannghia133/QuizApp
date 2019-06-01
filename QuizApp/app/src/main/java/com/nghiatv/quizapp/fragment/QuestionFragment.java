package com.nghiatv.quizapp.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nghiatv.quizapp.IQuestion;
import com.nghiatv.quizapp.R;
import com.nghiatv.quizapp.model.CurrentQuestion;
import com.nghiatv.quizapp.model.Question;
import com.nghiatv.quizapp.utils.Common;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class QuestionFragment extends Fragment implements IQuestion {
    TextView txtQuestionText;
    CheckBox ckbA, ckbB, ckbC, ckbD;
    FrameLayout layoutImage;
    ProgressBar progressBar;

    Question question;
    int questionIndex = -1;


    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_question, container, false);

        //Get question
        questionIndex = getArguments().getInt("index", -1);
        question = Common.questionList.get(questionIndex);
        if (question != null) {
            //Init view
            layoutImage = (FrameLayout) itemView.findViewById(R.id.layoutImage);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            if (question.isImageQuestion()) {
                ImageView imgQuestion = (ImageView) itemView.findViewById(R.id.imgQuestion);
                Picasso.get().load(question.getQuestionImage()).into(imgQuestion, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                layoutImage.setVisibility(View.GONE);
            }

            txtQuestionText = (TextView) itemView.findViewById(R.id.txtQuestionText);
            txtQuestionText.setText(question.getQuestionText());

            ckbA = (CheckBox) itemView.findViewById(R.id.ckbA);
            ckbA.setText(question.getAnswerA());
            ckbA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Common.selectedValue.add(ckbA.getText().toString());
                    } else {
                        Common.selectedValue.remove(ckbA.getText().toString());
                    }
                }
            });

            ckbB = (CheckBox) itemView.findViewById(R.id.ckbB);
            ckbB.setText(question.getAnswerB());
            ckbB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Common.selectedValue.add(ckbB.getText().toString());
                    } else {
                        Common.selectedValue.remove(ckbB.getText().toString());
                    }
                }
            });

            ckbC = (CheckBox) itemView.findViewById(R.id.ckbC);
            ckbC.setText(question.getAnswerC());
            ckbC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Common.selectedValue.add(ckbC.getText().toString());
                    } else {
                        Common.selectedValue.remove(ckbC.getText().toString());
                    }
                }
            });

            ckbD = (CheckBox) itemView.findViewById(R.id.ckbD);
            ckbD.setText(question.getAnswerD());
            ckbD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Common.selectedValue.add(ckbD.getText().toString());
                    } else {
                        Common.selectedValue.remove(ckbD.getText().toString());
                    }
                }
            });

        }

        return itemView;
    }


    @Override
    public CurrentQuestion getSelectedAnswer() {
        //This function will return state of answer
        CurrentQuestion currentQuestion = new CurrentQuestion(questionIndex, Common.ANSWER_TYPE.NO_ANSWER);
        StringBuilder result = new StringBuilder();
        if (Common.selectedValue.size() > 1) {
            //If multichoice
            //Split answer to array
            Object[] arrayAnswer = Common.selectedValue.toArray();
            for (int i = 0; i < arrayAnswer.length; i++) {
                if (i < arrayAnswer.length - 1) {
                    result.append(new StringBuilder((String) arrayAnswer[i]).substring(0, 1)).append(",");
                } else {
                    result.append(new StringBuilder((String) arrayAnswer[i]).substring(0, 1));
                }
            }
        } else if (Common.selectedValue.size() == 1) {
            Object[] arrayAnswer = Common.selectedValue.toArray();
            result.append(new StringBuilder((String) arrayAnswer[0]).substring(0, 1));
        }

        if (question != null) {
            if (!TextUtils.isEmpty(result)) {
                if (result.toString().equals(question.getCorrectAnswer())) {
                    currentQuestion.setType(Common.ANSWER_TYPE.RIGHT_ANSWER);
                } else {
                    currentQuestion.setType(Common.ANSWER_TYPE.WRONG_ANSWER);
                }
            } else {
                currentQuestion.setType(Common.ANSWER_TYPE.NO_ANSWER);
            }
        } else {
            Toast.makeText(getContext(), "Can not get question", Toast.LENGTH_SHORT).show();
        }
        Common.selectedValue.clear();

        return currentQuestion;
    }

    @Override
    public void showCorrectAnswer() {
        String[] correctAnswer = question.getCorrectAnswer().split(",");
        for (String answer : correctAnswer) {
            if (answer.equals("A")) {
                ckbA.setTypeface(null, Typeface.BOLD);
                ckbA.setTextColor(Color.RED);
            } else if (answer.equals("B")) {
                ckbB.setTypeface(null, Typeface.BOLD);
                ckbB.setTextColor(Color.RED);
            } else if (answer.equals("C")) {
                ckbC.setTypeface(null, Typeface.BOLD);
                ckbC.setTextColor(Color.RED);
            } else if (answer.equals("D")) {
                ckbD.setTypeface(null, Typeface.BOLD);
                ckbD.setTextColor(Color.RED);
            }
        }
    }

    @Override
    public void disableAnswer() {
        ckbA.setEnabled(false);
        ckbB.setEnabled(false);
        ckbC.setEnabled(false);
        ckbD.setEnabled(false);
    }

    @Override
    public void resetQuestion() {
        ckbA.setEnabled(true);
        ckbB.setEnabled(true);
        ckbC.setEnabled(true);
        ckbD.setEnabled(true);

        ckbA.setChecked(false);
        ckbB.setChecked(false);
        ckbC.setChecked(false);
        ckbD.setChecked(false);

        ckbA.setTypeface(null, Typeface.NORMAL);
        ckbA.setTextColor(Color.BLACK);
        ckbB.setTypeface(null, Typeface.NORMAL);
        ckbB.setTextColor(Color.BLACK);
        ckbC.setTypeface(null, Typeface.NORMAL);
        ckbC.setTextColor(Color.BLACK);
        ckbD.setTypeface(null, Typeface.NORMAL);
        ckbD.setTextColor(Color.BLACK);

    }
}
