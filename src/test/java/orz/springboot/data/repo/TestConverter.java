package orz.springboot.data.repo;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TestConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String attribute) {
        System.out.println("TestConverter.convertToDatabaseColumn: " + attribute);
        return attribute;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        System.out.println("TestConverter.convertToEntityAttribute: " + dbData);
        return dbData;
    }
}
