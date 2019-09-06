package com.example.examplecodingkotlin.activity;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by fbarbieri on 2019-09-03.
 */
public class FavoritesActivityTest {

    @Rule
    public ActivityTestRule<RecipesActivity> activityActivityTestRuleFairst =
        new ActivityTestRule<>(RecipesActivity.class);

    @Rule
    public ActivityTestRule<FavoritesActivity> activityActivityTestRule =
        new ActivityTestRule<>(FavoritesActivity.class);

    @Test
    public void TestPressBack() {
        Espresso.pressBack();
    }

}