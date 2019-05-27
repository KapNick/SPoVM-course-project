package com.example.navigator;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class AddPoint extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point);

        //makeFolder();             //Это использоваось для проверки работы с файловой системой

        Button ended = findViewById(R.id.Ended);
        ended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPoint.this.finish();
                Toast.makeText(AddPoint.this, "Successfully added a new point", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeFolder(){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"fandroid");

        if (!file.exists()){
            boolean ff = file.mkdir();
            if (ff){
                Toast.makeText(AddPoint.this, "Folder created successfully", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(AddPoint.this, "Failed to create folder", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(AddPoint.this, "Folder already exist", Toast.LENGTH_SHORT).show();
        }
    }
}
