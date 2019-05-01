package com.example.attendanceapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;
import java.util.List;

public class StudentView extends AppCompatActivity {
    private final String TAG = "StudentView";

    private RecyclerView students;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private StudentAdapter mAdapter;
    private ArrayAdapter<String> classAdapter;

    public List<Student> showStudents = new ArrayList<>();

    private List<String> classList = new ArrayList<>();
    private Student workingStudent;

    private RecyclerView.LayoutManager layoutManager;
    private CollectionReference studentRef = db.collection("Students");
    private CollectionReference classRef = db.collection("Classes");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);


        setContentView(R.layout.activity_student_view);

        updateStudents();
        students = findViewById(R.id.show_students);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAdapter = new StudentAdapter(showStudents, this);
        students.setAdapter(mAdapter);
        Log.v("StudentView", "Before updateStudents()");

        Log.v("StudentView", "After updateStudents()");

        layoutManager = new LinearLayoutManager(getApplicationContext());
        students.setLayoutManager(layoutManager);
        students.setItemAnimator(new DefaultItemAnimator());
        students.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        Log.v("StudentView","Number of items in Students ArrayList: " + showStudents.size());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workingStudent = new Student();
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(StudentView.this);


                View dialogView = getLayoutInflater().inflate(R.layout.register_student,null);

                builder.setTitle("Register Student");

                final EditText editName = dialogView.findViewById(R.id.name_field);
                final EditText editAge =  dialogView.findViewById(R.id.age_field);
                final EditText editEmail = dialogView.findViewById(R.id.email_field);

                Spinner classSelection = dialogView.findViewById(R.id.class_spinner);

                classSelection.setSelection(0,false);
                classAdapter = new ArrayAdapter<>(StudentView.this, android.R.layout.simple_spinner_item, classList);
                classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                populateClassList();

                classSelection.setAdapter(classAdapter);

                classSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        workingStudent.setClassName(parent.getSelectedItem().toString());
                        getStudentToClass();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


                Log.v("DialogRegisterStudent", "Where am I");


                builder.setPositiveButton("Create Student", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        workingStudent.setName(editName.getText().toString());
                        try{
                            workingStudent.setAge(Integer.parseInt(editAge.getText().toString()));
                            workingStudent.setEmail(editEmail.getText().toString());
                            if (isStudentObjectBadInput()){
                                dialog.dismiss();
                            } else{
                                studentRef.add(workingStudent);
                                Log.v("Creating Student", workingStudent.getClassName());
                                classRef.document(workingStudent.getClassID()).collection("Students").add(workingStudent);
                                StudentView.this.updateStudents();
                                StudentView.this.getmAdapter().notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        } catch (NumberFormatException e){
                            Toast.makeText(StudentView.this, "Error: Age has not been properly inputted", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setView(dialogView);
                builder.create().show();
            }
        });

        Log.v("StudentView","this activity has been loaded");
        Log.v("StudentView","Number of items in Students ArrayList (End of StudentView.onCreate()): " + showStudents.size());
    }

    private Boolean isStudentObjectBadInput() {
        if (workingStudent.getName() == null){
            Toast.makeText(this, "Error: Student Name Has Not Been Entered", Toast.LENGTH_LONG).show();
            return true;
        }
        if (workingStudent.getEmail() == null){
            Toast.makeText(this, "Error: Email Has Not Been Entered", Toast.LENGTH_SHORT).show();
            return true;
        } else if(!(workingStudent.getEmail().contains("@"))){
            Toast.makeText(this,"Error: Email is not valid",Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    public void updateStudents(){

        Log.v("StudentView","the students are starting to be updated");

        showStudents.clear();

        studentRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Student s = documentSnapshot.toObject(Student.class);
                    showStudents.add(s);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public StudentAdapter getmAdapter() {
        return mAdapter;
    }
    public void populateClassList(){
        classList.clear();
        classRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    DanceClass d = documentSnapshot.toObject(DanceClass.class);
                    classList.add(d.toString());
                    Log.v("populateclasslist","The size of classlist is: " + classList.size());
                }
                classAdapter.notifyDataSetChanged();
            }
        });
    }
    public void getStudentToClass(){
        classRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    if(documentSnapshot.get("className").equals(workingStudent.getClassName())){
                        Log.v("addStudentToClass","The document with the class name is: " + documentSnapshot.getId());
                        workingStudent.setClassID(documentSnapshot.getId());
                        Log.v("getStudentToClass",workingStudent.getClassID());
                    }
                    Log.v("getStudentToClass", "Uh oh nothing came up");
                }
            }
        });
    }
}
