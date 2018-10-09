package com.tvd12.ezyfoxserver.client.manager;

import com.tvd12.ezyfoxserver.client.entity.EzyZone;

import java.util.List;

/**
 * Created by tavandung12 on 10/3/18.
 */

public interface EzyZoneGroup {

    void addZone(EzyZone zone);

    EzyZone getZone();

    EzyZone getZoneById(int zoneId);

    EzyZone getZoneByName(String zoneName);

    List<EzyZone> getZoneList();

}
