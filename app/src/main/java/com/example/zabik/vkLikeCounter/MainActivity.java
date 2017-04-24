package com.example.zabik.vkLikeCounter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button logOut;
    private Button get;
    private TextView view;
    private TextView sizeOfdb;
    private Button delete;
    DB dataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logOut = (Button)findViewById(R.id.add);
        get = (Button)findViewById(R.id.get);
        logOut.setOnClickListener(this);
        get.setOnClickListener(this);
        view = (TextView) findViewById(R.id.textView);
        dataBase = new DB(this);
        delete = (Button) findViewById(R.id.deleteBd);
        delete.setOnClickListener(this);
        sizeOfdb = (TextView)findViewById(R.id.sizeOfDb);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        SQLiteDatabase dataB = dataBase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ParsePhoto parseModul = new ParsePhoto(dataBase);
        switch (v.getId()){
            case R.id.add:
                parseModul.getUsersWhichLikePhoto(0);
                break;
            case R.id.get:
              //  parseModul.showDB(view);
                int size = parseModul.getSize();
                sizeOfdb.setText(String.valueOf(size));
                break;
            case R.id.deleteBd:
               parseModul.deleteTable();
                break;
        }
        dataB.close();
    }
}
