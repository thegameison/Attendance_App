package com.example.attendanceapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AttendanceView extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private Calendar c;
    private Spinner classSpinner;
    private String className;
    private String id;
    private ArrayAdapter<String> classAdapter;
    private List<String> classList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference classRef = db.collection("Classes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_attendance_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button btnDate = findViewById(R.id.set_date);
        final TextView name = findViewById(R.id.class_name);
        TextView classSelection = findViewById(R.id.class_select);
        classSpinner = findViewById(R.id.spinner);
        Button btnAttendance = findViewById(R.id.goToAttendance);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            className = extras.getString("DanceClass");
            name.setText(className);
            id = extras.getString("id");
            Log.v("AttendanceView", "Value of class id: " + id);
            classSelection.setVisibility(View.INVISIBLE);
            classSpinner.setVisibility(View.INVISIBLE);
        } else{
            classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classList);
            classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            populateClassList();
            classSpinner.setAdapter(classAdapter);
            classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    name.setText(parent.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"This");
            }
        });

        btnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTakeAttendance(name.getText().toString());
            }
        });
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        TextView textView = (TextView) findViewById(R.id.show_date);
        textView.setText(currentDateString);
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
    public void openTakeAttendance(String name){
        if (c == null){
            Toast.makeText(this, "Error: Please enter a date", Toast.LENGTH_LONG).show();
        } else{

            Intent i = new Intent(this, TakeAttendance.class);
            i.putExtra("ClassName",name);
            i.putExtra("Day",c.get(Calendar.DAY_OF_MONTH));
            i.putExtra("Month", c.get(Calendar.MONTH));
            i.putExtra("Year",c.get(Calendar.YEAR));
            i.putExtra("Id",id);
            Log.v("AttendanceView", "Value of year: " + c.get(Calendar.YEAR));
            startActivity(i);
        }
    }

}
