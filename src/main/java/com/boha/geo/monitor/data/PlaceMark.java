package com.boha.geo.monitor.data;

import lombok.Data;

@Data
public class PlaceMark {
    String name, street,
            isoCountryCode, country,
            postalCode, administrativeArea,
            locality, subLocality,
            subThoroughfare, thoroughfare;


}
