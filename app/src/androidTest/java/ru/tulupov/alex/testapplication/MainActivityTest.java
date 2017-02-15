package ru.tulupov.alex.testapplication;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Test
    public void testShowHtmlExistWebSite() throws Exception {
        onView(withId(R.id.edt_URL)).perform(typeText("https://www.yandex.ru/"), closeSoftKeyboard());
        onView(withId(R.id.btn_show_text)).perform(click());
        onView(withId(R.id.tv_HTML_text)).check(matches(not(withText(""))));
    }

    @Test
    public void testShowHtmlNotExistWebSite() throws Exception {
        onView(withId(R.id.edt_URL)).perform(typeText("https://www.notexistedsuchwebsite.ru/"), closeSoftKeyboard());
        onView(withId(R.id.btn_show_text)).perform(click());
        onView(withId(R.id.tv_HTML_text)).check(matches(withText("")));
    }

    @Test
    public void testShowHtmlWrongUrl() throws Exception {
        onView(withId(R.id.edt_URL)).perform(typeText("some text"), closeSoftKeyboard());
        onView(withId(R.id.btn_show_text)).perform(click());
        onView(withId(R.id.tv_HTML_text)).check(matches(withText("")));
    }

    @Test
    public void testShowHtmlEmptyUrl() throws Exception {
        onView(withId(R.id.edt_URL)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.btn_show_text)).perform(click());
        onView(withId(R.id.tv_HTML_text)).check(matches(withText("")));
    }
}

