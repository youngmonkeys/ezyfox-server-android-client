package com.tvd12.ezyfoxserver.client.util;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tavandung12 on 9/19/18.
 */

public final class Lists {

    private Lists() {
    }

    public static <T> List<T> newArrayList(T... items) {
        return Arrays.asList(items);
    }

}
