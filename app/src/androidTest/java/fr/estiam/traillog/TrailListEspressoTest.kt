package fr.estiam.traillog

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import fr.estiam.traillog.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

/** M5 Lab 2: implement Espresso UI tests using the existing TrailLog view IDs. */
@RunWith(AndroidJUnit4::class)
class TrailListEspressoTest {
    @get:Rule val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun catalogueIsDisplayed() {
        onView(withId(R.id.trailList)).check(matches(isDisplayed()))
    }

    @Test
    fun addTrailFromCatalogueAndFindCreatedRow() {
        val uniqueName = "Espresso Trail ${UUID.randomUUID().toString().substring(0, 8)}"

        onView(withId(R.id.addButton)).perform(click())
        onView(withId(R.id.nameInput)).perform(replaceText(uniqueName), closeSoftKeyboard())
        onView(withId(R.id.regionInput)).perform(replaceText("Alpes"), closeSoftKeyboard())
        onView(withId(R.id.distanceInput)).perform(replaceText("10.0"), closeSoftKeyboard())
        onView(withId(R.id.elevationInput)).perform(replaceText("250"), closeSoftKeyboard())
        onView(withId(R.id.saveButton)).perform(click())

        onView(withId(R.id.trailList)).check(matches(isDisplayed()))
        onView(withId(R.id.trailList)).perform(
            RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                hasDescendant(withText(uniqueName))
            )
        )
        onView(withId(R.id.trailList)).check(
            matches(hasDescendant(withText(uniqueName)))
        )
    }
}
