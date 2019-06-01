package com.nghiatv.comicreader.activity;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.nghiatv.comicreader.R;
import com.nghiatv.comicreader.utils.Common;

import java.util.ArrayList;
import java.util.List;

public class FilterSearchActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);

        //Init view
        recyclerFilter = (RecyclerView) findViewById(R.id.recyclerFilterSearch);
        recyclerFilter.setHasFixedSize(true);
        recyclerFilter.setLayoutManager(new GridLayoutManager(this, 2));

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.inflateMenu(R.menu.main_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.actionFilter:
                        showFilterDialog();
                        break;

                    case R.id.actionSearch:
                        showSearchDialog();
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
    }