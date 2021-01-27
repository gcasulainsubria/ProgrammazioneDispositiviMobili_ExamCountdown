package it.uninsubria.examcountdown.dummy;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExamListItem implements Comparable<ExamListItem> {
    private String examName;
    private Date examDate;
    private String examDateStr;

    public ExamListItem() {
        //this.id = id;
        this.examDate = new Date();
        this.examDateStr = "";
        }

    public ExamListItem(String exameName, Date examDate) {
        this.examDate = examDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        this.examDateStr = sdf.format(examDate);
        this.examName = exameName;
        }

    public ExamListItem(String exameName) {
        this.examName = exameName;
        }

    public String getExamDateStr() {
        return examDateStr;
    }

    public void setExamDateStr(String examDateStr) {
        this.examDateStr = examDateStr;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate (Date var){
        this.examDate = var;
    }

    @Override
    public String toString() {
            return examDateStr+examName;
        }

    @Override
    public int compareTo(ExamListItem o) {
        return getExamDateStr().compareTo(o.getExamDateStr());
    }

    }
