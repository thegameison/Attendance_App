package com.example.attendanceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class TakeAttendance extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference classRef = db.collection("Classes");

    public RecyclerView recyclerView;
    private CheckboxAdapter adapter;
    private LinearLayoutManager layoutManager;

    private List<String> students = new ArrayList<>();

    private String className;
    private String classHash;

    private Calendar date = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_take_attendance);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            className = extras.getString("ClassName");
            int year = extras.getInt("Year");
            Log.v("TakeAttendance", "value of year: " + year);
            int day = extras.getInt("Day");
            int month = extras.getInt("Month");
            date.set(YEAR,year);
            date.set(DAY_OF_MONTH,day);
            date.set(MONTH,month);
            classHash = extras.getString("Id");
        }
        Log.v("Take Attendance", "Value of className from the intent: " + classHash);
        recyclerView = findViewById(R.id.attendance_recyclerView);
        adapter = new CheckboxAdapter();
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        Button btnSubmit = findViewById(R.id.submit_attendance);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.loadStudents(students);
        populateStudentList(classHash);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                List<String> students = adapter.getStudentNames();
                SparseBooleanArray attendance = adapter.getCheckBoxStates();
                Map<String, Boolean> classAttendance = new HashMap<>();
                for (int i = 0; i < students.size(); i++){
                    classAttendance.put(students.get(i),attendance.get(i));
                }
                classRef.document(classHash).collection("Attendance")
                        .document(dateFormat.format(date.getTime())).set(classAttendance);
                returnToMainActivity();
            }
        });
    }

    /*public Task getClassHash(){
        Log.v("getClassHash", "OK we are going to get the class hash");
        Task t = classRef.whereEqualTo("className", className).get();
*//*        t.addOnCompleteListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    if(documentSnapshot.get("className").equals(className)){

                        Log.v("getClassHash()","Class hash inside loop: " + classHash);
                        break;
                    }
                }
                Log.v("getClassHash()", "OK it has exited the loop. Class Hash: " + classHash);
            }
        });*//*
        t.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
            }
        })
        return t;
    }*/

    public void populateStudentList (String classHash){
        classRef.document(classHash).collection("Students").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    students.add(documentSnapshot.get("name").toString());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void returnToMainActivity(){
        Toast.makeText(this, "Attendance Successfully Taken", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
