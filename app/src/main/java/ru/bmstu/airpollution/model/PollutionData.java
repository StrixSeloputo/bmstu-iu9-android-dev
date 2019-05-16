package ru.bmstu.airpollution.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PollutionData {
    // measurement precision
    private double precision;
    // atmospheric pressure at the point of measurement, hPa
    private double pressure;
    // volume mixing ratio of specified toxic gas
    private double value;
    // toxin info
    private Toxin toxin;
}
