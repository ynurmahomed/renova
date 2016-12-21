package br.ufu.renova.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import br.ufu.renova.R;
import br.ufu.renova.util.EspressoIdlingResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
public class LoginScreenTest {

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
        onView(ViewMatchers.withId(R.id.login_button)).perform(click());
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
        onView(withId(R.id.login_edit_text)).perform(typeText("login"));
        onView(withId(R.id.password_edit_text)).perform(typeText("password"));
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.books_recycler_view)).check(matches(isDisplayed()));
    }

    private void clearSharedPreferences() {
        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }

}