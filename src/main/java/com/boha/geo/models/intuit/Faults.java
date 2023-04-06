package com.boha.geo.models.intuit;

import java.util.ArrayList;
import java.util.List;

public class Faults {
    List< Fault > error = new ArrayList<>();

    public List<Fault> getError() {
        return error;
    }

    public void setError(List<Fault> error) {
        this.error = error;
    }
}
