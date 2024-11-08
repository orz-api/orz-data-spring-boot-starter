package orz.springboot.data.repo.secondary.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import orz.springboot.data.repo.TestConverter;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "test")
@IdClass(TestEo.TestId.class)
public class TestEo {
    @Id
    private String id;

    @Convert(converter = TestConverter.class)
    private String f1;

    public static class TestId implements Serializable {
        @Column(name = "id")
        @Convert(converter = TestConverter.class)
        private String id;
    }
}
