package orz.springboot.data.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

@Converter
public class OrzStringListConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return attribute == null ? null : String.join(",", attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        if (StringUtils.isBlank(dbData)) {
            return Collections.emptyList();
        }
        return List.of(dbData.split(","));
    }
}
