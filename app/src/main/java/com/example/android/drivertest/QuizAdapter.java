package com.example.android.drivertest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public class QuizAdapter extends ArrayAdapter<QuizQuestions> {

    public QuizAdapter(Context context, ArrayList<QuizQuestions> questions) {
        super(context, R.layout.list_questions, questions);
    }

    public class Holder {
        TextView mQuestion;
        TextView mCorAnswer;
        TextView mCorAnswerSec;
        TextView mOptA;
        TextView mOptB;
        TextView mOptC;
        TextView mOptD;
        ImageView pic;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        // Get the data item for this position
        QuizQuestions data = getItem(position);

        Holder viewHolder; // view lookup cache stored in tag

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            viewHolder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_questions, parent, false);
            viewHolder.pic = convertView.findViewById(R.id.imgView);
            viewHolder.mQuestion = convertView.findViewById(R.id.ques_view);
            viewHolder.mCorAnswer = convertView.findViewById(R.id.correct_answers_view);
            viewHolder.mCorAnswerSec = convertView.findViewById(R.id.correct_answers_view_sec);

            viewHolder.mOptA = convertView.findViewById(R.id.opt_a);
            viewHolder.mOptB = convertView.findViewById(R.id.opt_b);
            viewHolder.mOptC = convertView.findViewById(R.id.opt_c);
            viewHolder.mOptD = convertView.findViewById(R.id.opt_d);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Holder) convertView.getTag();
        }
        if (data != null) {
            viewHolder.pic.setImageBitmap(convertToBitmap(data.get_image()));
            viewHolder.mQuestion.setText(data.get_question());
            viewHolder.mCorAnswer.setText(data.get_answer());
            if (viewHolder.mCorAnswerSec != null) {
                viewHolder.mCorAnswerSec.setText(data.get_answerSec());
            }
            viewHolder.mOptA.setText(data.get_choiceA());
            viewHolder.mOptB.setText(data.get_choiceB());
            viewHolder.mOptC.setText(data.get_choiceC());
            if (viewHolder.mOptD != null) {
                viewHolder.mOptD.setText(data.get_choiceD());
            }
        }
        // Return the completed view to render on screen
        return convertView;
    }

    /**
     * Method is used to get bitmap image from byte array
     */

    private Bitmap convertToBitmap(byte[] b) {
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }
}

