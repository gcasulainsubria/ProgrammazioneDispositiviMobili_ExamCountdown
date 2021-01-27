package it.uninsubria.examcountdown;
import android.os.Build;
import android.os.Handler;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import java.util.Calendar;
import java.util.Date;

public class CountdownRunnable implements Runnable {
    public TextView mExamDate;
    public Date examDate = new Date();
    public Handler handler;

    public CountdownRunnable(Handler handler, TextView mExamDate) {
        this.handler = handler;
        this.mExamDate = mExamDate;
        }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {

        Calendar now = Calendar.getInstance();
        long different = examDate.getTime() - now.getTimeInMillis();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String time = elapsedDays + " " + "gg" + " :" + elapsedHours + ":" + elapsedMinutes + ":" + elapsedSeconds;
        mExamDate.setText(time);

        handler.postDelayed(this, 1000);
        }
    }