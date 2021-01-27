package it.uninsubria.examcountdown.dummy;

import java.util.ArrayList;
import java.util.HashMap;

public class ExamList {

    public ArrayList<ExamListItem> ITEMS;

    private HashMap<String, ExamListItem> ITEM_MAP;

    public ExamList(){
        this.ITEM_MAP = new HashMap<String, ExamListItem>();
        this.ITEMS = new ArrayList<ExamListItem>();
        }

    public void addItem(ExamListItem item) {
        ITEM_MAP.put(item.getExamName(),item);
        ITEMS.add(item);
        }

    public void removeItem(ExamListItem item) {
        ITEM_MAP.remove(item.getExamName());
        for(int i =0;i<ITEMS.size();i++){
            if(ITEMS.get(i).getExamName().equals(item.getExamName())){
                ITEMS.remove(i);
                }
            }
        }

    public void removeAt(Integer position) {
        ExamListItem tmp = ITEMS.get(position);
        ITEMS.remove(position);
        ITEM_MAP.remove(tmp.getExamName());
        }

    public HashMap<String, ExamListItem> getItemMap (){
        return this.ITEM_MAP;
        }

    public ArrayList<ExamListItem> getItemListValues (){
        return ITEMS;
        }
    }