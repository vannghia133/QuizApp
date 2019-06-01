package com.nghiatv.quizapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nghiatv.quizapp.R;
import com.nghiatv.quizapp.activity.QuestionActivity;
import com.nghiatv.quizapp.model.Category;
import com.nghiatv.quizapp.utils.Common;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>{
    Context context;
    List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_category, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.txtCategoryName.setText(categories.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardCategory;
        TextView txtCategoryName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategoryName = (TextView) itemView.findViewById(R.id.txtCategoryName);
            cardCategory = (CardView) itemView.findViewById(R.id.cardCategory);
            cardCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.categorySelected = categories.get(getAdapterPosition());
                    Intent intent = new Intent(context, QuestionActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
