package parking.direct;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class RouteOverlay extends Overlay {
    //private GeoPoint gp1;
    //private GeoPoint gp2;
    private int color;
    private ArrayList<GeoPoint> GPList;
 
public RouteOverlay(ArrayList<GeoPoint> GPList, int color) {
        //this.gp1 = gp1;
        //this.gp2 = gp2;
        this.color = color;
        this.GPList = GPList;
    }
@Override
public void draw(Canvas canvas, MapView mapView, boolean shadow) {
    Projection projection = mapView.getProjection();
    Path myPath = new Path();
    Point startPoint = new Point();
    Point nextPoint = new Point();
    projection.toPixels(GPList.get(0), startPoint);
    myPath.moveTo(startPoint.x,startPoint.y);
    for(int i=1;i<GPList.size();i++)
    {
    	projection.toPixels(GPList.get(i), nextPoint);       
        myPath.lineTo(nextPoint.x, nextPoint.y);
    }
    Paint paint = new Paint();
    paint.setStyle(Paint.Style.STROKE);
    paint.setColor(color);
    paint.setStrokeWidth(5);
    paint.setAlpha(120);
    canvas.drawPath(myPath, paint);
    super.draw(canvas, mapView, shadow);
	}
}