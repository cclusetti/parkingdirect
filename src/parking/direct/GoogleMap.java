package parking.direct;


import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class GoogleMap extends MapActivity{
	private MyLocationOverlay myLocationOverlay;
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
	@Override
		protected void onResume() {
		    super.onResume();
		    // when our activity resumes, we want to register for location updates
		    myLocationOverlay.enableMyLocation();
		}
		 
	@Override
		protected void onPause() {
		    super.onPause();
	    // when our activity pauses, we want to remove listening for location updates
		    myLocationOverlay.disableMyLocation();
		}
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        //set initial map size and centering point
        MapController myController = mapView.getController();
        GeoPoint home = new GeoPoint((int)(38.029226 *1000000), (int)(-78.509247 *1000000)); //my home address
        myController.setCenter(home);
        myController.setZoom(16);
        
        //create overlay
        List<Overlay> mapOverlays = mapView.getOverlays();

        //mylocationoverlay
        myLocationOverlay = new MyLocationOverlay(mapView.getContext(), mapView);
        mapOverlays.add(myLocationOverlay);
        
        //Document myDom = Utility.getParsedXMLDocument("http://people.virginia.edu/~ccl4r/ParkingDirect/getSuppliers.php");
        //String[][] data = Utility.getParsedData(myDom);
        
        
        
        //for(int i=0; i < data.length; i++)
        //{
        	//points must be a double w/ 6 figures after the dec point 
        	//GeoPoint point = new GeoPoint(Integer.parseInt(data[i][2]),Integer.parseInt(data[i][3]));
        	//GPList.add(point);
        	//overlayitem = new OverlayItem(point, "Price: " + data[i][0],"Available Spots: " + data[i][1]);
        
        	//itemizedoverlay.addOverlay(overlayitem);
        //}
        //mapOverlays.add(itemizedoverlay);
        //add dir overlay
        
        ArrayList<GeoPoint> GPList = new ArrayList<GeoPoint>();//move this down later
        Bundle b = getIntent().getExtras();      
        int numArrays = b.getInt("ArraysCount");
        String[] currDirsList;
        
        for(int i=0; i<numArrays;i++)
        {
        	currDirsList = b.getStringArray(String.valueOf(i));
        	//change dirs formatted gps to map gps (multiply times 1E6)
        	GeoPoint gp = new GeoPoint((int) (Double.parseDouble(currDirsList[2])*1E6),(int)(Double.parseDouble(currDirsList[3])*1E6));
        	GPList.add(gp);
        	
        	
        }
        mapOverlays.add(new RouteOverlay(GPList,1));
        
        //add flag for destination
        String[] destInfo = b.getStringArray(String.valueOf(numArrays-1));
        Drawable drawable = this.getResources().getDrawable(R.drawable.flag);
        SiteItemizedOverlay itemizedoverlay = new SiteItemizedOverlay(drawable, mapView.getContext());
    
        GeoPoint point = new GeoPoint((int) (Double.parseDouble(destInfo[2])*1E6),(int)(Double.parseDouble(destInfo[3])*1E6));
        OverlayItem overlayitem = new OverlayItem(point, "Price: ","Available Spots: ");
        itemizedoverlay.addOverlay(overlayitem);
        
        mapOverlays.add(itemizedoverlay);
        mapView.postInvalidate();
       
    }
    
    
}
