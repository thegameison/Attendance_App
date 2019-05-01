package com.example.attendanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassView extends AppCompatActivity {

    private RecyclerView classes;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference classRef = db.collection("Classes");

    private ClassAdapter mAdapter;

    public List<DanceClass> showClasses = new ArrayList<>();

    private List<HashMap<String,String>> nameToId = new ArrayList<>();

    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_class_view);

        initUpdateClass();

        classes = findViewById(R.id.scroll_class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAdapter = new ClassAdapter(showClasses, this);
        classes.setAdapter(mAdapter);
        Log.v("StudentView", "Before updateStudents()");

        Log.v("StudentView", "After updateStudents()");

        layoutManager = new LinearLayoutManager(getApplicationContext());
        classes.setLayoutManager(layoutManager);
        classes.setItemAnimator(new DefaultItemAnimator());
        classes.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("ClassView","The fab has been clicked");
                DialogRegisterClass dialog = new DialogRegisterClass();
                dialog.show(getSupportFragmentManager(), "");
            }
        });
    }

    public void initUpdateClass(){
        showClasses.clear();
        nameToId.clear();
        classRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    DanceClass s = documentSnapshot.toObject(DanceClass.class);
                    HashMap<String,String> classToDocId = new HashMap<>();
                    classToDocId.put(s.getClassName(),documentSnapshot.getId());
                    nameToId.add(classToDocId);
                    showClasses.add(s);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public  ClassAdapter getmAdapter(){
        return mAdapter;
    }
    public void openClass(int position){
        DanceClass current = showClasses.get(position);
        Intent i = new Intent(this, AttendanceView.class);
        i.putExtra("DanceClass", current.getClassName());

        i.putExtra("id",getClassId(current.getClassName()));
        startActivity(i);
    }
    /*public String getClassId(final String s){
        String ids;
        final List<String> id = new ArrayList<>();
        Task t = classRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    if(documentSnapshot.get("className").equals(s)){
                        id.add(documentSnapshot.getId());
                        break;
                    }
                }
            }
        });
        try {
            t.wait();
            return id.get(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hey";
    }*/
    public String getClassId(String s){
        for(HashMap<String, String> h : nameToId){
            if (h.containsKey(s)){
                return h.get(s);
            }
        }
        return null;
    }

    public List<DanceClass> getShowClasses(){
        return showClasses;
    }

}
