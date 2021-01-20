package it.uninsubria.examcountdown;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.uninsubria.examcountdown.dummy.ExamListItem;

public class CountdownRunnable implements Runnable {
    public TextView mExamDate;
    public Date examDate = new Date();
    public Handler handler;
    //ImageView imageView;

    public CountdownRunnable(Handler handler, TextView mExamDate) {
        this.handler = handler;
        this.mExamDate = mExamDate;
        //this.imageView = imageView;
    }

    @Override
    public void run() {
        /* do what you need to do */

        long millisUntilFinished = examDate.getTime() - Calendar.getInstance().getTime().getTime();

        long seconds = millisUntilFinished / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        String time = days + " " + "gg" + " :" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60;
        mExamDate.setText(time);

        millisUntilFinished -= 1000;

        Log.d("CountdownRunnable", time);
        //imageView.setX(imageView.getX() + seconds);
        /* and here comes the "trick" */
        handler.postDelayed(this, 1000);
    }

}