package com.example.attendanceapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CheckboxAdapter extends RecyclerView.Adapter<CheckboxAdapter.ViewHolder> {

    private SparseBooleanArray checkBoxStates = new SparseBooleanArray();
    private List<String> studentNames = new ArrayList<>();

    CheckboxAdapter(){

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_student_checkbox,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(i);

    }
    public SparseBooleanArray getCheckBoxStates(){
        for (int i = 0; i < checkBoxStates.size();i++){
            Log.v("getCheckBoxStates",checkBoxStates.get(i)+"");
        }
        return checkBoxStates;
    }
    public List<String> getStudentNames(){
        return studentNames;
    }

    @Override
    public int getItemCount() {
        if(studentNames == null){
            return 0;
        }
        return studentNames.size();
    }

    public void loadStudents(List<String> studentNames){
        this.studentNames = studentNames;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox mCheckBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.checkBox);
            mCheckBox.setOnClickListener(this);
        }

        void bind(int position) {
            // use the sparse boolean array to check
            if (!checkBoxStates.get(position, false)) {
                mCheckBox.setChecked(false);
            }
            else {
                mCheckBox.setChecked(true);
            }
            mCheckBox.setText(String.valueOf(studentNames.get(position)));

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if (!checkBoxStates.get(adapterPosition, false)) {
                mCheckBox.setChecked(true);
                checkBoxStates.put(adapterPosition, true);

            }
            else  {
                mCheckBox.setChecked(false);
                checkBoxStates.put(adapterPosition, false);
            }
        }
    }
}
