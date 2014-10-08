package com.pettinder;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;



public class MainActivity extends ActionBarActivity {

	
	View.OnClickListener destroy = (new View.OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    });
	Button button;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(destroy);
        Log.d("Checkpoint 3", "Called onCreate");
    }

    
    @Override
    protected void onStart(){
    	super.onStart();
        Log.d("Checkpoint 3", "Called onStart");
    }
    
    @Override
    protected void onRestart(){
    	super.onRestart();
        Log.d("Checkpoint 3", "Called onRestart");
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
        Log.d("Checkpoint 3", "Called onResume");
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
        Log.d("Checkpoint 3", "Called onPause");
    }
    
    @Override
    protected void onStop(){
    	super.onStop();
        Log.d("Checkpoint 3", "Called onStop");
    }

    @Override
    protected void onDestroy(){
    	super.onDestroy();
        Log.d("Checkpoint 3", "Called onDestroy");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
