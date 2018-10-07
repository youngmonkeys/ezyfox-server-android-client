package com.tvd12.ezyfoxserver.client.manager;

import com.tvd12.ezyfoxserver.client.entity.EzyZone;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tavandung12 on 10/3/18.
 */

public class EzySimpleZoneManager implements EzyZoneManager {

    private final List<EzyZone> zoneList;
    private final Map<Integer, EzyZone> zonesBydId;
    private final Map<String, EzyZone> zonesByName;

    public EzySimpleZoneManager() {
        this.zoneList = new ArrayList<>();
        this.zonesBydId = new ConcurrentHashMap<>();
        this.zonesByName = new ConcurrentHashMap<>();
    }

    @Override
    public void addZone(EzyZone zone) {
        zoneList.add(zone);
        zonesBydId.put(zone.getId(), zone);
        zonesByName.put(zone.getName(), zone);
    }

    @Override
    public EzyZone getZoneById(int zoneId) {
        EzyZone zone = zonesBydId.get(zoneId);
        if(zone == null)
            throw new IllegalArgumentException("has no zone with id = " + zoneId);
        return zone;
    }

    @Override
    public EzyZone getZoneByName(String zoneName) {
        EzyZone zone = zonesByName.get(zoneName);
        if(zone == null)
            throw new IllegalArgumentException("has no zone with name = " + zoneName);
        return zone;
    }

    @Override
    public EzyZone getZone() {
        if(zoneList.isEmpty())
            throw new IllegalStateException("has no connected zone");
        EzyZone zone = zoneList.get(0);
        return zone;
    }

    @Override
    public List<EzyZone> getZoneList() {
        List<EzyZone> zones = new ArrayList<>(zoneList);
        return zones;
    }

    @Override
    public void reset() {
        zoneList.clear();
        zonesBydId.clear();
        zonesByName.clear();
    }
}
