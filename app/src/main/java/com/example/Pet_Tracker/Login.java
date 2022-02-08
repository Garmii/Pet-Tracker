package com.example.Pet_Tracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.login.R;

import java.sql.SQLException;

public class Login extends AppCompatActivity {

    private SALUDSqlHelper saludSqlHelper;
    private SQLiteDatabase db;

    Button iniciarSesion;
    Button registrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db=null;
        saludSqlHelper = SALUDSqlHelper.getInstance(this);

        iniciarSesion = findViewById(R.id.iniciarSesion);
        registrarse = findViewById(R.id.registrarse);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                    }
                }
        );





/*        try {
            db = saludSqlHelper.open();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }*/



        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,SignIn.class);
               activityResultLauncher.launch(intent);
            }
        });


    }

}
