package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.example.login.R;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    private SALUDSqlHelper saludSqlHelper;
    private SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saludSqlHelper = SALUDSqlHelper.getInstance(this);

        try {
            db = saludSqlHelper.open();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Log.i("VERSION",""+db.getVersion());

        Log.i("Version", db.getVersion() + "");
    }
}
