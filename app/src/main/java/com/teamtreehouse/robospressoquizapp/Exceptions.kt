package com.teamtreehouse.robospressoquizapp

class NoCorrectAnswersException(question: String, answerChoices: MutableList<String>): Exception() {
    override val message = "Uh oh! It looks like there's not a correct answer." +
            "\n|       Question: $question" +
            "\n|       Answer Choices: $answerChoices"
}

class DuplicateCorrectAnswersException(question: String, answerChoices: MutableList<String>): Exception() {
    override val message = "Uh oh! It looks like there's more than one correct answer here." +
            "\n|       Question: $question" +
            "\n|       Answer Choices: $answerChoices"
}


