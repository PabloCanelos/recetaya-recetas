# RecetaYa - Microservicio de Recetas

Microservicio encargado de gestionar las recetas médicas de RecetaYa.

Permite:

- Crear recetas con sus medicamentos asociados.
- Consultar todas las recetas.
- Consultar una receta por ID.
- Filtrar recetas por estado.
- Actualizar el estado de una receta.
- Eliminar recetas y sus detalles asociados.
- Persistir información en una base de datos relacional.

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.1.1
- Maven
- Spring Data JPA
- Hibernate
- MySQL / MariaDB
- Lombok
- Bruno para pruebas de API
- Git / GitHub
- Docker

## Arquitectura interna

El proyecto está organizado en capas:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Entity
    ↓
Base de datos