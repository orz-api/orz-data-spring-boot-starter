package orz.springboot.data.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Converter
public class OrzLongListConverter implements AttributeConverter<List<Long>, String> {
    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {
        return attribute == null ? null : attribute.stream().map(Objects::toString).collect(Collectors.joining(","));
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        if (StringUtils.isBlank(dbData)) {
            return Collections.emptyList();
        }
        return Stream.of(dbData.split(",")).map(Long::parseLong).toList();
    }
}
