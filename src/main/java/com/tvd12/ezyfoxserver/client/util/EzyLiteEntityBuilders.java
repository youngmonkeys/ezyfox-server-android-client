package com.tvd12.ezyfoxserver.client.util;

import com.tvd12.ezyfoxserver.client.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.client.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.client.entity.EzyArray;
import com.tvd12.ezyfoxserver.client.entity.EzyObject;
import com.tvd12.ezyfoxserver.client.factory.EzyLiteEntityFactory;

public class EzyLiteEntityBuilders {
	
	protected EzyArray newArray() {
		return EzyLiteEntityFactory.newArray();
	}

	protected EzyObject newObject() {
		return EzyLiteEntityFactory.newObject();
	}
	
	protected EzyArrayBuilder newArrayBuilder() {
		return EzyLiteEntityFactory.newArrayBuilder();
	}

	protected EzyObjectBuilder newObjectBuilder() {
		return EzyLiteEntityFactory.newObjectBuilder();
	}
	
}
