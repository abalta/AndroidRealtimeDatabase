package com.abdullahbalta.androidrealtimedatabase.activitiy;

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.abdullahbalta.androidrealtimedatabase.R;
import com.abdullahbalta.androidrealtimedatabase.adapter.InventorAdapter;
import com.abdullahbalta.androidrealtimedatabase.fragment.AddNewEntryFragment;
import com.abdullahbalta.androidrealtimedatabase.listener.RecyclerTouchListener;
import com.abdullahbalta.androidrealtimedatabase.model.InventorModel;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private RecyclerView recyclerView;
    private InventorAdapter inventorAdapter;
    private List<InventorModel> inventorModels;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private FloatingActionButton fab;
    private CustomTabsIntent.Builder customTabsIntent;

    public static int COUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        fab = (FloatingActionButton)findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        inventorModels = new ArrayList<>();
        inventorAdapter = new InventorAdapter(this, inventorModels);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(inventorAdapter);
        
        fetchFirebaseDb();

        try {
            Glide.with(this).load("http://res.cloudinary.com/abdullah-balta/image/upload/c_scale,h_360,w_1080/v1475608975/maxresdefault_ar9npf.jpg").into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getApplicationContext(), inventorModels.get(position).getInventor_name(), Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(inventorModels.get(position).getWiki());
                customTabsIntent = new CustomTabsIntent.Builder();
                customTabsIntent.setToolbarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                customTabsIntent.build().launchUrl(MainActivity.this, uri);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.scale_small_to_big, R.anim.scale_small_to_big, R.anim.scale_big_to_small, R.anim.scale_big_to_small).add(android.R.id.content, new AddNewEntryFragment(), "ADD").addToBackStack("ADD").commit();
            }
        });
    }

    private void fetchFirebaseDb() {
        myRef = database.getReference();
        myRef.child("inventors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                inventorModels.clear();
                Log.d(TAG, "Value is: " + dataSnapshot.getChildrenCount());
                COUNT = Integer.parseInt(dataSnapshot.getChildrenCount() + "");
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    InventorModel inventorModel = data.getValue(InventorModel.class);
                    Log.i(TAG, "onDataChange: " + inventorModel.getInventor_name());
                    inventorModels.add(inventorModel);
                }

                inventorAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
}
