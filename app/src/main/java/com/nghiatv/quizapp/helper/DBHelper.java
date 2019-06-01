package com.nghiatv.quizapp.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nghiatv.quizapp.model.Category;
import com.nghiatv.quizapp.model.Question;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteAssetHelper {
    private static final String DB_NAME = "Quiz.db";
    private static final int DB_VER = 1;

    private static final String DB_TABLE_CATEGORY = "Category";
    private static final String DB_COLUMN_ID_TABLE_CATEGORY = "ID";
    private static final String DB_COLUMN_NAME_TABLE_CATEGORY = "Name";
    private static final String DB_COLUMN_IMAGE_TABLE_CATEGORY = "Image";

    private static final String DB_TABLE_QUESTION = "Question";
    private static final String DB_COLUMN_ID_TABLE_QUESTION = "ID";
    private static final String DB_COLUMN_QUESTION_TEXT_TABLE_QUESTION = "QuestionText";
    private static final String DB_COLUMN_QUESTION_IMAGE_TABLE_QUESTION = "QuestionImage";
    private static final String DB_COLUMN_ANSWER_A_TABLE_QUESTION = "AnswerA";
    private static final String DB_COLUMN_ANSWER_B_TABLE_QUESTION = "AnswerB";
    private static final String DB_COLUMN_ANSWER_C_TABLE_QUESTION = "AnswerC";
    private static final String DB_COLUMN_ANSWER_D_TABLE_QUESTION = "AnswerD";
    private static final String DB_COLUMN_CORRECT_ANSWER_TABLE_QUESTION = "CorrectAnswer";
    private static final String DB_COLUMN_IS_IMAGE_QUESTION_TABLE_QUESTION = "IsImageQuestion";
    private static final String DB_COLUMN_CATEGORY_ID_TABLE_QUESTION = "CategoryID";

    private static DBHelper instance;

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    /**
     * Get all category from DB
     */
    public List<Category> getAllCategories() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DB_TABLE_CATEGORY, null);
        List<Category> categories = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Category category = new Category(cursor.getInt(cursor.getColumnIndex(DB_COLUMN_ID_TABLE_CATEGORY)),
                        cursor.getString(cursor.getColumnIndex(DB_COLUMN_NAME_TABLE_CATEGORY)),
                        cursor.getString(cursor.getColumnIndex(DB_COLUMN_IMAGE_TABLE_CATEGORY)));
                categories.add(category);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return categories;
    }

    /**
     * Get 30 question from DB by category
     */
    public List<Question> getQuestionByCategory(int category) {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM Question WHERE CategoryId = %d ORDER BY RANDOM() LIMIT 30", category), null);
        List<Question> questions = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Question question = new Question(cursor.getInt(cursor.getColumnIndex(DB_COLUMN_ID_TABLE_QUESTION)),
                        cursor.getString(cursor.getColumnIndex(DB_COLUMN_QUESTION_TEXT_TABLE_QUESTION)),
                        cursor.getString(cursor.getColumnIndex(DB_COLUMN_QUESTION_IMAGE_TABLE_QUESTION)),
                        cursor.getString(cursor.getColumnIndex(DB_COLUMN_ANSWER_A_TABLE_QUESTION)),
                        cursor.getString(cursor.getColumnIndex(DB_COLUMN_ANSWER_B_TABLE_QUESTION)),
                        cursor.getString(cursor.getColumnIndex(DB_COLUMN_ANSWER_C_TABLE_QUESTION)),
                        cursor.getString(cursor.getColumnIndex(DB_COLUMN_ANSWER_D_TABLE_QUESTION)),
                        cursor.getString(cursor.getColumnIndex(DB_COLUMN_CORRECT_ANSWER_TABLE_QUESTION)),
                        cursor.getInt(cursor.getColumnIndex(DB_COLUMN_IS_IMAGE_QUESTION_TABLE_QUESTION)) == 0 ? Boolean.FALSE : Boolean.TRUE,
                        cursor.getInt(cursor.getColumnIndex(DB_COLUMN_CATEGORY_ID_TABLE_QUESTION)));
                questions.add(question);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return questions;
    }

}
