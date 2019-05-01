package com.example.attendanceapp;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class DialogRegisterClass extends DialogFragment {
    private ClassView callingActivity;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference classRef = db.collection("Classes");
    private List<DanceClass> currentClasses;

    public Dialog onCreateDialog(Bundle savedInstanceState){
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        callingActivity = (ClassView) getActivity();

        currentClasses = callingActivity.getShowClasses();
        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_register_class, null);

        Log.v("DialogRegisterClass","The dialog has been inflated");

        final EditText editName = dialogView.findViewById(R.id.class_name);

        builder.setView(dialogView).setMessage("Add a New Class");

        Button btnCancel = dialogView.findViewById(R.id.cancel_button);
        Button btnOK = dialogView.findViewById(R.id.register);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DanceClass c = new DanceClass();
                c.setClassName(editName.getText().toString());
                if (isClassBadInput(c)){
                    dismiss();
                } else{
                    classRef.add(c);
                    Log.v("Testing hash code",classRef.hashCode()+"");
                    callingActivity.initUpdateClass();
                    callingActivity.getmAdapter().notifyDataSetChanged();
                    dismiss();
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return builder.create();
    }

    private boolean isClassBadInput(DanceClass danceClass){
        if (danceClass.getClassName() == null || danceClass.getClassName().equals("")){
            Toast.makeText(callingActivity, "Error: Class name has not been entered", Toast.LENGTH_LONG).show();
            return true;
        } else{
            for (DanceClass d : currentClasses){
                if(d.getClassName().equals(danceClass.getClassName())){
                    Toast.makeText(callingActivity, "Error: Class has already been created", Toast.LENGTH_LONG).show();
                    return true;
                }
            }
            return  false;
        }
    }
}
