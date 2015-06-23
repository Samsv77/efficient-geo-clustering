package com.geo.cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.log4j.Logger;

import com.geo.entities.GeoPoint;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.io.GeohashUtils;
import com.spatial4j.core.shape.Point;

/**
*
* <p>Title: GeoClusterManagere</p>
* <p>Description: This class provides a public method for efficient geo clustering using dbscan and geohash</p>
* <p>Copyright: Copyright (c) 2015</p>
* <p>Company: </p>
* @author Samuel Mony
* @version 1.0
*/
public class GeoClusterManager {
	private static final Logger log = Logger.getLogger(GeoClusterManager.class);
	
	private GeoClusterCore core = new GeoClusterCore();
	
	
	/**
	 * Process geo clustering using geo hash pre-processing
	 * 
	 * @param points
	 * @param distance Maximum distance in km between each points and a centroid
	 * @param useGeohash Specify true to improve clustering by grouping by Geohash
	 * @return
	 */
	public List<GeoPoint> clusterize(List<GeoPoint> points, int distance, boolean useGeohash) throws Exception {
		if (points == null)
			throw new IllegalArgumentException();
		
		else if (points.size() == 0)
			return new ArrayList<GeoPoint>();
		
		List<DoublePoint> arrPoints = new ArrayList<DoublePoint>();
		
		// Identify geo hash level from the distance
		int geoHashLevel = (useGeohash)? distanceToGeoHashLevel(distance) : 0;
		log.debug("Geo hash level: " + geoHashLevel);
		
		if (geoHashLevel > 0) {
			// Convert latitude longitude points to reduced geo hash level
			Map<String, MutableDouble> hashPoints = mapByGeoHash(points, geoHashLevel);
			
			// Convert geohash points to list of latitude longitude points
			for (Map.Entry<String, MutableDouble> ptHash : hashPoints.entrySet()) {
				Point point = GeohashUtils.decode(ptHash.getKey(), SpatialContext.GEO);	
				arrPoints.add(new DoublePoint(new double[] { point.getY(), point.getX(), ptHash.getValue().doubleValue() }));
			}
			
		} else {
			// Convert from geo points object to double arrays without using Geo Hash
			for (GeoPoint point : points) {
				arrPoints.add(new DoublePoint(new double[] { point.getLatitude(), point.getLongitude(), point.getValue() }));
			}
		}
		
		log.debug("Processing " + arrPoints.size() + " points");
		
		// Process geo clustering
		return core.clusterDBSCAN(arrPoints, distance);
	}
	
	
	/**
	 * Group the points with similar Geohash at specified level
	 * 
	 * @param points
	 * @param level Geohash level
	 * @return
	 */
	private Map<String, MutableDouble> mapByGeoHash(List<GeoPoint> points, int level) {
		if (level < 1 || points == null)
			throw new IllegalArgumentException();
		
		Map<String, MutableDouble> hashPoints = new HashMap<String, MutableDouble>();
		
		for (GeoPoint point : points) {
			try {
				// Calculate geo hash
				String hash = GeohashUtils.encodeLatLon(point.getLatitude(), point.getLongitude());
				
				// Cut to geo hash level
				hash = hash.substring(0, level);
				
				// Group values
				MutableDouble val = hashPoints.get(hash);
				if (val == null) {
					hashPoints.put(hash, new MutableDouble(point.getValue()));
				} else {
					val.add(point.getValue());
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
		
		return hashPoints;
	}
	
	
	/**
	 * Identify most efficient geo hash level based on distance
	 * 
	 * @param distance in km
	 * @return geohash level
	 */
	protected int distanceToGeoHashLevel(int distance) {
		/*
		 * Higher geo hash level increase accuracy but also the number of points (reduce performance)
		 * 
		 * Geohash level | Distance of Adjacent Cell in Meters
		 *	-------------|-------------------------------------
		 *	1| 5003530
		 *	2| 625441
		 *	3| 123264
		 *	4| 19545
		 *	5| 3803
		 *	6| 610
		 *	7| 118
		 *	8| 19
		 *	9| 3.71
		 *	10| 0.6
		 */
		
		if (distance >= 280)
			return 3;
		else if (distance >= 80)
			return 4;
		else if (distance >= 20)
			return 5;
		else
			return -1;		// No geohash clustering to keep accuracy with small distances
	}
}
