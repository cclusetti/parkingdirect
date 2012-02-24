package parking.direct;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;

public class ParkingDirectActivity extends TabActivity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab
       
        

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, ClosestMeAllActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("closesttome").setIndicator("Close To Me",
                          res.getDrawable(R.drawable.ic_tab_closes2me))
                      .setContent(intent);
        
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, CheapestAllActivity.class);
        spec = tabHost.newTabSpec("cheapest").setIndicator("Cheap",
                          res.getDrawable(R.drawable.ic_tab_cheapest))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ClosestEventAllActivity.class);
        spec = tabHost.newTabSpec("closestoevent").setIndicator("Close To Event",
                          res.getDrawable(R.drawable.ic_tab_closest2event))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++) 
        {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.BLACK);
        } 

        tabHost.setCurrentTab(1);

    }
	
}

