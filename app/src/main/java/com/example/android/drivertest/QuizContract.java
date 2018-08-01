package com.example.android.drivertest;

import android.provider.BaseColumns;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class QuizContract implements BaseColumns {
    public QuizContract() {
    }

    public static abstract class QuizEntry implements BaseColumns {

        // Quiz table name
        public static final String QUIZ_TABLE_NAME = "car";

        // Quiz Table Columns names
        public static final String QUIZ_COLUMN_ID = "_id";
        public static final String QUIZ_COLUMN_IMAGE = "image";
        public static final String QUIZ_COLUMN_QUESTION = "question";
        public static final String QUIZ_COLUMN_RIGHT_ANSWER = "answer";
        public static final String QUIZ_COLUMN_RIGHT_ANSWER_SEC = "answerSec";
        public static final String QUIZ_COLUMN_CHOICE_A = "choiceA";
        public static final String QUIZ_COLUMN_CHOICE_B = "choiceB";
        public static final String QUIZ_COLUMN_CHOICE_C = "choiceC";
        public static final String QUIZ_COLUMN_CHOICE_D = "choiceD";
    }
}