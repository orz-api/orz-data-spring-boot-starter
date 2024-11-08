package orz.springboot.data.repo.primary;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import orz.springboot.data.repo.primary.model.PrimaryTest1Eo;

import java.util.List;

@Mapper
public interface PrimaryTest1Mapper {
    @Select("SELECT * FROM primary_test_1")
    List<PrimaryTest1Eo> queryTestV1();

    @Insert("INSERT INTO primary_test_1(f1) VALUES(#{f1})")
    void insertTestV1(PrimaryTest1Eo eo);
}
