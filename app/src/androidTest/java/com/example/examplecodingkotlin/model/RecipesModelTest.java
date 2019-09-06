package com.example.examplecodingkotlin.model;

import androidx.test.rule.ActivityTestRule;
import com.example.examplecodingkotlin.activity.RecipesActivity;
import com.example.examplecodingkotlin.dto.Result;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by fbarbieri on 2019-09-03.
 */
public class RecipesModelTest {

    private RecipesModel recipesModel;
    private static final String FIRST_TITLE = "Recipe Puppy";
    private static final String FIRST_PAGE = "1";
    private static final String EMPTY = "";
    private static final int PAGE_ITEMS_COUNT = 10;

    @Rule
    public ActivityTestRule<RecipesActivity> activityActivityTestRule =
        new ActivityTestRule<>(RecipesActivity.class);

    @Before
    public void setUp() {
        recipesModel = new RecipesModel();
    }

    @Test
    public void TestGetRecipesCompleted() {
        recipesModel.getRecipesData(FIRST_PAGE,EMPTY).test().assertCompleted();
    }

    @Test
    public void TestGetRecipesTitle() {
        final TestSubscriber<Result> subscriber = TestSubscriber.create();
        recipesModel.getRecipesData(FIRST_PAGE,EMPTY).subscribe(subscriber);
        subscriber.assertNoErrors();
        subscriber.assertCompleted();
        assertThat(subscriber.getOnNextEvents().get(0).getTitle(), is(FIRST_TITLE));
    }

    @Test
    public void TestGetRecipesListCount() {
        final TestSubscriber<Result> subscriber = TestSubscriber.create();
        recipesModel.getRecipesData(FIRST_PAGE,EMPTY).subscribe(subscriber);
        subscriber.assertNoErrors();
        subscriber.assertCompleted();
        assertThat(subscriber.getOnNextEvents().get(0).getResults().size(), is(PAGE_ITEMS_COUNT));
    }

}