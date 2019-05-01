package com.example.attendanceapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ListItemHolder> {
    private List<DanceClass> classes;

    private ClassView classView;

    public ClassAdapter(List<DanceClass> c, ClassView v){
        classes = c;
        classView = v;
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_class, viewGroup, false);
        return new ClassAdapter.ListItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ClassAdapter.ListItemHolder listItemHolder, int i) {
        DanceClass c = classes.get(i);
        listItemHolder.mName.setText(c.getClassName());
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }


    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mName;

        public ListItemHolder(@NonNull View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.textName);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            classView.openClass(getAdapterPosition());
        }
    }
}
