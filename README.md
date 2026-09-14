# CourseHub Service Layer — solución

Solución de referencia con Services que reciben/devuelven DTOs, MapStruct compile-time y Lombok de forma controlada.

```bash
mvn test
mvn verify
```

Para inspeccionar el código generado por MapStruct:

```bash
find target/generated-sources/annotations -type f -name '*MapperImpl.java' -print
```
