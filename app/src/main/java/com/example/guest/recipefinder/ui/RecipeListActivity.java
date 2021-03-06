package com.example.guest.recipefinder.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.guest.recipefinder.R;
import com.example.guest.recipefinder.adapters.RecipeListAdapter;
import com.example.guest.recipefinder.models.Recipe;
import com.example.guest.recipefinder.services.FoodService;

import java.io.IOException;
import java.util.*;
import java.lang.*;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RecipeListActivity extends BaseActivity {
    public static final String TAG = RecipeListActivity.class.getSimpleName();
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    private RecipeListAdapter mAdapter;

    public ArrayList<Recipe> mRecipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        ButterKnife.bind(this);

        Intent recipesIntent = getIntent();
        String ingredient1 = recipesIntent.getStringExtra("ingredient1");
        //String ingredient2 = recipesIntent.getStringExtra("ingredient2");
        String idxValue = recipesIntent.getStringExtra("idxValue");
        String idxValue1 = recipesIntent.getStringExtra("idxValue1");
        //String calRange = recipesIntent.getStringExtra("calRange");

        getRecipes(ingredient1, idxValue,idxValue1);
    }

    private void getRecipes(String ingredient1, String idxValue,String idxValue1) {
        final FoodService foodService = new FoodService();

        foodService.findRecipes(ingredient1, idxValue,idxValue1, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                mRecipes = foodService.processResults(response);


                RecipeListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new RecipeListAdapter(getApplicationContext(), mRecipes);
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RecipeListActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                    }
                });

            }
        });
    }


}