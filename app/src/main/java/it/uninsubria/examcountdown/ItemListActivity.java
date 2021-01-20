package it.uninsubria.examcountdown;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import it.uninsubria.examcountdown.dummy.ExamList;
import it.uninsubria.examcountdown.dummy.ExamListItem;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private ExamList examList=new ExamList();
    Handler handler;
    private String mUserId;
    private ExamItemRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);


        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        adapter =  new ExamItemRecyclerViewAdapter(this, this, examList.getItemListValues());
        ((RecyclerView) recyclerView).setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View view) {
                addExamAlertDialog(view);
            }
        });



        /*if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }*/



        // Initialize Firebase Auth and Database Reference
        /*
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            }
        else {
            mUserId = mFirebaseUser.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            }

        mDatabase.child("users").child(mUserId).child("exams").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ExamListItem item = new ExamListItem((String) dataSnapshot.child("examname").getValue(),(Date) dataSnapshot.child("date").getValue());
                examList.addItem(item);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ExamListItem item = new ExamListItem((String) dataSnapshot.child("examname").getValue());
                examList.removeItem(item);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });*/

    }

    @Override
    protected void onPause() {
        super.onPause();

        //remove all timer before pausing app.
        adapter.clearAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void  addExamAlertDialog(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Inserisci nuovo esame");


        LinearLayout lila1= new LinearLayout(v.getContext());
        lila1.setOrientation(LinearLayout.VERTICAL);
        final EditText input1 = new EditText(v.getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input1.setInputType(InputType.TYPE_CLASS_TEXT);
        input1.setHint("Nome dell'esame");


        final TextView label = new TextView(v.getContext());
        label.setText("Seleziona Data\n");
        label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        label.setTextSize(17);
        final TextView space = new TextView(v.getContext());
        space.setText("");
        space.setTextSize(3);
        final DatePicker datePicker = new DatePicker(v.getContext());
        //datePicker.();

        //datePicker.setMinDate(Calendar.getInstance().getTime().getDate());
                /*final EditText input2 = new EditText(view.getContext());
                input2.setInputType(InputType.TYPE_CLASS_TEXT);*/

        lila1.addView(input1);
        lila1.addView(label);
        //lila1.addView(space);
        lila1.addView(datePicker);
        builder.setView(lila1);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = input1.getText().toString();
                String date = ""+datePicker.getYear()+"/"+(datePicker.getMonth()+1)+"/"+datePicker.getDayOfMonth();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd");

                try {
                    Date strDate = sdf.parse(""+datePicker.getYear()+"/"+(datePicker.getMonth()+1)+"/"+datePicker.getDayOfMonth());
                    addExamToList(str,strDate);
                    Collections.sort(examList.getItemListValues());


                    Snackbar.make(v, "Aggiunto Elemento "+ str +" "+date , Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    adapter.notifyDataSetChanged();
                    str = null;
                    date = null;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void addExamToList(String examName, Date examDate){
        ExamListItem item = new ExamListItem(examName,examDate);
        //mDatabase.child("users").child(mUserId).child("exams").push().setValue(item);
        examList.addItem(item);

    }

    /*private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new ExamItemRecyclerViewAdapter(this,this, examList.getItemListValues(), mTwoPane));
    }*/

    public static class ExamItemRecyclerViewAdapter
            extends RecyclerView.Adapter<ExamItemRecyclerViewAdapter.ViewHolder> {

        private List<ExamListItem> mValues;
        private LayoutInflater mInflater;
        private final ItemListActivity mParentActivity;
        private Handler handler = new Handler();

        public void clearAll() {
            handler.removeCallbacksAndMessages(null);
        }

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExamListItem item = (ExamListItem) view.getTag();
                /*if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.exameName);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {*/
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.getExamName());
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_VALUE, item.getExamDateStr());
                    context.startActivity(intent);
                //}
            }
        };


        ExamItemRecyclerViewAdapter(Context context,
                                    ItemListActivity parent,
                                    Collection<ExamListItem> items) {
            mValues = (List<ExamListItem>) items;
            mInflater = LayoutInflater.from(context);
            mParentActivity = parent;
            //mTwoPane = twoPane;
        }

        public void setmValues( Collection<ExamListItem> items){
            this.mValues = (List<ExamListItem>) items;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mExamName.setText(mValues.get(position).getExamName());

            /*long diffInMillies = mValues.get(position).getExamDate().getTime() - Calendar.getInstance().getTime().getTime();
            TimeUnit unit = TimeUnit.MINUTES;
            unit.convert(diffInMillies,TimeUnit.MILLISECONDS);*/
            handler.removeCallbacks(holder.countdownRunnable);

            //holder.countdownRunnable.mExamDate = timeStamp;
            holder.countdownRunnable.examDate = mValues.get(position).getExamDate();

            handler.postDelayed(holder.countdownRunnable, 100);


            //holder.mExamDate.setText(mValues.get(position).getExamDateStr());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);

        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mExamName;
            final TextView mExamDate;
            CountdownRunnable countdownRunnable;

            ViewHolder(View view) {
                super(view);
                mExamName = (TextView) view.findViewById(R.id.id_text);
                mExamDate = (TextView) view.findViewById(R.id.content);
                countdownRunnable = new CountdownRunnable(handler,mExamDate);
            }
        }
    }
}