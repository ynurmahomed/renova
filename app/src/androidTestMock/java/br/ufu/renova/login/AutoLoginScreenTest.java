package br.ufu.renova.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import br.ufu.renova.R;
import br.ufu.renova.TestUtils;
import br.ufu.renova.util.EspressoIdlingResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by yassin on 1/11/17.
 */
@RunWith(AndroidJUnit4.class)
public class AutoLoginScreenTest {

    private static final String USERNAME = "pref_username";

    private static final String PASSWORD = "pref_password";

    @Before
    public void before() {
        Espresso.registerIdlingResources(EspressoIdlingResource.getIdlingResource());
        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        editor.putString(USERNAME, "ada");
        editor.putString(PASSWORD, "lovelace");
        editor.commit();
    }

    @After
    public void after() {
        Espresso.unregisterIdlingResources(EspressoIdlingResource.getIdlingResource());
        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        TestUtils.clearSharedPreferences(ctx);
    }

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class, false, false);

    @Test
    public void shouldAutologinIfUserIsSaved() {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.books_recycler_view)).check(matches(isDisplayed()));
    }
}
