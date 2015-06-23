package com.geo.cluster;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.util.FastMath;
import org.apache.log4j.Logger;

import com.geo.entities.GeoPoint;

/**
*
* <p>Title: GeoClusterCore</p>
* <p>Description: This class provides DBSCAN based geo clustering</p>
* <p>Copyright: Copyright (c) 2015</p>
* <p>Company: </p>
* @author Samuel Mony
* @version 1.0
*/
public class GeoClusterCore {
	private static final Logger log = Logger.getLogger(GeoClusterCore.class);
	
	// Radius of the earth (approx), in km
	private static final double R = 6371;

	
	// Calculate haversine distance between points to determine clustering
	private DistanceMeasure measure = new DistanceMeasure() {
		private static final long serialVersionUID = 1L;
		
		@Override
		public double compute(double[] a, double[] b) {
			return haversineDistance(a[0], a[1], b[0], b[1]);
		}
	};
	
	
	/**
	 * 
	 * @param points
	 * @param k
	 * @return
	 */
	public List<GeoPoint> clusterDBSCAN(List<DoublePoint> points, int distance) {
		// Process dbscan clustering
		DBSCANClusterer<DoublePoint> dbscan = new DBSCANClusterer<DoublePoint>(distance, 0, measure);
		List<Cluster<DoublePoint>> clusters = dbscan.cluster(points);
		
		List<GeoPoint> centroids = new ArrayList<GeoPoint>();
		
		// Calculate the geo center of every centroid
		for(Cluster<DoublePoint> cluster : clusters) {
			GeoPoint centroid = calculateGeoClusterCenter(cluster.getPoints());
			if (centroid != null) {
				centroids.add(centroid);
			}
		}
		
		return centroids;
	}
	
	
	/**
	 * Calculate Geo cluster centroids and their values
	 */
	private GeoPoint calculateGeoClusterCenter(List<DoublePoint> points) {
		// Calculate the mean of all lats and longs, and sum up the points values
		double lat = 0;
		double lon = 0;
		double value = 0;
		
		if (points.size() == 0)
			return null;
		
		for (DoublePoint p : points) {
			lat += p.getPoint()[0];
			lon += p.getPoint()[1];
			value += p.getPoint()[2];
		}
		
		log.debug("Centroid coordinates: " + (lat / (double) points.size()) + ", " + (lon / (double) points.size()));
		
		return new GeoPoint(lat / (double) points.size(), lon / (double) points.size(), value);
	}
	
	
	/**
	 * Calculates the approximate distance between two geographic points on the
	 * earth. The earth is not a globe / sphere; it is a very rough, slightly
	 * amorphous ellipsoid. We use the haversine formula in order to calculate
	 * an approximate distance
	 *
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @return
	 */
	private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		double a = FastMath.pow(FastMath.sin(dLat / 2), 2) + FastMath.cos(lat1) * FastMath.cos(lat2) * FastMath.pow(FastMath.sin(dLon / 2), 2);
		double c = 2 * FastMath.asin(FastMath.sqrt(a));
		
		// R is the radius of the earth, set above statically
		return R * c;
		
		// For height difference
		//double height = el1 - el2;
	    //distance = Math.pow(distance, 2) + Math.pow(height, 2);
	    //return Math.sqrt(distance)/1000;
	}
	
}
