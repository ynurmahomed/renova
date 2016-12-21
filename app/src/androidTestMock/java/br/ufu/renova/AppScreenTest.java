package br.ufu.renova;

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
import static android.support.test.espresso.intent.matcher.IntentMatchers.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.*;

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
        clearSharedPreferences();
    }

    @After
    public void after() {
        Espresso.unregisterIdlingResources(EspressoIdlingResource.getIdlingResource());
        clearSharedPreferences();
    }

    @Test
    public void mostraListadeLivros() {
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
    public void renovaUmLivroAoClicar() {
        onView(withId(R.id.books_recycler_view))
                .check(matches(atPosition(0, hasDescendant(withText("24 de novembro")))))
                .perform(actionOnItemAtPosition(0, click()))
                .check(matches(atPosition(0, hasDescendant(withText("01 de dezembro")))));
    }

    @Test
    public void mostraTelaDePreferenciasAoArrastar() {
        onView(withId(R.id.app_view_pager)).perform(swipeLeft());
        onView(withId(R.id.notification_preference)).check(matches(isDisplayed()));
        onView(withId(R.id.share_preference)).check(matches(isDisplayed()));
        onView(withId(R.id.rate_renovapp)).check(matches(isDisplayed()));
        onView(withId(R.id.logout_preference)).check(matches(isDisplayed()));
    }


    @Test
    public void alteraAntecedenciaDasNotificacoes() {
        int numDays = 6;
        onView(withId(R.id.app_view_pager)).perform(swipeLeft());
        // Usando sleep porque o espresso não espera a animação do swipe terminar
        // antes de executar o click, outra solução seria usar IdlingResource
        SystemClock.sleep(500);
        onView(withId(R.id.notification_preference)).perform(click());
        onView(withId(R.id.number_picker)).perform(pickNumber(numDays));
        onView(withText(R.string.number_picker_dialog_positive_text)).perform(click());
        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String quantityString = ctx.getResources().getQuantityString(R.plurals.config_notifications, numDays, numDays);
        onView(withId(R.id.notification_preference))
                .check(matches(hasDescendant(withText(quantityString))));
    }

    @Test
    public void mostraTelaParaCompartilhar() {
        onView(withId(R.id.app_view_pager)).perform(swipeLeft());
        // Usando sleep porque o espresso não espera a animação do swipe terminar
        // antes de executar o click, outra solução seria usar IdlingResource
        SystemClock.sleep(500);
        onView(withId(R.id.share_preference)).perform(click());
        intended(chooser(allOf(
                hasAction(equalTo(Intent.ACTION_SEND)),
                hasType("text/plain"),
                hasExtra(Intent.EXTRA_TEXT, "Renove seus livros na biblioteca UFU com um toque - https://play.google.com/store/apps/details?id=br.ufu.renova"))));
    }

    @Test
    @Ignore("Testar com Google APIs image")
    @Suppress
    public void mostraTelaParaAvaliar() {
        onView(withId(R.id.app_view_pager)).perform(swipeLeft());
        // Usando sleep porque o espresso não espera a animação do swipe terminar
        // antes de executar o click, outra solução seria usar IdlingResource
        SystemClock.sleep(500);
        onView(withId(R.id.rate_renovapp)).perform(click());
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse("market://details?id=br.ufu.renova"))));
    }

    @Test
    public void fazLogout() {
        onView(withId(R.id.app_view_pager)).perform(swipeLeft());
        // Usando sleep porque o espresso não espera a animação do swipe terminar
        // antes de executar o click, outra solução seria usar IdlingResource
        SystemClock.sleep(500);
        onView(withId(R.id.logout_preference)).perform(click());
        onView(withText("Sair")).perform(click());
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }

    public static Matcher<Intent> chooser(Matcher<Intent> matcher) {
        return allOf(hasAction(Intent.ACTION_CHOOSER), hasExtra(is(Intent.EXTRA_INTENT), matcher));
    }

    public static Matcher<View> atPosition(final int position, final Matcher<View> itemMatcher) {
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

    private void clearSharedPreferences() {
        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
