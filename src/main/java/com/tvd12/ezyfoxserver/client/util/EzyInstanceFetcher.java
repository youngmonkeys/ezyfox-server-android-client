package com.tvd12.ezyfoxserver.client.util;

/**
 * Created by tavandung12 on 10/8/18.
 */

public interface EzyInstanceFetcher {

    <T> T get(Class<T> clazz);

}
