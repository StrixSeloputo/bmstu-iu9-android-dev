package ru.bmstu.airpollution.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
class Location {
    // latitude for returned data
    private double latitude;
    // longitude for returned data
    private double longitude;
}
