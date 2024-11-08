package orz.springboot.data.repo.primary.model;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import orz.springboot.data.repo.TestConverter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "test")
public class TestEo {
    @Id
    @Convert(converter = TestConverter.class)
    private String id;

    @Convert(converter = TestConverter.class)
    private String f1;
}
