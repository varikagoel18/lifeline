package com.delight.lifeline;



import com.delight.lifeline.UI.LLEventListFragment;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;


public class MainActivity extends Activity {

	private LLEventListFragment mFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
   	    mFragment = (LLEventListFragment) getFragmentManager().findFragmentById( R.id.list);
   	    
   	    Log.d("TAG", "on create main activity");
   	    
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }
    
    
    
    
    
}
