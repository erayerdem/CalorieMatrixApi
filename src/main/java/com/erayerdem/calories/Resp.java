package com.erayerdem.calories;


import lombok.Data;

import java.util.Map;

@Data
public class Resp {

    private Double sumOfCalorie;
    private Map<String,Double> foodCalorie;


}
