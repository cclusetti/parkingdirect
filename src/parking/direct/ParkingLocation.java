package parking.direct;

public class ParkingLocation {
	private double price;
	private int numSpots;
	private String latitude;
	private String longitude;
	public ParkingLocation(double price, int numSpots, String latitude, String longitude)
	{
		this.price = price;
		this.numSpots = numSpots;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	double getPrice()
	{
		return price;
	}
	int getNumSpots()
	{
		return numSpots;
	}
	String getLat()
	{
		return latitude;
	}
	String getLong()
	{
		return longitude;
	}
	
}
