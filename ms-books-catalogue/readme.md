# Microservicio Catálogo de Libros

Este proyecto implementa un **microservicio REST para la gestión de un catálogo de libros**, desarrollado con **Spring Boot** y diseñado para integrarse en una arquitectura de **microservicios** basada en **Eureka Server** y **API Gateway**.

El microservicio expone endpoints HTTP que permiten crear y consultar libros, utilizando **JSON** como formato estándar de intercambio de datos.

---

## Arquitectura y flujo de ejecución

1. El microservicio se inicia mediante Spring Boot.
2. Se registra automáticamente en **Eureka Server** con un nombre lógico.
3. El **API Gateway** descubre el servicio usando Eureka.
4. El cliente envía las peticiones HTTP al Gateway.
5. El Gateway enruta la solicitud al microservicio correspondiente.
6. El controlador procesa la petición y devuelve una respuesta en formato JSON.

---

## Componentes del sistema

- **MsBooksCatalogueApplication**  
  Clase principal que inicia el microservicio y levanta el contexto de Spring Boot.

- **Book**  
  Clase de dominio que representa la entidad Libro. Define los atributos que se almacenan en la base de datos y que se envían o reciben en formato JSON.

- **BookRepository**  
  Interfaz que gestiona el acceso a datos mediante Spring Data JPA, proporcionando automáticamente las operaciones CRUD.

- **BookSpecifications**  
  Clase utilizada para construir consultas dinámicas y aplicar filtros de búsqueda sin escribir SQL manual.

- **BookController**  
  Controlador REST que expone los endpoints del microservicio y gestiona las peticiones HTTP.

---

## Endpoints disponibles

### Obtener lista de libros

- **Método:** GET
- **URI:** `/books`
- **Request Body:** No aplica

Respuesta:
```json
[
  {
    "id": 1,
    "title": "Cien años de soledad",
    "author": "Gabriel García Márquez",
    "publisher": "Editorial Sudamericana",
    "year": 1967,
    "isbn": "9788439723868"
  },
  {
    "id": 2,
    "title": "El principito",
    "author": "Antoine de Saint-Exupéry",
    "publisher": "Reynal & Hitchcock",
    "year": 1943,
    "isbn": "9780156012195"
  }
]