package orz.springboot.data.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;
import orz.springboot.base.OrzBaseUtils;

import java.lang.reflect.ParameterizedType;
import java.util.List;

@Converter
public abstract class OrzJsonListConverter<T> implements AttributeConverter<List<T>, String> {
    private final ObjectMapper objectMapper;
    private final JavaType dataType;

    public OrzJsonListConverter() {
        this(OrzBaseUtils.getAppContext().getBean(ObjectMapper.class));
    }

    public OrzJsonListConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.dataType = objectMapper.getTypeFactory().constructCollectionType(
                List.class,
                (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]
        );
    }

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(List<T> attribute) {
        return attribute == null ? null : objectMapper.writeValueAsString(attribute);
    }

    @SneakyThrows
    @Override
    public List<T> convertToEntityAttribute(String dbData) {
        return dbData == null ? null : objectMapper.readValue(dbData, dataType);
    }
}
