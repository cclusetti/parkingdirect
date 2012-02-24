package parking.direct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class ClosesToEvent extends Activity{
	
	private LocationManager locationManager;
	private String bestProvider;
	Criteria criteria;
	private double longitude;
	private double latitude;
	//clear location list so i can now sort my price
    GlobalVariables appState;
	ListView dirsList;
	ArrayList<HashMap<String,String>> myList;
	TextView cheapTextView;
	String[][] dirs;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cheapestscreen);
        //instantiate global variables
        appState = ((GlobalVariables)getApplicationContext());
        //instantiate listview
        dirsList= (ListView)findViewById(R.id.cheapestDirectionsListView);
        cheapTextView = (TextView)findViewById(R.id.cheapestTextView);
        //instantiate listitem list
        myList = new ArrayList<HashMap<String,String>>();
         
        Button cheapestMapButton = (Button) findViewById(R.id.cheapestMapButton);
        cheapestMapButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent myIntent = new Intent(ClosesToEvent.this, GoogleMap.class);
				Bundle b = new Bundle();
				//put a two dimensional array as list of arrays into a bundle
				int arrcount = dirs.length;
				b.putInt("ArraysCount",arrcount);//need to pass to the map related info
				
				for(int i=0;i<arrcount;i++)
				{
					b.putStringArray(String.valueOf(i), dirs[i]);
				}
				
				myIntent.putExtras(b);
				startActivity(myIntent);
				//finish();
			}
		});
        Button nextCheapestButton = (Button) findViewById(R.id.nextCheapestButton);
        nextCheapestButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				
				//clear directions list				
				myList.clear();				
				//go to next cheapest only if there is one
				if(appState.locationsIndex < appState.MyLocationsList.size()-1)
				appState.locationsIndex++;
				
				cheapTextView.setText("Price:" + appState.MyLocationsList.get(appState.locationsIndex).getPrice());
				//cheapTextView.setText("Price:" + appState.MyLocationsList.get(appState.locationsIndex).getPrice());
				//get direction data for current item in location list
				
				// Get the location manager
				
				Location location = locationManager.getLastKnownLocation(bestProvider);
				try {
					longitude = location.getLongitude();
					latitude = location.getLatitude();
				} catch (Exception e) {
					
				}
				
				//"38.029226","-78.509247"
		        dirs = Utility.getDirectionData(String.valueOf(latitude),String.valueOf(longitude), String.valueOf(Double.parseDouble(appState.MyLocationsList.get(appState.locationsIndex).getLat())/1000000.0), String.valueOf(Double.parseDouble(appState.MyLocationsList.get(appState.locationsIndex).getLong())/1000000.0));
		        //compile regex
		        Pattern p = Pattern.compile("<[^>]*>");
		        Matcher m;
		        for(int i=0; i< dirs.length ; i++)
		        {	
		        	//regex match regex
		        	m = p.matcher(dirs[i][0]);
		        	dirs[i][0] = m.replaceAll("");
		        	
		        	//create new listitem
		        	HashMap<String,String> map = new HashMap<String, String>();
		        	map.put("Dir", dirs[i][0]);
		        	map.put("Dist", dirs[i][1]);
		        	
		        	//insert to lsitview
		        	myList.add(map);
		        }
		        SimpleAdapter mSchedule = new SimpleAdapter(ClosesToEvent.this, myList, R.layout.dirs_list_item, new String[]{"Dir", "Dist"},new int[]{R.id.dir_Cell, R.id.dist_Cell}); 
		        dirsList.setAdapter(mSchedule);
								
				
			}
		});
        Button previousCheapestButton = (Button) findViewById(R.id.previousCheapestButton);
        previousCheapestButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				//clear directions list				
				myList.clear();
				
				//go to next cheapest only if there is one
				if(appState.locationsIndex > 0)
				appState.locationsIndex--;
				
				cheapTextView.setText("Price:" + appState.MyLocationsList.get(appState.locationsIndex).getPrice());
				//cheapTextView.setText("Price:" + appState.MyLocationsList.get(appState.locationsIndex).getPrice());
				//get direction data for current item in location list
				Location location = locationManager.getLastKnownLocation(bestProvider);
				try {
					longitude = location.getLongitude();
					latitude = location.getLatitude();
				} catch (Exception e) {
					
				}
				//"38.029226","-78.509247"
		        dirs = Utility.getDirectionData(String.valueOf(latitude),String.valueOf(longitude), String.valueOf(Double.parseDouble(appState.MyLocationsList.get(appState.locationsIndex).getLat())/1000000.0), String.valueOf(Double.parseDouble(appState.MyLocationsList.get(appState.locationsIndex).getLong())/1000000.0));
		        //compile regex
		        Pattern p = Pattern.compile("<[^>]*>");
		        Matcher m;
		        for(int i=0; i< dirs.length ; i++)
		        {	
		        	//regex match regex
		        	m = p.matcher(dirs[i][0]);
		        	dirs[i][0] = m.replaceAll("");
		        	
		        	//create new listitem
		        	HashMap<String,String> map = new HashMap<String, String>();
		        	map.put("Dir", dirs[i][0]);
		        	map.put("Dist", dirs[i][1]);
		        	
		        	//insert to lsitview
		        	myList.add(map);
		        }
		        SimpleAdapter mSchedule = new SimpleAdapter(ClosesToEvent.this, myList, R.layout.dirs_list_item, new String[]{"Dir", "Dist"},new int[]{R.id.dir_Cell, R.id.dist_Cell}); 
		        dirsList.setAdapter(mSchedule);
								
				
				
			}
		});
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		criteria = new Criteria();
		bestProvider = locationManager.getBestProvider(criteria, false);
        
        
        Bundle b = getIntent().getExtras();
        int listPox = b.getInt("listposition");
        appState.locationsIndex = listPox;
    	
        cheapTextView.setText("Price:" + appState.MyLocationsList.get(appState.locationsIndex).getPrice());
        
        //get direction data for current item in location list
        Location location = locationManager.getLastKnownLocation(bestProvider);
        try {
			longitude = location.getLongitude();
			latitude = location.getLatitude();
		} catch (Exception e) {
			
		}
        //"38.029226","-78.509247"
        dirs = Utility.getDirectionData(String.valueOf(latitude),String.valueOf(longitude), String.valueOf(Double.parseDouble(appState.MyLocationsList.get(appState.locationsIndex).getLat())/1000000.0), String.valueOf(Double.parseDouble(appState.MyLocationsList.get(appState.locationsIndex).getLong())/1000000.0));
        //compile regex
        Pattern p = Pattern.compile("<[^>]*>");
        Matcher m;
        for(int i=0; i< dirs.length ; i++)
        {	
        	//regex match regex
        	m = p.matcher(dirs[i][0]);
        	dirs[i][0] = m.replaceAll("");
        	
        	//create new listitem
        	HashMap<String,String> map = new HashMap<String, String>();
        	map.put("Dir", dirs[i][0]);
        	map.put("Dist", dirs[i][1]);
        	
        	//insert to lsitview
        	myList.add(map);
        }
        
        SimpleAdapter mSchedule = new SimpleAdapter(this, myList, R.layout.dirs_list_item, new String[]{"Dir", "Dist"},new int[]{R.id.dir_Cell, R.id.dist_Cell}); 
        dirsList.setAdapter(mSchedule);
   
    }
}
