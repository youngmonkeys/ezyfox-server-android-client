package com.tvd12.ezyfoxserver.client.factory;

import com.tvd12.ezyfoxserver.client.io.EzyCollectionConverter;
import com.tvd12.ezyfoxserver.client.io.EzyLiteCollectionConverter;
import com.tvd12.ezyfoxserver.client.io.EzyLiteOutputTransformer;
import com.tvd12.ezyfoxserver.client.io.EzyOutputTransformer;

public class EzySimpleLiteEntityCreator extends EzySimpleEntityCreator {

	private static final EzyOutputTransformer OUTPUT_TRANSFORMER
			= new EzyLiteOutputTransformer();
	private static final EzyCollectionConverter COLLECTION_CONVERTER
			= new EzyLiteCollectionConverter(OUTPUT_TRANSFORMER);

	@Override
	protected EzyOutputTransformer getOutputTransformer() {
		return OUTPUT_TRANSFORMER;
	}

	@Override
	protected EzyCollectionConverter getCollectionConverter() {
		return COLLECTION_CONVERTER;
	}

}
