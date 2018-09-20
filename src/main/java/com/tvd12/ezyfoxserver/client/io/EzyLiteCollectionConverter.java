package com.tvd12.ezyfoxserver.client.io;

import com.tvd12.ezyfoxserver.client.entity.EzyArray;

import java.util.Collection;

@SuppressWarnings({"rawtypes"})
public class EzyLiteCollectionConverter extends EzySimpleCollectionConverter {

	private final EzyOutputTransformer outputTransformer;

	public EzyLiteCollectionConverter(EzyOutputTransformer outputTransformer) {
		this.outputTransformer = outputTransformer;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <T> T toArray(Object array, Class type) {
		if(array instanceof EzyArray)
			return toArray(((EzyArray)array).toList(), type.getComponentType());
		if(array instanceof Collection)
			return toArray((Collection)array, type.getComponentType());
		return (T) outputTransformer.transform(array, type);
	}
	
}
