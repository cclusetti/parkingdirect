package parking.direct;

import java.util.ArrayList;

import android.app.Application;

public class GlobalVariables extends Application{
	public ArrayList<ParkingLocation> MyLocationsList =  new ArrayList<ParkingLocation>();
	public int locationsIndex = 0;

}
