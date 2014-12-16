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

    //this method automatically called when user select menu items
    public boolean onOptionsItemSelected(MenuItem item) {

        if (myView == null) {
            myView = new MyView(this);
        }
        switch (item.getItemId()) {
            case R.id.DRAW:                
                MyView.linetype = editText.getText().toString();
                setContentView(myView);
                break;
            case R.id.RETURN:
                setContentView(R.layout.main);
                editText = (EditText) findViewById(R.id.edit_message);
                break;

        }
        return true;
    }
}

