package com.nghiatv.quizapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nghiatv.quizapp.R;
import com.nghiatv.quizapp.activity.QuestionActivity;
import com.nghiatv.quizapp.model.CurrentQuestion;
import com.nghiatv.quizapp.utils.Common;

import java.util.List;

public class AnswerSheetAdapter extends RecyclerView.Adapter<AnswerSheetAdapter.MyViewHolder> {
    Context context;
    List<CurrentQuestion> currentQuestions;

    public AnswerSheetAdapter(Context context, List<CurrentQuestion> currentQuestions) {
        this.context = context;
        this.currentQuestions = currentQuestions;
    }

    @NonNull
    @Override
    public AnswerSheetAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_grid_answer_sheet, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerSheetAdapter.MyViewHolder myViewHolder, int i) {
        if (currentQuestions.get(i).getType() == Common.ANSWER_TYPE.RIGHT_ANSWER) {
            myViewHolder.questionItem.setBackgroundResource(R.drawable.grid_question_right_answer);
        } else if (currentQuestions.get(i).getType() == Common.ANSWER_TYPE.WRONG_ANSWER) {
            myViewHolder.questionItem.setBackgroundResource(R.drawable.grid_question_wrong_answer);
        } else {
            myViewHolder.questionItem.setBackgroundResource(R.drawable.grid_question_no_answer);
        }
    }

    @Override
    public int getItemCount() {
        return currentQuestions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View questionItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            questionItem = (View) itemView.findViewById(R.id.questionItem);
        }
    }
}
