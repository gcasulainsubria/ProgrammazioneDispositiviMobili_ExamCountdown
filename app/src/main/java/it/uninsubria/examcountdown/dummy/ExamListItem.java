package it.uninsubria.examcountdown.dummy;

import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExamListItem {
        //public final String id;
        public String examName;
        public Date examDate;
        public String examDateStr;
        private Calendar calendar;
        private SimpleDateFormat dateFormat;


        public ExamListItem() {
            //this.id = id;
            this.calendar = Calendar.getInstance();
            this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.examDate = new Date();
            this.examDateStr = "";
            }

        public ExamListItem(String exameName, Date examDate) {
            this.calendar = Calendar.getInstance();
            this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.examDate = examDate;
            this.examDateStr = dateFormat.format(examDate);
            this.examName = exameName;

        }

        public ExamListItem(String exameName) {
            //this.id = id;
            this.examName = exameName;
            //this.exameDate = exameDate;
        }



        @Override
        public String toString() {
            return examName;
        }
}
