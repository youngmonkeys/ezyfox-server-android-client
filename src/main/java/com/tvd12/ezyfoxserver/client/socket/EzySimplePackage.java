package com.tvd12.ezyfoxserver.client.socket;

import com.tvd12.ezyfoxserver.client.constant.EzyConstant;
import com.tvd12.ezyfoxserver.client.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.client.entity.EzyArray;

public class EzySimplePackage implements EzyPackage {

    protected EzyArray data;
    protected EzyConstant transportType;

    public EzySimplePackage(EzyArray data) {
        this(data,  EzyTransportType.TCP);
    }

    public EzySimplePackage(EzyArray data, EzyConstant transportType) {
        this.data = data;
        this.transportType = transportType;
    }

    @Override
    public EzyArray getData() {
        return data;
    }

    @Override
    public EzyConstant getTransportType() {
        return transportType;
    }

    @Override
    public void release() {
        this.data = null;
    }

}
