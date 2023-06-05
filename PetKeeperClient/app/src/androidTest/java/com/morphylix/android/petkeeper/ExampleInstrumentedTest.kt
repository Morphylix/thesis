package com.morphylix.android.petkeeper

import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class LongListActivityTest {

    @Rule
    var rule: ActivityScenarioRule<LongListActivity> = ActivityScenarioRule(
        LongListActivity::class.java
    )


    @Test
    fun lastItem_NotDisplayed() {
        // Last item should not exist if the list wasn't scrolled down.
        onView(withText(LAST_ITEM_ID)).check(doesNotExist())
    }


    @Test
    fun list_Scrolls() {
        onRow(LAST_ITEM_ID).check(matches(isCompletelyDisplayed()))
    }


    @Test
    fun row_Click() {
        // Click on one of the rows.
        onRow(TEXT_ITEM_30).onChildView(withId(R.id.rowContentTextView)).perform(click())

        // Check that the activity detected the click on the first column.
        onView(ViewMatchers.withId(R.id.selection_row_value))
            .check(matches(withText(TEXT_ITEM_30_SELECTED)))
    }


    @Test
    fun toggle_Click() {
        // Click on a toggle button.
        onRow(TEXT_ITEM_30).onChildView(withId(R.id.rowToggleButton)).perform(click())

        // Check that the toggle button is checked.
        onRow(TEXT_ITEM_30).onChildView(withId(R.id.rowToggleButton)).check(matches(isChecked()))
    }


    @Test
    fun toggle_ClickDoesntPropagate() {
        // Click on one of the rows.
        onRow(TEXT_ITEM_30).onChildView(withId(R.id.rowContentTextView)).perform(click())

        // Click on the toggle button, in a different row.
        onRow(TEXT_ITEM_60).onChildView(withId(R.id.rowToggleButton)).perform(click())

        // Check that the activity didn't detect the click on the first column.
        onView(ViewMatchers.withId(R.id.selection_row_value))
            .check(matches(withText(TEXT_ITEM_30_SELECTED)))
    }

    @Test
    fun launchDialogFragmentAndVerifyUI() {
        val scenario = launchFragment<SampleDialogFragment>()

        scenario.onFragment { fragment ->
            assertThat(fragment.dialog).isNotNull()
            assertThat(fragment.requireDialog().isShowing).isTrue()
        }

        Espresso.onView(ViewMatchers.withId(R.id.textView)).inRoot(isDialog())
            .check(ViewAssertions.matches(ViewMatchers.withText("I am a fragment")));
    }

    @Test
    fun launchDialogFragmentEmbeddedToHostActivityAndVerifyUI() {
        val scenario = launchFragmentInContainer<DialogFragment>()

        scenario.onFragment { fragment ->
            assertThat(fragment.dialog).isNull()
        }

        Espresso.onView(ViewMatchers.withId(R.id.textView))
            .check(ViewAssertions.matches(ViewMatchers.withText("I am a fragment")));
    }

    private val ITEM_BELOW_THE_FOLD = 40

    @Rule
    var activityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule<MainActivity>(
            MainActivity::class.java
        )

    @Test(expected = PerformException::class)
    fun itemWithText_doesNotExist() {
        // Attempt to scroll to an item that contains the special text.
        onView(ViewMatchers.withId(R.id.recyclerView)) // scrollTo will fail the test if no item matches.
            .perform(
                RecyclerViewActions.scrollTo(
                    hasDescendant(withText("not in the list"))
                )
            )
    }

    @Test
    fun scrollToItemBelowFold_checkItsText() {
        // First scroll to the position that needs to be matched and click on it.
        onView(ViewMatchers.withId(R.id.recyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition(ITEM_BELOW_THE_FOLD, click()))

        // Match the text in an item below the fold and check that it's displayed.
        val itemElementText: String = getApplicationContext().getResources().getString(
            R.string.item_element_text
        ) + ITEM_BELOW_THE_FOLD.toString()
        onView(withText(itemElementText)).check(matches(isDisplayed()))
    }

    @Test
    fun itemInMiddleOfList_hasSpecialText() {
        // First, scroll to the view holder using the isInTheMiddle matcher.
        onView(ViewMatchers.withId(R.id.recyclerView))
            .perform(RecyclerViewActions.scrollToHolder(isInTheMiddle()))

        // Check that the item has the special text.
        val middleElementText: String =
            getApplicationContext().getResources().getString(R.string.middle)
        onView(withText(middleElementText)).check(matches(isDisplayed()))
    }

    private fun isInTheMiddle(): Matcher<CustomAdapter.ViewHolder?>? {
        return object : TypeSafeMatcher<CustomAdapter.ViewHolder?>() {
            protected fun matchesSafely(customHolder: CustomAdapter.ViewHolder): Boolean {
                return customHolder.getIsInTheMiddle()
            }

            fun describeTo(description: Description) {
                description.appendText("item in the middle")
            }
        }
    }

    companion object {
        private const val TEXT_ITEM_30 = "item: 30"
        private const val TEXT_ITEM_30_SELECTED = "30"
        private const val TEXT_ITEM_60 = "item: 60"

        // Match the last item by matching its text.
        private const val LAST_ITEM_ID = "item: 99"

        private fun onRow(str: String): DataInteraction {
            return onData(hasEntry(equalTo(LongListActivity.ROW_TEXT), `is`(str)))
        }
    }
}