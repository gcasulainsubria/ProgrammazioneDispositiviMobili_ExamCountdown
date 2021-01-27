package it.uninsubria.examcountdown;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import it.uninsubria.examcountdown.dummy.ExamList;
import it.uninsubria.examcountdown.dummy.ExamListItem;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;
    private ExamList examList=new ExamList();
    private ExamItemRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View view) {
                addExamAlertDialog(view);
                }
            });

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        adapter =  new ExamItemRecyclerViewAdapter(this, this, examList.getItemListValues());
        ((RecyclerView) recyclerView).setAdapter(adapter);

        // Initialize Firebase Auth and Database Reference
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
                ExamListItem item = (ExamListItem)dataSnapshot.getValue(ExamListItem.class);
                examList.addItem(item);
                Collections.sort(examList.getItemListValues());
                adapter.notifyDataSetChanged();
                }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ExamListItem item = new ExamListItem((String) dataSnapshot.child("examName").getValue());
                examList.removeItem(item);
                adapter.notifyDataSetChanged();
                }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
            });
        }

    @Override
    protected void onPause() {
        super.onPause();
        //remove all timer before pausing app.
        adapter.clearAll();
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            mFirebaseAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void  addExamAlertDialog(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Inserisci nuovo esame");

        LinearLayout lila1= new LinearLayout(v.getContext());
        lila1.setOrientation(LinearLayout.VERTICAL);
        final EditText input1 = new EditText(v.getContext());

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

        lila1.addView(input1);
        lila1.addView(label);
        lila1.addView(datePicker);
        builder.setView(lila1);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = input1.getText().toString();
                String strDate = ""+datePicker.getYear()+"/"+(datePicker.getMonth()+1)+"/"+datePicker.getDayOfMonth();

                Calendar cDate = Calendar.getInstance();
                cDate.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                cDate.set(Calendar.MONTH,datePicker.getMonth());
                cDate.set(Calendar.YEAR,datePicker.getYear());

                Date d = cDate.getTime();

                addExamToList(str,d);
                Toast.makeText(ItemListActivity.this, "Aggiunto Elemento: "+ str +" "+strDate , Toast.LENGTH_SHORT).show();

                str = null;
                d = null;
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
        mDatabase.child("users").child(mUserId).child("exams").push().setValue(item);
        }

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
                Context context = view.getContext();
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra(ItemDetailActivity.ARG_ITEM_ID, item.getExamName());
                intent.putExtra(ItemDetailActivity.ARG_ITEM_VALUE, item.getExamDateStr());
                context.startActivity(intent);
                }
            };

        ExamItemRecyclerViewAdapter(Context context,
                                    ItemListActivity parent,
                                    Collection<ExamListItem> items) {
            mValues = (List<ExamListItem>) items;
            mInflater = LayoutInflater.from(context);
            mParentActivity = parent;
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
            handler.removeCallbacks(holder.countdownRunnable);
            holder.countdownRunnable.examDate = mValues.get(position).getExamDate();
            handler.postDelayed(holder.countdownRunnable, 100);
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
            //holder.itemView.setOnLongClickListener(mOnLongClickListener);
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