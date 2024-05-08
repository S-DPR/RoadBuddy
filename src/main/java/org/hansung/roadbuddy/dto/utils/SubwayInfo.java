package org.hansung.roadbuddy.dto.utils;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(of = {"line", "station"})
public class SubwayInfo {
    private String line;
    private String station;
    @Builder.Default
    @ToString.Exclude
    private List<SubwayInfo> connect = new ArrayList<>();

    @JsonSetter
    public void setPrv(List<SubwayInfo> connect) {
        if (connect != null) {
            this.connect = connect;
        }
    }

    public List<SubwayInfo> findDistKStationPath(String station, int dist, HashSet<SubwayInfo> vis) {
        System.out.println("station = " + station);
        System.out.println("dist = " + dist);
        if (dist == 0) return station.equals(this.station) ? List.of(this) : Collections.emptyList();
        List<SubwayInfo> ret = new ArrayList<>();
        vis.add(this);
        System.out.println("this = " + this);
        System.out.println("connect.size() = " + connect.size());
        for (SubwayInfo subwayInfo: connect) {
            System.out.println("this = " + this);
            System.out.println("subwayInfo = " + subwayInfo);
            System.out.println("this.hashCode() = " + this.hashCode());
            System.out.println("subwayInfo.hashCode() = " + subwayInfo.hashCode());
            String nxtStation = subwayInfo.getStation();
            if (vis.contains(subwayInfo)) continue;
            if (subwayInfo.findDistKStationPath(nxtStation, dist-1, vis).isEmpty()) continue;
            ret.add(subwayInfo);
        }
        return ret;
    }
}
