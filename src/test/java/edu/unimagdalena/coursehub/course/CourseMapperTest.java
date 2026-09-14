package edu.unimagdalena.coursehub.course;
import edu.unimagdalena.coursehub.course.dto.CourseDto; import org.junit.jupiter.api.Test; import org.mapstruct.factory.Mappers; import static org.assertj.core.api.Assertions.assertThat;
class CourseMapperTest {
 private final CourseMapper mapper=Mappers.getMapper(CourseMapper.class);
 @Test void shouldFlattenNestedDepartment(){ Course course=new Course("JPA-01","Persistence",4,new Department("Engineering")); CourseDto dto=mapper.toDto(course); assertThat(dto.getDepartmentName()).isEqualTo("Engineering"); assertThat(dto.getCode()).isEqualTo("JPA-01"); }
}
