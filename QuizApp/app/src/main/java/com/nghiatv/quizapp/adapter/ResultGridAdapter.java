package com.nghiatv.quizapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nghiatv.quizapp.R;
import com.nghiatv.quizapp.model.CurrentQuestion;
import com.nghiatv.quizapp.utils.Common;

import java.util.List;

public class ResultGridAdapter extends RecyclerView.Adapter<ResultGridAdapter.MyViewHolder> {
    Context context;
    List<CurrentQuestion> currentQuestionList;

    public ResultGridAdapter(Context context, List<CurrentQuestion> currentQuestionList) {
        this.context = context;
        this.currentQuestionList = currentQuestionList;
    }

    @NonNull
    @Override
    public ResultGridAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_result, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultGridAdapter.MyViewHolder myViewHolder, int i) {
        Drawable img;

        myViewHolder.btnQuestion.setText(new StringBuilder("Question ").append(currentQuestionList.get(i).getQuestionIndex() + 1));
        if (currentQuestionList.get(i).getType() == Common.ANSWER_TYPE.RIGHT_ANSWER) {
            myViewHolder.btnQuestion.setBackgroundColor(Color.GREEN);
            img = context.getResources().getDrawable(R.drawable.ic_check_white_24dp);
            myViewHolder.btnQuestion.setCompoundDrawablesWithIntrinsicBounds(null, null, null, img);

        } else if (currentQuestionList.get(i).getType() == Common.ANSWER_TYPE.WRONG_ANSWER) {
            myViewHolder.btnQuestion.setBackgroundColor(Color.RED);
            img = context.getResources().getDrawable(R.drawable.ic_clear_white_24dp);
            myViewHolder.btnQuestion.setCompoundDrawablesWithIntrinsicBounds(null, null, null, img);
        } else {
            img = context.getResources().getDrawable(R.drawable.ic_error_outline_white_24dp);
            myViewHolder.btnQuestion.setCompoundDrawablesWithIntrinsicBounds(null, null, null, img);
        }
    }

    @Override
    public int getItemCount() {
        return currentQuestionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button btnQuestion;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnQuestion = (Button) itemView.findViewById(R.id.btnQuestion);
            btnQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocalBroadcastManager.getInstance(context).
                            sendBroadcast(new Intent(Common.KEY_BACK_FOR_RESULT).putExtra(Common.KEY_BACK_FOR_RESULT,
                                    currentQuestionList.get(getAdapterPosition()).getQuestionIndex()));
                }
            });
        }
    }
}
