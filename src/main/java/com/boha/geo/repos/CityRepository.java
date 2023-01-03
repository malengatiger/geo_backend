package com.boha.geo.repos;

import com.boha.geo.monitor.data.City;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CityRepository extends MongoRepository<City, String> {

    /*
    // No metric: {'geoNear' : 'person', 'near' : [x, y], maxDistance : distance }
  // Metric: {'geoNear' : 'person', 'near' : [x, y], 'maxDistance' : distance,
  //          'distanceMultiplier' : metric.multiplier, 'spherical' : true }
  GeoResults<Person> findByLocationNear(Point location, Distance distance);
     */
    List<City> findByPositionNear(Point location, Distance distance);
    public com.boha.geo.models.City findByName(String name);
    public List<com.boha.geo.models.City> findByProvince(String province);
}
