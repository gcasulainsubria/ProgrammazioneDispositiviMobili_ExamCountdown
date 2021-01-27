package it.uninsubria.examcountdown;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import it.uninsubria.examcountdown.dummy.ExamListItem;

public class ItemDetailActivity extends AppCompatActivity {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_VALUE = "item_value";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;

    private ExamListItem mItem= new ExamListItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            }

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

        Bundle b = getIntent().getExtras();
        if (b!= null) {
            mItem.setExamName(b.getString(ARG_ITEM_ID));
            mItem.setExamDateStr(b.getString(ARG_ITEM_VALUE));

            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) this.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(""+mItem.getExamName()+" "+mItem.getExamDateStr());
                }

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Query examQuery = mDatabase.child("users").child(mUserId).child("exams").orderByChild("examName").equalTo(mItem.getExamName());
                    examQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });

                    Toast.makeText(ItemDetailActivity.this, "Rimosso elemento", Toast.LENGTH_SHORT).show();
                    finish();
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemListActivity.class);
                    context.startActivity(intent);
                    }
                });
            }
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, ItemListActivity.class));
            return true;
            }
        return super.onOptionsItemSelected(item);
        }
    }