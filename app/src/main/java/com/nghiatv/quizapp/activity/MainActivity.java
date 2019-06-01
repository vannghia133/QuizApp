package com.nghiatv.quizapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;

import com.nghiatv.quizapp.R;
import com.nghiatv.quizapp.adapter.CategoryAdapter;
import com.nghiatv.quizapp.helper.DBHelper;
import com.nghiatv.quizapp.utils.SpaceDecoration;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init view
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Quiz App");
        setSupportActionBar(toolbar);

        recyclerCategory = (RecyclerView) findViewById(R.id.recyclerCategory);
        recyclerCategory.setHasFixedSize(true);
        recyclerCategory.setLayoutManager(new GridLayoutManager(this, 2));

        //Get screen height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels / 8; //Map size of item in category

        CategoryAdapter adapter = new CategoryAdapter(this, DBHelper.getInstance(this).getAllCategories());
        int spaceInPixel = 4;
        recyclerCategory.addItemDecoration(new SpaceDecoration(spaceInPixel));
        recyclerCategory.setAdapter(adapter);
    }
}
