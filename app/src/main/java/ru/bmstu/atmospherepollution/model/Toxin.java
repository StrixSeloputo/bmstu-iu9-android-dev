package ru.bmstu.atmospherepollution.model;

import lombok.Getter;

public class Toxin {
    @Getter
    ToxinType type;
    @Getter
    String label = "type wasn't set";
    @Getter
    String subscript = "type wasn't set";

    public Toxin() {
    }

    public Toxin(String toxinType) {
        if (toxinType.toLowerCase().equals(ToxinType.CO.toString().toLowerCase())) {
            setType(ToxinType.CO);
        }
        if (toxinType.toLowerCase().equals(ToxinType.SO2.toString().toLowerCase())) {
            setType(ToxinType.SO2);
        }
    }

    public void setType(ToxinType type) {
        this.type = type;
        switch (type) {
            case CO:
                label = "Carbon monoxide";
                subscript = "Carbon monoxide (CO) is a colorless, odorless, and tasteless flammable gas that is slightly less dense than air. " +
                        "It is toxic to animals that use hemoglobin as an oxygen carrier (both invertebrate and vertebrate) when encountered in concentrations above about 35 ppm, although it is also produced in normal animal metabolism in low quantities, and is thought to have some normal biological functions. " +
                        "In the atmosphere, it is spatially variable and short lived, having a role in the formation of ground-level ozone.";
                break;
            case SO2:
                label = "Sulfur dioxide";
                subscript = "Sulfur dioxide (SO2) is a toxic gas with a burnt match smell. " +
                        "It is released naturally by volcanic activity and is produced as a by-product of the burning of fossil fuels contaminated with sulfur compounds and copper extraction." +
                        "Sulfur dioxide is a noticeable component in the atmosphere, especially following volcanic eruptions." +
                        "Sulfur dioxide is a major air pollutant and has significant impacts upon human health. " +
                        "In addition, the concentration of sulfur dioxide in the atmosphere can influence the habitat suitability for plant communities, as well as animal life. " +
                        "Sulfur dioxide emissions are a precursor to acid rain and atmospheric particulates." +
                        "Inhaling sulfur dioxide is associated with increased respiratory symptoms and disease, difficulty in breathing, and premature death."
                ;
                break;
        }
    }
}
