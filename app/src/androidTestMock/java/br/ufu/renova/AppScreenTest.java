package br.ufu.renova;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.Suppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.NumberPicker;
import br.ufu.renova.util.EspressoIdlingResource;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.*;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Created by yassin on 11/23/16.
 */

@RunWith(AndroidJUnit4.class)
public class AppScreenTest {

    @Rule
    public IntentsTestRule<AppActivity> mActivityRule = new IntentsTestRule<>(AppActivity.class);

    @Before
    public void before() {
        Espresso.registerIdlingResources(EspressoIdlingResource.getIdlingResource());
        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        TestUtils.clearSharedPreferences(ctx);
    }

    @After
    public void after() {
        Espresso.unregisterIdlingResources(EspressoIdlingResource.getIdlingResource());
        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        TestUtils.clearSharedPreferences(ctx);
    }

    @Test
    public void shouldShowBookList() {
        onView(withId(R.id.books_recycler_view))
            .check(matches(atPosition(0, hasDescendant(withText("Effective Java")))))
            .check(matches(atPosition(0, hasDescendant(withText("Joshua Bloch")))))
            .check(matches(atPosition(0, hasDescendant(withText("24 de novembro")))))

            .check(matches(atPosition(1, hasDescendant(withText("Machine Learning")))))
            .check(matches(atPosition(1, hasDescendant(withText("Tom M. Mitchell")))))
            .check(matches(atPosition(1, hasDescendant(withText("24 de novembro")))))

            .check(matches(atPosition(2, hasDescendant(withText("Introduction to Algorithms")))))
            .check(matches(atPosition(2, hasDescendant(withText("Charles E. Leiserson, Thomas H. Cormen, Clifford Stein, Ronald Rivest")))))
            .check(matches(atPosition(2, hasDescendant(withText("24 de novembro")))))

            .perform(scrollToPosition(3))
            .check(matches(atPosition(3, hasDescendant(withText("Design Patterns: Elements of Reusable Object-Oriented Software")))))
            .check(matches(atPosition(3, hasDescendant(withText("Erich Gamma, Ralph Johnson, Richard Helm, John Vlissides")))))
            .check(matches(atPosition(3, hasDescendant(withText("24 de novembro")))));
    }

    @Test
    public void shouldRenewBookExpirationDate() {
        onView(withId(R.id.books_recycler_view))
                .check(matches(atPosition(0, hasDescendant(withText("24 de novembro")))))
                .perform(actionOnItemAtPosition(0, click()))
                .check(matches(atPosition(0, hasDescendant(withText("01 de dezembro")))));
    }

    @Test
    public void shouldShowPreferencesScreen() {
        Context ctx = InstrumentationRegistry.getTargetContext();
        int numDays = ctx.getResources().getInteger(R.integer.preference_notifications_default);
        String quantityString = ctx.getResources().getQuantityString(R.plurals.config_notifications, numDays, numDays);

        onView(withId(R.id.app_view_pager)).perform(swipeLeft());

        onView(withId(R.id.list))
                .check(matches(atPosition(0, allOf(
                        hasDescendant(withText(R.string.notification_preference_title)),
                        hasDescendant(withText(quantityString)),
                        isDisplayed()))))

                .check(matches(atPosition(1, allOf(
                        hasDescendant(withText(R.string.share_preference_title)),
                        isDisplayed()))))

                .check(matches(atPosition(2, allOf(
                        hasDescendant(withText(R.string.rate_preference_title)),
                        isDisplayed()))))

                .check(matches(atPosition(3, allOf(
                        hasDescendant(withText(R.string.logout_preference_title)),
                        hasDescendant(withText(R.string.logout_preference_summary)),
                        isDisplayed()))))

                .check(matches(atPosition(4, allOf(
                        hasDescendant(withText(R.string.contribute_preference_title)),
                        hasDescendant(withText(R.string.contribute_preference_summary)),
                        isDisplayed()))))

                .check(matches(atPosition(5, allOf(
                        hasDescendant(withText(R.string.version_preference_title)),
                        hasDescendant(withText(R.string.versionName)),
                        isDisplayed()))));
    }

    @Test
    public void shouldChangeNotificationAdvancePreference() {
        int numDays = 6;
        Context ctx = InstrumentationRegistry.getTargetContext();

        navigateToSettings();

        ViewInteraction recyclerView = onView(withId(R.id.list));
        recyclerView.perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.number_picker)).perform(pickNumber(numDays));
        onView(withText(R.string.number_picker_dialog_positive_text)).perform(click());

        String quantityString = ctx.getResources().getQuantityString(R.plurals.config_notifications, numDays, numDays);
        recyclerView.check(matches(atPosition(0, allOf(
                hasDescendant(withText(R.string.notification_preference_title)),
                hasDescendant(withText(quantityString)),
                isDisplayed()))));
    }

    @Test
    public void shouldShowShareScreen() {
        Context ctx = InstrumentationRegistry.getTargetContext();
        String shareSummary = ctx.getString(R.string.share_summary);
        Intent intent = new Intent();
        intending(anyIntent()).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, intent));
        navigateToSettings();
        onView(withId(R.id.list)).perform(actionOnItemAtPosition(1, click()));
        intended(allOf(
                hasAction(equalTo(Intent.ACTION_SEND)),
                hasType("text/plain"),
                hasExtra(Intent.EXTRA_TEXT, shareSummary)));
    }

    @Test
    @Ignore("Testar com Google APIs image")
    @Suppress
    public void shouldShowAppInPlayStore() {
        Context ctx = InstrumentationRegistry.getTargetContext();
        String marketUrl = ctx.getString(R.string.market_url);
        Intent intent = new Intent();
        intending(anyIntent()).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, intent));
        navigateToSettings();
        onView(withId(R.id.list)).perform(actionOnItemAtPosition(2, click()));
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse(marketUrl))));
    }

    @Test
    public void shouldLogoutUser() {
        navigateToSettings();
        onView(withId(R.id.list)).perform(actionOnItemAtPosition(3, click()));
        onView(withText("Sair")).perform(click());
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldShowProjectPage() {
        Context ctx = InstrumentationRegistry.getTargetContext();
        String githubUrl = ctx.getString(R.string.github_url);
        Intent intent = new Intent();
        intending(anyIntent()).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, intent));
        navigateToSettings();
        onView(withId(R.id.list)).perform(actionOnItemAtPosition(4, click()));
        intended(allOf(
                hasAction(equalTo(Intent.ACTION_VIEW)),
                hasData(Uri.parse(githubUrl))));
    }

    @Test
    public void shouldSurviveOrientationChange() {
        TestUtils.rotateOrientation(TestUtils.getCurrentActivity());
        onView(withId(R.id.books_recycler_view)).check(matches(isDisplayed()));
    }

    private void navigateToSettings() {
        onView(withId(R.id.app_view_pager)).perform(swipeLeft());
        // Usando sleep porque o espresso não espera a animação do swipe terminar
        // antes de executar o click, outra solução seria usar IdlingResource
        SystemClock.sleep(500);
    }

    private static Matcher<View> atPosition(final int position, final Matcher<View> itemMatcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    private ViewAction pickNumber(final int number) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(NumberPicker.class);
            }

            @Override
            public String getDescription() {
                return "Pick a number from the NumberPicker";
            }

            @Override
            public void perform(UiController uiController, View view) {
                NumberPicker np = (NumberPicker) view;
                np.setValue(number);
            }
        };
    }
}
