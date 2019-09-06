package com.example.examplecodingkotlin.activity;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;
import com.example.examplecodingkotlin.R;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by fbarbieri on 2019-09-03.
 */
public class DetailActivityTest {

    @Rule
    public ActivityTestRule<RecipesActivity> activityActivityTestRuleFairst =
        new ActivityTestRule<>(RecipesActivity.class);

    @Rule
    public ActivityTestRule<DetailActivity> activityActivityTestRule =
        new ActivityTestRule<>(DetailActivity.class);

    @Test
    public void TestShowLoading() {
        onView(withId(R.id.progressBar)).check(matches(isDisplayed()));
    }

    @Test
    public void TestPressBack() {
        Espresso.pressBack();
    }

}