# Efficient server side Geo Points clustering in Java

This Java library provides an efficient geo-map clustering based on GeoHash and Density-Based Spatial Clustering with Noise (DBSCAN)

## Installation


```
cd efficient-geo-clustering
mvn clean package
```


Plotting points on a map can be very useful in data visualization. The issue comes when we have too many points to display.
This geo-map clustering library provides an efficient server-side clustering.


### The problem

Consider millions of points do display in your map.

![](https://github.com/github/training-kit/blob/master/images/professortocat.png)


Different algorithms are available to provide geo-map clustering. The first one is GeoHash.
GeoHash is a geocode system calculated from the latitude and longitude of a point. The hash can be truncated at every level to reduce the precision and improve clustering.
The issue comes when we need to find the best geohash length.


The most common and convenient geo-map clustering is the Density-Based Spatial Clustering with Noise DBSCAN.
It is very efficient in terms of results, but can be expensive in cpu and time with millions of points.


### Fast and efficient clustering


This library provides a geohash based clustering to reduce the number of geo points in order to increase the performance, followed by a DBSCAN processing to increase accuracy and efficiency.


### Applying to Solr