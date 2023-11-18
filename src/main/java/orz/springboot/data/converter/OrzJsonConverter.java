package orz.springboot.data.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;
import orz.springboot.base.OrzBaseUtils;

import java.lang.reflect.ParameterizedType;

@Converter
public abstract class OrzJsonConverter<T> implements AttributeConverter<T, String> {
    private final ObjectMapper objectMapper;
    private final JavaType dataType;

    public OrzJsonConverter() {
        this(OrzBaseUtils.getAppContext().getBean(ObjectMapper.class));
    }

    public OrzJsonConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.dataType = objectMapper.getTypeFactory().constructType(
                ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]
        );
    }

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(T attribute) {
        return attribute == null ? null : objectMapper.writeValueAsString(attribute);
    }

    @SneakyThrows
    @Override
    public T convertToEntityAttribute(String dbData) {
        return dbData == null ? null : objectMapper.readValue(dbData, dataType);
    }
}
