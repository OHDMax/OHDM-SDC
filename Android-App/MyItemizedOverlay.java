package de.htw.berlin.mobanwtest;

import java.util.ArrayList;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> overlayItemList = new ArrayList<OverlayItem>();
	GeoPoint tappedGeoPoint;
	String title = "";
	String substance = "";
	String value = "";
	String unit = "";
	double lat = 0.0;
	double lon = 0.0;

	public MyItemizedOverlay(Drawable pDefaultMarker,
			ResourceProxy pResourceProxy) {
		super(pDefaultMarker, pResourceProxy);
		// TODO Auto-generated constructor stub
	}

	public void addItem(GeoPoint p, String title, String snippet) {
		OverlayItem newItem = new OverlayItem(title, snippet, p);
		overlayItemList.add(newItem);
		populate();
	}

	@Override
	public boolean onSnapToItem(int arg0, int arg1, Point arg2, IMapView arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent event, MapView map) {
		// TODO Auto-generated method stub
		int coordinateX = (int) event.getX();
		int coordinateY = (int) event.getY();

		Projection projection = map.getProjection();
		this.tappedGeoPoint = (GeoPoint) projection.fromPixels(coordinateX,
				coordinateY);

		// tappedGeoPoint.getLatitudeE6();
		// tappedGeoPoint.getLongitudeE6();
		return false;
	}

	@Override
	protected OverlayItem createItem(int arg0) {
		// TODO Auto-generated method stub
		return overlayItemList.get(arg0);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return overlayItemList.size();
	}

	public GeoPoint getTappedGeoPoint() {
		return tappedGeoPoint;
	}

	public String gettitle() {
		return title;
	}

	public void settitle(String title) {
		this.title = title;
	}

	public String getsubstance() {
		return substance;
	}

	public void setsubstance(String substance) {
		this.substance = substance;
	}

	public String getvalue() {
		return value;
	}

	public void setvalue(String value) {
		this.value = value;
	}

	public String getunit() {
		return unit;
	}

	public void setunit(String unit) {
		this.unit = unit;
	}

	public double getlon() {
		return lon;
	}

	public void setlon(double lon) {
		this.lon = lon;
	}

	public double getlat() {
		return lat;
	}

	public void setlat(double lat) {
		this.lat = lat;
	}

}
