package com.example;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    private EditText editText = null;
    private MyView myView = null;
    private String fillcolor="";
    private String linecolor="";
    private String T="";
    private String T1="";
    private String H="";
    private String H1="";
    private String W="";
    private String W1="";
    private String linetype="";
    private String AM="";
    private String AN="";
    private String X="";
    private String extents="";
    private String rev="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        editText = (EditText) findViewById(R.id.edit_message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflator = new MenuInflater(this);
        inflator.inflate(R.layout.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
    private static String lastContext="modifiers";
    //this method automatically called when user select menu items
    public boolean onOptionsItemSelected(MenuItem item) {

        if (myView == null) {
            myView = new MyView(this);
        }
        if(lastContext=="attributes")
        {
            editText = (EditText) findViewById(R.id.edit_LineColor);
            linecolor=editText.getText().toString();
            editText = (EditText) findViewById(R.id.edit_FillColor);
            fillcolor=editText.getText().toString();
            editText = (EditText) findViewById(R.id.edit_AM);
            AM=editText.getText().toString();
            editText = (EditText) findViewById(R.id.edit_AN);
            AN=editText.getText().toString();
            editText = (EditText) findViewById(R.id.edit_X);
            X=editText.getText().toString();
            editText = (EditText) findViewById(R.id.edit_Extents);
            extents=editText.getText().toString();            
            editText = (EditText) findViewById(R.id.edit_Revision);
            rev=editText.getText().toString();            
        }
        else if(lastContext=="modifiers")
        {
            editText = (EditText) findViewById(R.id.edit_message);                
            linetype=editText.getText().toString();
            editText = (EditText) findViewById(R.id.edit_T);
            T=editText.getText().toString();
            editText = (EditText) findViewById(R.id.edit_T1);
            T1=editText.getText().toString();
            editText = (EditText) findViewById(R.id.edit_H);
            H=editText.getText().toString();
            editText = (EditText) findViewById(R.id.edit_H1);
            H1=editText.getText().toString();
            editText = (EditText) findViewById(R.id.edit_W);
            W=editText.getText().toString();
            editText = (EditText) findViewById(R.id.edit_W1);
            W1=editText.getText().toString();
        }
        switch (item.getItemId()) {
            case R.id.DRAW:                
                utility.linetype=linetype;
                MyView.linetype = linetype;
                MyView.extents=extents;
                MyView.Rev=rev;
                utility.T=T;
                utility.T1=T1;
                utility.H=H;
                utility.H1=H1;
                utility.W=W;
                utility.W1=W1;
                utility.linecolor=linecolor;
                utility.fillcolor=fillcolor;
                utility.AM=AM;                
                utility.AN=AN;
                utility.X=X;
                utility.Rev=rev;
                lastContext="draw";
                setContentView(myView);
                break;
            case R.id.MODIFIERS:
                lastContext="modifiers";
                setContentView(R.layout.main);
                editText = (EditText) findViewById(R.id.edit_message);                
                editText.setText(linetype);
                editText = (EditText) findViewById(R.id.edit_T);
                editText.setText(T);
                editText = (EditText) findViewById(R.id.edit_T1);
                editText.setText(T1);
                editText = (EditText) findViewById(R.id.edit_H);
                editText.setText(H);
                editText = (EditText) findViewById(R.id.edit_H1);
                editText.setText(H1);
                editText = (EditText) findViewById(R.id.edit_W);
                editText.setText(W);
                editText = (EditText) findViewById(R.id.edit_W1);
                editText.setText(W1);
                //editText = (EditText) findViewById(R.id.edit_message);
                break;      
            case R.id.ATTRIBUTES:
                lastContext="attributes";
                setContentView(R.layout.attributes);
                editText = (EditText) findViewById(R.id.edit_LineColor);
                editText.setText(linecolor);
                editText = (EditText) findViewById(R.id.edit_FillColor);
                editText.setText(fillcolor);
                editText = (EditText) findViewById(R.id.edit_AM);
                editText.setText(AM);                
                editText = (EditText) findViewById(R.id.edit_AN);
                editText.setText(AN);
                editText = (EditText) findViewById(R.id.edit_X);
                editText.setText(X);                
                editText = (EditText) findViewById(R.id.edit_Revision);
                editText.setText(rev);                
                editText = (EditText) findViewById(R.id.edit_Extents);
                editText.setText(extents);                
                break;
            default:
                break;
        }
        return true;
    }
}

