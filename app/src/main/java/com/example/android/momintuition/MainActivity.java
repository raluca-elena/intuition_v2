package com.example.android.momintuition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    private static Context context;

    public static Context getAppContext() {
        return MainActivity.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        drawButtons();
    }

    void drawButtons() {
        final Button x = (Button) findViewById(R.id.buttonSignin);
        final Button y = (Button) findViewById(R.id.buttonSignup);
        final Button z = (Button) findViewById(R.id.buttonAnim);

        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BUTTON", "Signin");
                FetchCoordinates fetchCordinates = new FetchCoordinates(getApplicationContext());
                fetchCordinates.execute();

                //Intent i;
                //i = new Intent(getAppContext(), SignIn.class);
                //startActivity(i);

            }
        };


        View.OnClickListener l1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BUTTON", "Signup");
                Intent i;
                i = new Intent(getApplicationContext(), ActivityChooser.class);
                startActivity(i);

            }
        };


        View.OnClickListener l2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BUTTON", "Animate");
                Intent i;
                i = new Intent(getApplicationContext(), Anim.class);
                startActivity(i);

            }
        };

        x.setOnClickListener(l);
        y.setOnClickListener(l1);
        z.setOnClickListener(l2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

