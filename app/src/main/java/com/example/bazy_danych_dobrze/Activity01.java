package com.example.bazy_danych_dobrze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Activity01 extends AppCompatActivity {

    public  static String[] comRange;
    String currentItemName=null;
    Integer currentItemQuantity=null;

    TextView stateTV=null;
    EditText changeET=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_01);

        stateTV=(TextView)findViewById(R.id.stateTextView);
        changeET=(EditText)findViewById(R.id.editText);

        comRange=getResources().getStringArray(R.array.Asortyment);

        final SQLiteOpenHelper DBHelper=new MarketDatabaseHelper(this);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(
                this,R.array.Asortyment,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner=(Spinner)findViewById(R.id.Spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        currentItemName=comRange[position];
                        try
                        {
                            SQLiteDatabase DB=DBHelper.getReadableDatabase();
                            Cursor cursor=DB.query(
                                    "STAND",
                                    new String[]{"QUANTITY"},
                                    "Name=?",
                                    new String[]{currentItemName},
                                    null, null, null);
                            cursor.moveToFirst();
                            currentItemQuantity = cursor.getInt(0);
                            cursor.close();
                            DB.close();
                        }
                        catch (SQLiteException e)
                        {
                            Toast.makeText(Activity01.this,"EXCEPTION: SPINNER",Toast.LENGTH_SHORT).show();

                        }
                        stateTV.setText("Stan magazynu dla "+currentItemName+": "+currentItemQuantity);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
        //Skladaj
        Button setButton=(Button) findViewById(R.id.setButton);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer newItemQuantity;
                Integer changeItemQuantity;
                if(changeET.getText().toString().equals(""))
                {
                    changeItemQuantity=0;
                    newItemQuantity=currentItemQuantity;

                }
                else {
                     changeItemQuantity = Integer.parseInt(changeET.getText().toString());
                     newItemQuantity = currentItemQuantity + changeItemQuantity;
                }
                try
                {
                    SQLiteDatabase DB=DBHelper.getWritableDatabase();
                    ContentValues itemValues=new ContentValues();
                    itemValues.put("QUANTITY",newItemQuantity.toString());

                    DB.update("STAND",
                            itemValues,
                            "NAME=?",
                            new String[]{currentItemName});
                    DB.close();
                }
                catch (SQLiteException e)
                {
                    Toast.makeText(Activity01.this,"EXCEPTION:SET",Toast.LENGTH_SHORT).show();
                }

                stateTV.setText("Stan magazynu dla "+currentItemName+" : "+newItemQuantity);
                changeET.setText("");
                currentItemQuantity=newItemQuantity;
            }
        });
        //przycisk wydaj
        Button getButton=(Button)findViewById(R.id.getButton);
        getButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Integer newItemQuantity;
                        Integer changeItemQuantity;
                        if(changeET.getText().toString().equals(""))
                        {
                            changeItemQuantity=0;
                            newItemQuantity=currentItemQuantity;

                        }
                        else
                        {
                            changeItemQuantity=Integer.parseInt(changeET.getText().toString());
                            newItemQuantity=currentItemQuantity-changeItemQuantity;
                        }


                        try
                        {
                            SQLiteDatabase DB=DBHelper.getWritableDatabase();
                            ContentValues itemValues=new ContentValues();
                            itemValues.put("QUANTITY",newItemQuantity.toString());
                            DB.update("STAND",
                                    itemValues,
                                    "NAME=?",
                                    new String[]{currentItemName});
                            DB.close();
                        }
                        catch (SQLiteException e)
                        {
                            Toast.makeText(Activity01.this,"EXCEPTION: GET",Toast.LENGTH_SHORT).show();
                        }
                        stateTV.setText("Stan magazynu dla "+currentItemName+" :"+newItemQuantity);
                        changeET.setText("");
                        currentItemQuantity=newItemQuantity;
                    }
                }
        );


    }
}
