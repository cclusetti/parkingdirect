package parking.direct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class ClosestEventAllActivity extends MapActivity{
	GlobalVariables appState;
	ToggleButton listButton;
	ToggleButton mapButton;
	ViewSwitcher switcher;
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.close2eventall);
		
		MapView mapView = (MapView) findViewById(R.id.closeeventallmapview);
        mapView.setBuiltInZoomControls(true);
        
        switcher = (ViewSwitcher) findViewById(R.id.closestEventViewSwitcher);
        //switcher.showNext();
        listButton = (ToggleButton) findViewById(R.id.EventListTButton);
        mapButton = (ToggleButton) findViewById(R.id.EventMapTButton);
        
        listButton.setChecked(true);
        
        listButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				listButton.setChecked(true);
				mapButton.setChecked(false);
				//only switch view to listview if on mapview
				if(switcher.getDisplayedChild()==1)
						switcher.showPrevious();					
				
			}
		});
        mapButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
			
				listButton.setChecked(false);
				mapButton.setChecked(true);	
				//only switch view to mapview if on listview
				if(switcher.getDisplayedChild()==0)
					switcher.showNext();
				MapView allMapView = (MapView) findViewById(R.id.closeeventallmapview);
				allMapView.setBuiltInZoomControls(true);
		        
		        //set initial map size and centering point
		        MapController myController = allMapView.getController();
		        GeoPoint home = new GeoPoint((int)(38.029226 *1000000), (int)(-78.509247 *1000000)); //my home address
		        myController.setCenter(home);
		        myController.setZoom(17);
		        
		        List<Overlay> mapOverlays = allMapView.getOverlays();				
				Drawable drawable = ClosestEventAllActivity.this.getResources().getDrawable(R.drawable.orange_pin2);
				SiteItemizedOverlay itemizedoverlay = new SiteItemizedOverlay(drawable, ClosestEventAllActivity.this);
		        OverlayItem overlayitem;
		        
		        for (int i =0; i<appState.MyLocationsList.size();i++)
		        {
		        	//points must be a double w/ 6 figures after the dec point 
		        	GeoPoint point = new GeoPoint(Integer.parseInt(appState.MyLocationsList.get(i).getLat()),Integer.parseInt(appState.MyLocationsList.get(i).getLong()));
		        	overlayitem = new OverlayItem(point, "Price: $" + String.valueOf(appState.MyLocationsList.get(i).getPrice()) ,"Available Spots: " + String.valueOf(appState.MyLocationsList.get(i).getNumSpots()) );
		        
		        	itemizedoverlay.addOverlay(overlayitem);
		        }
		        mapOverlays.add(itemizedoverlay);
		        Toast.makeText(getApplicationContext(),"Click an icon to view details",
					      Toast.LENGTH_SHORT).show();
		        //allMapView.postInvalidate();
			}
		});	
        
		appState = ((GlobalVariables)getApplicationContext());
		
		ListView lv =  (ListView) findViewById(R.id.closeEventAllListView);
        ArrayList<HashMap<String,String>> myListViewList = new ArrayList<HashMap<String,String>>();
		
		
		Document myDom = Utility.getParsedXMLDocument("http://www.parkingdirectuva.com/getSuppliers.php");
        String[][] data = Utility.getParsedData(myDom);       
        appState.MyLocationsList.clear();
        
        ParkingLocation parkingLocation;
        for(int i=0; i< data.length ; i++)
        {
        	parkingLocation = new ParkingLocation(Double.parseDouble(data[i][0]), Integer.parseInt(data[i][1]), data[i][2], data[i][3]);
        	appState.MyLocationsList.add(parkingLocation);
        	
        	//create new listitem
        	HashMap<String,String> map = new HashMap<String, String>();
        	map.put("Price", "Price: " + String.valueOf(parkingLocation.getPrice()));
        	map.put("Spots", "Spots Available: " + String.valueOf(parkingLocation.getNumSpots()));
        	
        	//insert to listview
        	myListViewList.add(map);      	
        }
        
       //adapter stuff
        SimpleAdapter mSchedule = new SimpleAdapter(this, myListViewList, R.layout.all_list_item, new String[]{"Price", "Spots"},new int[]{R.id.price_Cell, R.id.spots_Cell});
		lv.setAdapter(mSchedule);
				lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) {
		    	Intent myIntent = new Intent(ClosestEventAllActivity.this, ClosesToEvent.class);
				Bundle b = new Bundle();
				b.putInt("listposition",position);
				myIntent.putExtras(b);
				startActivity(myIntent);
		      //Toast.makeText(getApplicationContext(), String.valueOf(id),
		      //Toast.LENGTH_SHORT).show();
		    }
		  }); 
		Toast.makeText(getApplicationContext(),"Select a Location to View Directions",
			      Toast.LENGTH_SHORT).show();
		  
	}

}
