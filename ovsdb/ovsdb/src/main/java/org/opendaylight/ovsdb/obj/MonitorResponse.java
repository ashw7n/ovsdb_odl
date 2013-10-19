package org.opendaylight.ovsdb.obj;


import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AsArrayTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import org.opendaylight.ovsdb.database.OvsdbType;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MonitorResponse {


    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Set.class, name = "set")})
    @JsonTypeIdResolver(OVSDBTypeIdResolver.class)
    static abstract class PolymorphicObjectMixIn
    {

    }


    @Retention(RetentionPolicy.RUNTIME)
    @JacksonAnnotation
    public static @interface OvsDBArrayTypeWrapper {
        String value();
    }

    public static class OvsDBArrayTypeWrapperDeserializer extends JsonDeserializer<Object> implements
            ContextualDeserializer {
        ObjectMapper mapper = null;
        private Class<?> wrappedType;
        private String wrapperKey;

        public OvsDBArrayTypeWrapperDeserializer() {
            this.mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule("tester", Version.unknownVersion());
            module.addDeserializer(Object.class, this);
            module.setMixInAnnotation(Object.class, PolymorphicObjectMixIn.class);
            mapper.registerModule(module);

        }

        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt,
                                          TypeDeserializer typeDeserializer)
                throws IOException, JsonProcessingException
        {
            return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
        }


        public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
                                                    BeanProperty property) throws JsonMappingException {
//            OvsDBArrayTypeWrapper wrapper = property
//                    .getAnnotation(OvsDBArrayTypeWrapper.class);
//
//            wrapperKey = wrapper.value();
//            JavaType collectionType = property.getType();
//            JavaType collectedType = collectionType.containedType(0);
//            wrappedType = collectedType.getRawClass();
            return this;
        }

        @Override
        public Object deserialize(JsonParser parser, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            return null;
        }

        private Object mapIntoObject(JsonNode node) throws IOException,
                JsonProcessingException {
            JsonParser parser = node.traverse();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(parser, wrappedType);
        }
    }

    public static class OVSDBTypeIdResolver implements TypeIdResolver
    {

        private JavaType mBaseType;

        @Override
        public void init(JavaType baseType)
        {
            mBaseType = baseType;
        }

        @Override
        public JsonTypeInfo.Id getMechanism()
        {
            return JsonTypeInfo.Id.CUSTOM;
        }

        @Override
        public String idFromValue(Object obj)
        {
            return idFromValueAndType(obj, obj.getClass());
        }

        @Override
        public String idFromBaseType()
        {
            return idFromValueAndType(null, mBaseType.getRawClass());
        }

        @Override
        public String idFromValueAndType(Object obj, Class<?> clazz)
        {
            String name = clazz.getName();
            throw new IllegalStateException("what to do ");
        }

        @Override
        public JavaType typeFromId(String type)
        {
            //return TypeFactory.defaultInstance().constructSpecializedType(mBaseType, clazz);
            return TypeFactory.defaultInstance().constructCollectionLikeType(Set.class, mBaseType);
        }
    }


    public static class Row extends ForwardingMap<String, Object>{

        Map<String, Object> backer = Maps.newHashMap();

        @Override
        protected Map<String, Object> delegate() {
            return backer;
        }
    }

    public static class RowUpdate {
        Row old;

//        @JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.WRAPPER_ARRAY)
//        @JsonDeserialize(contentAs = AsArrayTypeDeserializer.class)
//        @JsonTypeIdResolver(CommandTypeIdResolver.class)
       @JsonDeserialize(contentUsing = OvsDBArrayTypeWrapperDeserializer.class, contentAs = AsArrayTypeDeserializer.class )
        @JsonProperty(value="new")
        Row neww;
    }

    public static class TableUpdate extends ForwardingMap<String, RowUpdate> {

        Map<String, RowUpdate> backer = Maps.newHashMap();

        @Override
        protected Map<String, RowUpdate> delegate() {
           return backer;
        }
    }

    public static class TableUpdates extends ForwardingMap<String, TableUpdate> {

        Map<String, TableUpdate> backer = Maps.newHashMap();

        @Override
        protected Map<String, TableUpdate> delegate() {
            return backer;
        }
    }


}
