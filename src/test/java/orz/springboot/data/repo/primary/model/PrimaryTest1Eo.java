package orz.springboot.data.repo.primary.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import orz.springboot.data.repo.TestConverter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "primary_test_1")
public class PrimaryTest1Eo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = TestConverter.class)
    private String f1;
}
