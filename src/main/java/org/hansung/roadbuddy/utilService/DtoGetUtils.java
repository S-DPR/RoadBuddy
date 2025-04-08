package org.hansung.roadbuddy.utilService;

import org.hansung.roadbuddy.dto.google.response.googleDirections.Steps;

public class DtoGetUtils {

    public static String getArrivalStop(Steps steps) {
        return (String) steps.getTransit_details().getArrival_stop().get("name");
    }

    public static String getDepartureStop(Steps steps) {
        return (String) steps.getTransit_details().getDeparture_stop().get("name");
    }

    public static String getLineShortName(Steps steps) {
        return steps.getTransit_details().getLine().getShort_name();
    }

    public static boolean isSubway(Steps step) {
        if (!step.getTravel_mode().equals("TRANSIT")) return false;
        return step.getTransit_details().getLine().getVehicle().getType().equals("SUBWAY");
    }

    public static boolean isWalking(Steps step) {
        return step.getTravel_mode().equals("WALKING");
    }
}
