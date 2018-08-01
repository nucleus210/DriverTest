package com.example.android.drivertest;

// Created by Kiril Kamenov on 25.1.2018 г..

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Import Static variables from Quiz Contractor
import static com.example.android.drivertest.QuizContract.QuizEntry.QUIZ_COLUMN_CHOICE_A;
import static com.example.android.drivertest.QuizContract.QuizEntry.QUIZ_COLUMN_CHOICE_B;
import static com.example.android.drivertest.QuizContract.QuizEntry.QUIZ_COLUMN_CHOICE_C;
import static com.example.android.drivertest.QuizContract.QuizEntry.QUIZ_COLUMN_CHOICE_D;
import static com.example.android.drivertest.QuizContract.QuizEntry.QUIZ_COLUMN_ID;
import static com.example.android.drivertest.QuizContract.QuizEntry.QUIZ_COLUMN_IMAGE;
import static com.example.android.drivertest.QuizContract.QuizEntry.QUIZ_COLUMN_QUESTION;
import static com.example.android.drivertest.QuizContract.QuizEntry.QUIZ_COLUMN_RIGHT_ANSWER;
import static com.example.android.drivertest.QuizContract.QuizEntry.QUIZ_COLUMN_RIGHT_ANSWER_SEC;
import static com.example.android.drivertest.QuizContract.QuizEntry.QUIZ_TABLE_NAME;

/**
 * Create new class DataBaseHelper extend SQLiteOpenHelper to
 * manage database creation and version management.
 */


@SuppressWarnings("WeakerAccess")
class DataBaseHelper extends SQLiteOpenHelper {

    //destination path (location) of our database on device
    private static String DB_PATH;

    // Database name
    private static final String DB_NAME = "driverQuiz.db";

    // Database version
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase mDataBase;
    private final Context mContext;

    /**
     * Data Base Helper
     */

    @SuppressLint("SdCardPath")
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);

        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir
                    + context.getString(R.string.database_dir);
        } else {
            DB_PATH = context.getString(R.string.database_path)
                    + context.getPackageName()
                    + context.getString(R.string.database_dir);
        }
        this.mContext = context;
    }

    /**
     * Create Database activity
     */

    public void createDataBase() {

        boolean mDataBaseExist = checkDataBase();

        //Check database is exist and copy it from the assets folder.
        if (!mDataBaseExist) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDataBase(); // Copy the database from asset directory
                Log.e("DataBase", "Database have been successfully copied");
            } catch (IOException mIOException) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check that the database exists here: /data/data/projectName/databases/DatabaseNAME
     */

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        Log.v("dbFile", dbFile + "   " + dbFile.exists());
        return dbFile.exists();
    }

    /**
     * Copy the database from assets directory
     */

    private void copyDataBase() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    /**
     * Method is used to oped database.
     */

    public void openDataBase() throws SQLException {
        String mPath = DB_PATH + DB_NAME;
        //Log.v("mPath", mPath);
        mDataBase = SQLiteDatabase.openDatabase(
                mPath,
                null,
                SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    /**
     * Method is used to close database.
     */

    @Override
    public synchronized void close() {
        if (mDataBase != null) mDataBase.close();
        super.close();
    }

    /**
     * Start onCreate method database.
     */
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO
    }

    /**
     * Method is used to update database
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
    }

    /**
     * Adding new question to database
     */

    public void addQuestion(QuizQuestions quizQuestions) {

        SQLiteDatabase dBase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //insert new row in table
        values.put(QUIZ_COLUMN_IMAGE, quizQuestions.get_image());
        values.put(QUIZ_COLUMN_QUESTION, quizQuestions.get_question());
        values.put(QUIZ_COLUMN_RIGHT_ANSWER, quizQuestions.get_answer());
        values.put(QUIZ_COLUMN_RIGHT_ANSWER_SEC, quizQuestions.get_answerSec());
        values.put(QUIZ_COLUMN_CHOICE_A, quizQuestions.get_choiceA());
        values.put(QUIZ_COLUMN_CHOICE_B, quizQuestions.get_choiceB());
        values.put(QUIZ_COLUMN_CHOICE_C, quizQuestions.get_choiceC());
        values.put(QUIZ_COLUMN_CHOICE_D, quizQuestions.get_choiceD());

        // Inserting Row into database
        dBase.insert(QUIZ_TABLE_NAME, null, values);
        //close database
        dBase.close();
    }

    /**
     * Count Number of Entries in a SQLite Table.
     */

    public int getQuizCount() {
        SQLiteDatabase dBase = this.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + QUIZ_TABLE_NAME + " WHERE " + QUIZ_COLUMN_ID;
        Cursor cursor = dBase.rawQuery(countQuery, null);
        String number;

        // Move cursor in table rows and increment counted variable foo
        int mRowNum = 1;
        if (cursor.moveToFirst()) {
            do {
                number = cursor.getString(0);
                mRowNum = Integer.parseInt(number);
                mRowNum++;
            } while (cursor.moveToNext());
        }

        cursor.close();         // close cursor
        dBase.close();          // close database
        return mRowNum;         // return number of rows

    }

    /**
     * Return all rows data from database.
     */

    public List<QuizQuestions> getAllRows() {
        List<QuizQuestions> quizList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + QUIZ_TABLE_NAME;
        SQLiteDatabase dBase = this.getReadableDatabase();
        Cursor cursor = dBase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                QuizQuestions quizQuestions = new QuizQuestions();
                quizQuestions.set_ID(Integer.parseInt(cursor.getString(0)));
                quizQuestions.set_image(cursor.getBlob(1));
                quizQuestions.set_question(cursor.getString(2));
                quizQuestions.set_answer(cursor.getString(3));
                quizQuestions.set_answerSec(cursor.getString(4));
                quizQuestions.set_choiceA(cursor.getString(5));
                quizQuestions.set_choiceB(cursor.getString(6));
                quizQuestions.set_choiceC(cursor.getString(7));
                quizQuestions.set_choiceD(cursor.getString(8));

                // Adding Quiz List
                quizList.add(quizQuestions);
            } while (cursor.moveToNext());
        }

        cursor.close();         // close cursor
        dBase.close();          // close database
        return quizList;        // return Quiz List

    }

    /**
     * Return dynamic rows data from database. Data with rows id is passed to this method.
     * Then data is converted to be used in SQL statement.
     * Query database for specified criteria "_id"
     */

    public ArrayList<QuizQuestions> getRowById(String[] correctIds) {
        // Select All Query
        ArrayList<QuizQuestions> quizList = new ArrayList<>();
        SQLiteDatabase dBase = this.getReadableDatabase();
        String inClause = Arrays.toString(correctIds);

        //at this point inClause will look like "[23,343,33,55,43]"
        //replace the brackets with parentheses
        inClause = inClause.replace("[", "(");
        inClause = inClause.replace("]", ")");

        //now inClause will look like  "(23,343,33,55,43)" so use it to construct your SELECT

        String select = "SELECT * FROM "
                + QUIZ_TABLE_NAME
                + " WHERE "
                + QUIZ_COLUMN_ID
                + " IN "
                + inClause;

        Cursor cursor = dBase.rawQuery(select, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                QuizQuestions quizQuestions = new QuizQuestions();
                quizQuestions.set_ID(Integer.parseInt(cursor.getString(0)));
                quizQuestions.set_image(cursor.getBlob(1));
                quizQuestions.set_question(cursor.getString(2));
                quizQuestions.set_answer(cursor.getString(3));
                quizQuestions.set_answerSec(cursor.getString(4));
                quizQuestions.set_choiceA(cursor.getString(5));
                quizQuestions.set_choiceB(cursor.getString(6));
                quizQuestions.set_choiceC(cursor.getString(7));
                quizQuestions.set_choiceD(cursor.getString(8));

                // Adding Quiz List
                quizList.add(quizQuestions);
            } while (cursor.moveToNext());
        }

        cursor.close();         // close cursor
        dBase.close();          // close database
        return quizList;        // return Quiz List

    }
    public String[] getSecondaryAnswerIds(){
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT "+QUIZ_COLUMN_ID+" FROM "
                        + QUIZ_TABLE_NAME
                        + " WHERE "
                        + QUIZ_COLUMN_RIGHT_ANSWER_SEC
                        + " IS NOT NULL ",
                        null);

        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return names.toArray(new String[names.size()]);
    }
}











