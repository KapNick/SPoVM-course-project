package com.example.navigator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class AddPoint extends AppCompatActivity {
    private final static String FILE_NAME = "contt.txt";
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point);

        Button ended = findViewById(R.id.Ended);
        ended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText name = findViewById(R.id.Name);
                EditText lattitude = findViewById(R.id.Lattitude);
                EditText longtitude = findViewById(R.id.Longtitude);
                CheckBox chosen = findViewById(R.id.Chosen_loc);
                int flag = 0;
                if(lattitude.length() == 0 || longtitude.length() == 0 || name.length() == 0){
                    Toast.makeText(AddPoint.this, "Error adding new point", Toast.LENGTH_SHORT).show();
                }else {
                    if(chosen.isSelected()) {flag = 1;}
                    String name1 = name.getText().toString();
                    double lattitude1 = Double.parseDouble(lattitude.getText().toString());
                    double longtitude1 = Double.parseDouble(longtitude.getText().toString());
                    try {
                        openText();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    text = text + flag + " " + name1 + " " + lattitude1 + " " + longtitude1 + " ";
                    saveText();
                    Toast.makeText(AddPoint.this, "Successfully added a new point", Toast.LENGTH_SHORT).show();
                }

                AddPoint.this.finish();
            }
        });
    }

    public void saveText(){

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());
        }
        catch(IOException ex) {System.out.println(ex);}
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){
                System.out.println(ex);
            }
        }
    }

    public void openText() throws IOException {

        FileInputStream fin = openFileInput(FILE_NAME);
//        try {
//            fin = openFileInput(FILE_NAME);
//            byte[] bytes = new byte[fin.available()];
//            fin.read(bytes);
//            text = new String (bytes);
//        }
//        catch(IOException ex) {System.out.println(ex);}
//        finally{
//
//            try{
//                if(fin!=null)
//                    fin.close();
//            }
//            catch(IOException ex){System.out.println(ex);}
//        }
        InputStreamReader reader = new InputStreamReader(fin);
        BufferedReader buffer = new BufferedReader(reader);
        StringBuilder str = new StringBuilder();
        while((text = buffer.readLine()) != null){
            str.append(text).append("\n");
        }
    }
}
