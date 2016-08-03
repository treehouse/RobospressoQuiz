package com.teamtreehouse.robospressoquizapp

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class)
class RobolectricTest {
    lateinit var activity: MainActivity
    var questionText = ""
    val buttons: MutableMap<Button, String> = mutableMapOf()
    val correctAnswerButtons: MutableList<Button> = mutableListOf()
    var submitButton: Button? = null

    @Before
    fun setUp() {
        activity = Robolectric.setupActivity(MainActivity::class.java)
    }

    @Test
    fun theTest() {
        val root: ViewGroup = activity.findViewById(android.R.id.content) as ViewGroup
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

            correctAnswerButtons[0].performClick()
            submitButton?.performClick()
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
