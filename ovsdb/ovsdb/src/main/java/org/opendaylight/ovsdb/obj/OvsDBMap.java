package org.opendaylight.ovsdb.obj;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.Maps;
import org.opendaylight.ovsdb.obj.json.Converter;

import java.util.HashMap;
import java.util.Map;

//@JsonTypeIdResolver(OVSDBTypesIDResolver.class)
//@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.WRAPPER_ARRAY)
@JsonDeserialize(converter = Converter.MapConverter.class)
public class OvsDBMap<K, V> extends ForwardingMap<K, V> {

    HashMap<K, V> target = Maps.newHashMap();

    @Override
    protected Map<K, V> delegate() {
        return target;
    }
}
