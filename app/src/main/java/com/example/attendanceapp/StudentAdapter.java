package com.example.attendanceapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.support.constraint.Constraints.TAG;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ListItemHolder> {

    private List<Student> students;

    private StudentView studentView;


    public StudentAdapter(List<Student> students, StudentView v){
        this.students = students;
        studentView = v;
    }

    @NonNull
    @Override
    public StudentAdapter.ListItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_student, viewGroup, false);
        return new ListItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentAdapter.ListItemHolder listItemHolder, int i) {
        Student s = students.get(i);

        listItemHolder.mName.setText(s.getName());
        listItemHolder.mAge.setText(Integer.toString(s.getAge()));
        listItemHolder.mEmail.setText(s.getEmail());

        Log.v("StudentAdapter","Number of items in Students ArrayList: " + students.size());
        Log.v("StudentAdapter","The student data is being formatted");

    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mName;
        TextView mAge;
        TextView mEmail;

        public ListItemHolder(@NonNull View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.textName);
            mAge = itemView.findViewById(R.id.textAge);
            mEmail = itemView.findViewById(R.id.textEmail);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
