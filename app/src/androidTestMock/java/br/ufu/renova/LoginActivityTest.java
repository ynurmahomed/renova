package br.ufu.renova;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import br.ufu.renova.login.LoginActivity;
import br.ufu.renova.util.EspressoIdlingResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by yassin on 10/22/16.
 */

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Before
    public void before() {
        Espresso.registerIdlingResources(EspressoIdlingResource.getIdlingResource());
        clearSharedPreferences();
    }

    @After
    public void after() {
        clearSharedPreferences();
        Espresso.unregisterIdlingResources(EspressoIdlingResource.getIdlingResource());
    }

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void mostraToastAoFazerLoginSemUsername() {
        onView(withId(R.id.login_button)).perform(click());
        onView(withText(R.string.message_login_empty))
            .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
            .check(matches(isDisplayed()));
    }

    @Test
    public void mostraToastAoFazerLoginSemPassword() {
        onView(withId(R.id.login_edit_text)).perform(typeText("login"));
        onView(withId(R.id.login_button)).perform(click());
        onView(withText(R.string.message_password_empty))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void fazLoginAoClicarNoBotaoEntrar() {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);

        onView(withId(R.id.login_edit_text)).perform(typeText("login"));
        onView(withId(R.id.password_edit_text)).perform(typeText("password"));
        onView(withId(R.id.login_button)).perform(click());

        ViewInteraction recyclerView = onView(withId(R.id.books_recycler_view));

        recyclerView.check(matches(hasDescendant(withText("Effective Java"))));
        recyclerView.check(matches(hasDescendant(withText("Joshua Bloch"))));
        recyclerView.check(matches(hasDescendant(withText("Machine Learning"))));

    }

    private void clearSharedPreferences() {
        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }

}