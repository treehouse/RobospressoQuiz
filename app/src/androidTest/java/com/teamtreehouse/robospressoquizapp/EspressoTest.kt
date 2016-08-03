package com.teamtreehouse.robospressoquizapp

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import org.hamcrest.Description
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EspressoTest {
    var questionText = ""
    val buttons: MutableMap<Button, String> = mutableMapOf()
    val correctAnswerButtons: MutableList<Button> = mutableListOf()
    var submitButton: Button? = null

    @Rule @JvmField // http://stackoverflow.com/questions/29945087/kotlin-and-new-activitytestrule-the-rule-must-be-public
    val activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun test() {
        val root: ViewGroup = activityTestRule.activity.findViewById(android.R.id.content) as ViewGroup
        for (i in 1..10) {
            questionText = ""
            buttons.clear()
            correctAnswerButtons.clear()
            submitButton = null

            analyzeViews(root)
            val numbers = Regex("\\d+").findAll(questionText)
            val addends = Pair(numbers.first().value.toInt(), numbers.last().value.toInt())
            val sum = addends.first + addends.second

            println("_____________________________")
            println(questionText)

            buttons.forEach {
                println(it.value)
                if (it.value.toInt() == sum) {
                    correctAnswerButtons.add(it.key)
                }
            }

            if (correctAnswerButtons.size == 0) {
                throw NoCorrectAnswersException(questionText, buttons.values.toMutableList())
            }

            if (correctAnswerButtons.size != 1) {
                throw DuplicateCorrectAnswersException(questionText, buttons.values.toMutableList())
            }

            val weAreConcernedAboutHowLongItTakesToRun = true
            if (weAreConcernedAboutHowLongItTakesToRun) {
                // Too fast to really see anything
                activityTestRule.runOnUiThread {
                    correctAnswerButtons[0].performClick()
                    submitButton?.performClick()
                }
            } else {
                // Slow enough to watch
                val sumMatcher = SumMatcher(sum)
                onView(sumMatcher).perform(click())
                onView(withText("Submit")).perform(ViewActions.click())
            }
        }
    }

    // Populates 'questionText', 'buttons', and 'questionText'
    fun analyzeViews(viewGroup: ViewGroup) {
        for (i in 0..viewGroup.childCount - 1) {
            val view = viewGroup.getChildAt(i)
            if (view is ViewGroup) analyzeViews(view)
            if (view is Button) {
                if (Regex("\\d+").containsMatchIn(view.text))
                    buttons.put(view, view.text.toString())
                if (view.text.contains("Submit"))
                    submitButton = view
            }
            if (view is TextView && view.text.contains("+")) {
                questionText = view.text.toString()
            }
        }
    }
}

class SumMatcher(val sum: Int) : BoundedMatcher<View, RadioButton>(RadioButton::class.java) {
    override fun matchesSafely(item: RadioButton): Boolean = item.text.contains("$sum")
    override fun describeTo(description: Description?) = Unit
}
