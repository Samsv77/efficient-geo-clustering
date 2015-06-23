package com.geo.tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.geo.cluster.GeoClusterManager;
import com.geo.entities.GeoPoint;

public class GeoClusterTest {
	
	
	@Test
	public void geoClusterTest() {
		
		// Distance in kilometers
		int distance = 500;
		
		// Adding tests geo points
		// Latitude, Longitude, Value
		List<GeoPoint> points = new ArrayList<GeoPoint>();
		points.add(new GeoPoint(18.572125, 116.171875, 1.));
		points.add(new GeoPoint(37.261625, 42.840625, 1.));
		points.add(new GeoPoint(68.203125, 33.046875, 1.));
		points.add(new GeoPoint(38.679875, -86.484375, 1.));
		points.add(new GeoPoint(37.631875, -85.029125, 1.));
		points.add(new GeoPoint(40.078125, 44.296875, 1.));
		points.add(new GeoPoint(35.852375, -83.671875, 1.));
		points.add(new GeoPoint(44.296875, 40.079125, 1.));
		points.add(new GeoPoint(40.079125, -85.078125, 1.));
		points.add(new GeoPoint(37.265625, -82.265625, 1.));
		points.add(new GeoPoint(38.671875, -82.245625, 1.));
		points.add(new GeoPoint(48.525625, 68.203125, 1.));
		points.add(new GeoPoint(42.890625, -87.890625, 1.));
		points.add(new GeoPoint(32.453125, -79.453125, 1.));
		points.add(new GeoPoint(16.171875, 108.984375, 1.));
		points.add(new GeoPoint(-21.796875, 166.640625, 1.));
		points.add(new GeoPoint(52.734375, 49.921875, 1.));
		points.add(new GeoPoint(2.109375, 113.203125, 1.));
		points.add(new GeoPoint(62.578125, 14.765625, 1.));
		points.add(new GeoPoint(30.034375, 31.643625, 1.));
		points.add(new GeoPoint(-7.734375, 113.203125, 1.));
		points.add(new GeoPoint(44.996875, -75.284375, 1.));
		points.add(new GeoPoint(0.703125, 38.671875, 1.));
		points.add(new GeoPoint(-4.121875, 120.234675, 1.));
		points.add(new GeoPoint(3.565625, 101.953125, 1.));
		points.add(new GeoPoint(42.892625, 23.203155, 1.));
		points.add(new GeoPoint(-26.015625, -49.931875, 1.));
		points.add(new GeoPoint(51.318125, -120.230375, 1.));
		
		
		try {
			GeoClusterManager manager = new GeoClusterManager();
			List<GeoPoint> centroids = manager.clusterize(points, distance, true);
			
			Assert.assertTrue(centroids != null && centroids.size() > 0);
			
			System.out.println("Points count: " + points.size());
			System.out.println("Centroids count: " + centroids.size());
			
			//for (GeoPoint p : centroids) {
			//	System.out.println("Coordinates: " + p.getLatitude() + ", " + p.getLongitude() + ", value = " + p.getValue());
			//}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void emptyTest() {
		// Distance in kilometers
		int distance = 500;
		
		List<GeoPoint> points = new ArrayList<GeoPoint>();
		
		try {
			GeoClusterManager manager = new GeoClusterManager();
			List<GeoPoint> centroids = manager.clusterize(points, distance, true);
			
			Assert.assertTrue(centroids.size() == 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
