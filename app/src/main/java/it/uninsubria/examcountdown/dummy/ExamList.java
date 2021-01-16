package it.uninsubria.examcountdown.dummy;

import androidx.annotation.ArrayRes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ExamList {

    /**
     * An array of sample (dummy) items.
     */
    public ArrayList<ExamListItem> ITEMS;

    /**
     * A map of sample (dummy) items, by ID.
     */
    private HashMap<String, ExamListItem> ITEM_MAP;

    public ExamList(){
        this.ITEM_MAP = new HashMap<String, ExamListItem>();
        this.ITEMS = new ArrayList<ExamListItem>();
        ExamListItem newItem =  new ExamListItem("testExam1", new Date(2020,2,1));
        addItem(newItem);

        newItem =  new ExamListItem("testExam2", new Date(2020,2,1));
        addItem(newItem);

        newItem =  new ExamListItem("testExam3", new Date(2020,2,1));
        addItem(newItem);
        //this.ITEM_MAP = new HashMap<String, ExamListItem>();
    }

    /*private int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }*/

    public void addItem(ExamListItem item) {
        ITEM_MAP.put(item.examName,item);
        ITEMS.add(item);
        //ITEM_MAP.put(item.id, item);
    }

    public void removeItem(ExamListItem item) {
        ITEM_MAP.remove(item.examName);
        for(int i =0;i<ITEMS.size();i++){
            if(ITEMS.get(i).examName.equals(item.examName)){
                ITEMS.remove(i);
                }
        }
        //ITEM_MAP.put(item.id, item);
    }

    public HashMap<String, ExamListItem> getItemMap (){
        return this.ITEM_MAP;
    }

    public Collection<ExamListItem> getItemListValues (){
        return ITEMS;
        /*ArrayList<ExamListItem> collection = new ArrayList<ExamListItem>();

        for(ExamListItem item : ITEM_MAP.values()){
            collection.add(item);
        }
        return collection;*/
    }

    /*private ExamListItem createDummyItem(int position) {
       return new ExamListItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }*/

    /*private String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }*/

}