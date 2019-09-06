package com.example.examplecodingkotlin.view;

import com.example.examplecodingkotlin.dto.Recipe;
import java.util.ArrayList;

/**
 * Created by fbarbieri on 2019-08-28.
 */
public interface RecipesView {

    void showRecipesData(ArrayList<Recipe> recipes);

    void showProgressBar();

    void hideProgressBar();

    void showErrorView();

    void showEmptyView();
}
