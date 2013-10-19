package org.opendaylight.ovsdb.obj;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Sets;
import org.opendaylight.ovsdb.obj.json.Converter;

import java.util.Set;

/*This class exists just to aid in specifying annotations at type level*/

//@JsonTypeIdResolver(OVSDBTypesIDResolver.class)
//@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.WRAPPER_ARRAY)
//@JsonDeserialize(converter = ObjectToSetConverter.class)
@JsonDeserialize(converter = Converter.SetConverter.class)
public class OvsDBSet<T> extends ForwardingSet<T> {

    Set<T> target = Sets.newHashSet();

    @Override
    protected Set<T> delegate() {
        return target;
    }
}
