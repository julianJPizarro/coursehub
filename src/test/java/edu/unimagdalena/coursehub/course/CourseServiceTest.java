package edu.unimagdalena.coursehub.course;

import edu.unimagdalena.coursehub.course.dto.CourseDto;
import edu.unimagdalena.coursehub.course.dto.CreateCourseDto;
import org.junit.jupiter.api.BeforeEach; import org.junit.jupiter.api.Test; import org.mapstruct.factory.Mappers; import org.mockito.Mock; import org.mockito.MockitoAnnotations; import java.util.Optional;
import static org.assertj.core.api.Assertions.*; import static org.mockito.ArgumentMatchers.any; import static org.mockito.Mockito.*;
class CourseServiceTest {
 @Mock CourseRepository courseRepository; @Mock DepartmentRepository departmentRepository; private CourseService courseService;
 @BeforeEach void setUp(){MockitoAnnotations.openMocks(this);courseService=new CourseService(courseRepository,departmentRepository,Mappers.getMapper(CourseMapper.class));}
 @Test void shouldCreateCourseAndFlattenDepartmentIntoDto(){
  Department dep=new Department("Engineering");
  var dto=CreateCourseDto.builder().code(" java-21 ").name("Modern Java").credits(4).departmentId(7L).build();
  when(courseRepository.existsByCode("JAVA-21")).thenReturn(false); when(departmentRepository.findById(7L)).thenReturn(Optional.of(dep)); when(courseRepository.save(any(Course.class))).thenAnswer(i->i.getArgument(0));
  CourseDto result=courseService.create(dto); assertThat(result.getCode()).isEqualTo("JAVA-21"); assertThat(result.getDepartmentName()).isEqualTo("Engineering");
 }
 @Test void shouldRejectDuplicatedCodeBeforeDepartmentLookup(){
  var dto=CreateCourseDto.builder().code("JAVA-21").name("Modern Java").credits(4).departmentId(7L).build(); when(courseRepository.existsByCode("JAVA-21")).thenReturn(true);
  assertThatThrownBy(()->courseService.create(dto)).isInstanceOf(CourseCodeAlreadyExistsException.class); verify(departmentRepository,never()).findById(any());
 }
}
