package com.example.examplecodingkotlin.activity;

import android.util.Log;
import android.view.View;
import androidx.annotation.VisibleForTesting;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import com.example.examplecodingkotlin.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by fbarbieri on 2019-09-02.
 */
@RunWith(AndroidJUnit4.class)
public class RecipesActivityTest {

    private static final int MILLIS = 3000;
    private static final String SEARCH_TEXT = "ice";

    @Rule
    public ActivityTestRule<RecipesActivity> activityActivityTestRule =
        new ActivityTestRule<>(RecipesActivity.class);

    @Test
    public void TestShowRecyclerView () {
        onView(withId(R.id.recyclerview_recipes)).check(matches(isDisplayed()));
    }

    @Test
    public void TestClickRecyclerView () {
        onView(withId(R.id.recyclerview_recipes)).perform(click());
    }

    @Test
    public void TestOpenFavorites() {
        onView(withId(R.id.menu_favorites)).perform(click());
    }

    @Test
    public void TestSearch() {
        onView(withId(R.id.etSearch)).perform(click());
        onView(withId(R.id.etSearch)).perform(typeText(SEARCH_TEXT));
        try {
            Thread.sleep(MILLIS);
        } catch (final InterruptedException e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
    }

    @VisibleForTesting
    static class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        RecyclerViewItemCountAssertion(final int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(final View view, final NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }
            final RecyclerView recyclerView = (RecyclerView) view;
            final RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter != null ? adapter.getItemCount() : 0, is(expectedCount));
        }
    }
}