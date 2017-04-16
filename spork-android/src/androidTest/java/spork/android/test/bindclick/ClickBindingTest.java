package spork.android.test.bindclick;

import android.support.test.espresso.PerformException;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import spork.android.test.bindclick.domain.TestActivity;
import spork.android.test.bindclick.domain.TestFaultyClickArgumentsView;
import spork.android.test.bindclick.domain.TestFaultyView;
import spork.android.test.bindclick.domain.TestFragment;
import spork.android.test.bindclick.domain.TestPojo;
import spork.android.test.bindclick.domain.TestView;
import spork.BindFailed;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ClickBindingTest {

    @Rule
    public ActivityTestRule<TestActivity> activityRule = new ActivityTestRule<>(TestActivity.class);

    @Test
    public void testMain() {
        TestActivity activity = activityRule.getActivity();
        TestFragment testFragment = activity.getTestFragment();
        TestView testView = activity.getTestView();

        assertNotNull(testFragment);
        assertNotNull(testView);

        testClick(activity);
        testClick(activity.getTestFragment());
        testClick(activity.getTestView());
    }

    @Test(expected = BindFailed.class)
    public void testPojo() {
        new TestPojo();
    }

    @Test(expected = BindFailed.class)
    public void testFaultyView() {
        new TestFaultyView(activityRule.getActivity());
    }

    @Test(expected = PerformException.class)
    public void testFaultyClickArgumentsView() {
        TestFaultyClickArgumentsView view = activityRule.getActivity().getTestFaultyClickArgumentsView();
        assertNotNull(view);
        onView(withId(view.getId())).perform(click());
    }

    private void testClick(ClickTestProvider provider) {
        String message = "click testing " + provider.getClass().getSimpleName();
        int view_id = provider.getClickViewResourceId();
        assertEquals(message + ": initial click count", 0, provider.getClickCount());
        onView(withId(view_id)).perform(click());
        assertEquals(message + ": click count", 1, provider.getClickCount());
    }
}
