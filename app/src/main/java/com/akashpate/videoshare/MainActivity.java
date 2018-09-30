package com.akashpate.videoshare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    Uri selectedFile;

    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference videoDbRef = mFirebaseDatabase.getReference("url");

    //Data
    LinkedList<String> videoList = new LinkedList<>();



    RecyclerView mRecyclerView;
    VideoListAdapter videoListAdapter;
    TextView cosmicLink;
    SwipeRefreshLayout refreshVideo;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_video_feed:
                    findViewById(R.id.layout1).setVisibility(View.VISIBLE);
                    findViewById(R.id.layout2).setVisibility(View.GONE);
                    findViewById(R.id.layout3).setVisibility(View.GONE);
                    videoFeedActivity();
                    return true;

                case R.id.navigation_home:
                    findViewById(R.id.layout1).setVisibility(View.GONE);
                    findViewById(R.id.layout2).setVisibility(View.VISIBLE);
                    findViewById(R.id.layout3).setVisibility(View.GONE);
                    homeActivity();
                    return true;

                case R.id.navigation_about_us:
                    findViewById(R.id.layout1).setVisibility(View.GONE);
                    findViewById(R.id.layout2).setVisibility(View.GONE);
                    findViewById(R.id.layout3).setVisibility(View.VISIBLE);
                    aboutUsActivity();
                    return true;
            }
            return false;
        }
    };

    FloatingActionButton mfab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cosmicLink = findViewById(R.id.cosmicLink);
        cosmicLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(cosmicLink.getText().toString()));
                startActivity(contactIntent);
            }
        });

        videoDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    String url = child.getValue().toString();
                    videoList.add(url);
                    videoListAdapter.notifyDataSetChanged();
                    //Toast.makeText(MainActivity.this,url,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Get a handle to recycler view
        mRecyclerView = findViewById(R.id.videoList);
        //Create an adapter and supply the data
        videoListAdapter = new VideoListAdapter(this,videoList);
        //connect adapter to recyclerView
        mRecyclerView.setAdapter(videoListAdapter);
        //Give recycler view default layout manager with reverseLayout to view latest videos first
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        refreshVideo = findViewById(R.id.refreshVideo);

        mfab = findViewById(R.id.newUpload);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent()
                        .setType("video/*").setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent,"Select a file"),123);
            }
        });

        refreshVideo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshVideo.setRefreshing(false);
                mRecyclerView.setAdapter(videoListAdapter);
            }
        });


    }

    void videoFeedActivity(){
       // Toast.makeText(MainActivity.this,FirebaseAuth.getInstance().getCurrentUser().getEmail(),Toast.LENGTH_SHORT).show();
       // mRecyclerView.setAdapter(videoListAdapter);

    }

    void homeActivity(){

    }

    void aboutUsActivity(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123 && resultCode==RESULT_OK) {
            selectedFile = data.getData(); //The uri with the location of the file

            Intent intent = new Intent(MainActivity.this,UploadActivity.class);
            intent.putExtra("fileUrl",selectedFile.toString());
            startActivity(intent);
        }
    }


}
