package org.hansung.roadbuddy.dto.utils;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

import java.util.*;

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

    public List<SubwayInfo> findDistKStationPath(SubwayInfo station, int dist, HashSet<SubwayInfo> vis) {
        if (dist == 0) return station.equals(this) ? List.of(this) : Collections.emptyList();
        List<SubwayInfo> ret = new ArrayList<>();
        vis.add(this);
        for (SubwayInfo subwayInfo: connect) {
            if (vis.contains(subwayInfo)) continue;
            if (subwayInfo.findDistKStationPath(station, dist-1, vis).isEmpty()) continue;
            ret.add(subwayInfo);
        }
        return ret;
    }

    public int findStationShortestDistance(SubwayInfo destination) {
        if (!line.equals(destination.getLine())) {
            throw new RuntimeException(destination.getLine() + " " + line + " 이 둘이 line이 달라서 만나질 못함");
        }
        ArrayDeque<SubwayInfo> queue = new ArrayDeque<>();
        HashMap<SubwayInfo, Integer> vis = new HashMap<>();
        queue.add(this);
        vis.put(this, 0);
        while (!queue.isEmpty()) {
            SubwayInfo cur = queue.pollFirst();
            int time = vis.get(cur);
            for (SubwayInfo nxt: cur.getConnect()) {
                if (vis.containsKey(nxt)) continue;
                vis.put(nxt, time+1);
                queue.add(nxt);
            }
        }
        return vis.get(destination);
    }
}
