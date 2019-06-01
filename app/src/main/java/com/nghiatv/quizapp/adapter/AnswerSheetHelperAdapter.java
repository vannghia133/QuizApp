package com.nghiatv.quizapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nghiatv.quizapp.IRecyclerHelperClick;
import com.nghiatv.quizapp.R;
import com.nghiatv.quizapp.model.CurrentQuestion;
import com.nghiatv.quizapp.utils.Common;

import java.util.List;

public class AnswerSheetHelperAdapter extends RecyclerView.Adapter<AnswerSheetHelperAdapter.MyViewHolder> {
    Context context;
    List<CurrentQuestion> currentQuestionList;

    public AnswerSheetHelperAdapter(Context context, List<CurrentQuestion> currentQuestionList) {
        this.context = context;
        this.currentQuestionList = currentQuestionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_answer_sheet_helper,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.txtQuestionNumber.setText(String.valueOf(i+1)); // Show question number
        if(currentQuestionList.get(i).getType() == Common.ANSWER_TYPE.RIGHT_ANSWER)
            myViewHolder.layout_wrapper.setBackgroundResource(R.drawable.grid_question_right_answer);
        else if(currentQuestionList.get(i).getType() == Common.ANSWER_TYPE.WRONG_ANSWER)
            myViewHolder.layout_wrapper.setBackgroundResource(R.drawable.grid_question_wrong_answer);
        else
            myViewHolder.layout_wrapper.setBackgroundResource(R.drawable.grid_question_no_answer);
        myViewHolder.setiRecyclerHelperClick(new IRecyclerHelperClick() {
            @Override
            public void onClick(View view, int position) {
                //When user click to item , navigate to this question on Question Activity
                LocalBroadcastManager.getInstance(context)
                        .sendBroadcast(new Intent(
                                Common.KEY_GO_TO_QUESTION
                        ).putExtra(Common.KEY_GO_TO_QUESTION,position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return currentQuestionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtQuestionNumber;
        LinearLayout layout_wrapper;
        IRecyclerHelperClick iRecyclerHelperClick;

        public void setiRecyclerHelperClick(IRecyclerHelperClick iRecyclerHelperClick) {
            this.iRecyclerHelperClick = iRecyclerHelperClick;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestionNumber = (TextView)itemView.findViewById(R.id.txtQuestionNumber);
            layout_wrapper = (LinearLayout) itemView.findViewById(R.id.layout_wrapper);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerHelperClick.onClick(view,getAdapterPosition());
        }
    }
}
