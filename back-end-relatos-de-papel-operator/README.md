# Proyecto: back-end-relatos-de-papel-operator

## Descripción
Microservicio encargado de exponer operaciones para la gestión de órdenes de libros, alojadas en una base de datos en memoria. Desarrollado en Java con Spring Boot.

## Rutas Disponibles

| Método HTTP | URI           | Query Params | Request Body      | Response Body | Códigos HTTP de respuesta |
|-------------|---------------|--------------|-------------------|---------------|---------------------------|
| GET         | /orders       | -            | -                 | `[Order]`     | 200, 500                  |
| GET         | /orders/{id}  | -            | -                 | `Order`       | 200, 404, 500             |
| POST        | /orders       | -            | `OperatorRequest` | `Order`       | 200, 400, 404, 500        |

---

## Detalle de Endpoints

### 1. Obtener todas las órdenes
- **Método:** GET
- **URL:** `/orders`
- **Descripción:** Devuelve una lista con todas las órdenes existentes.
- **Respuesta exitosa (200):**
  ```json
  [
    {
      "id": 1,
      "books": [
        { "bookID": 10, "quantity": 2 }
      ],
      "clientID": "client123",
      "totalAmount": 59.99
    }
  ]
  ```

---

### 2. Obtener una orden por ID
- **Método:** GET
- **URL:** `/orders/{id}`
- **Parámetro de ruta:**
  - `id` (String, requerido): Identificador de la orden.
- **Descripción:** Devuelve la orden correspondiente al ID indicado.
- **Respuesta exitosa (200):**
  ```json
  {
    "id": 1,
    "books": [
      { "bookID": 10, "quantity": 2 }
    ],
    "clientID": "client123",
    "totalAmount": 59.99
  }
  ```
- **Respuesta error (404):** Orden no encontrada.

---

### 3. Crear una nueva orden
- **Método:** POST
- **URL:** `/orders`
- **Descripción:** Crea una nueva orden en la base de datos.
- **Request Body (`OperatorRequest`):**
  ```json
  {
    "books": [
      { "ID": 10, "quantity": 2 }
    ],
    "clientID": "client123",
    "totalAmount": 59.99
  }
  ```
  | Campo       | Tipo           | Requerido | Descripción                        |
  |-------------|----------------|-----------|------------------------------------|
  | books       | List<BookRequest> | Sí     | Lista de libros a ordenar          |
  | clientID    | String         | Sí        | Identificador del cliente          |
  | totalAmount | Float          | Sí        | Monto total (mayor a 0)            |

  **BookRequest:**
  | Campo    | Tipo    | Requerido | Descripción                        |
  |----------|---------|-----------|------------------------------------|
  | ID       | Integer | Sí        | Identificador del libro (mayor a 0)|
  | quantity | Integer | Sí        | Cantidad (mayor a 0)               |

- **Respuesta exitosa (200):**
  ```json
  {
    "id": 1,
    "books": [
      { "bookID": 10, "quantity": 2 }
    ],
    "clientID": "client123",
    "totalAmount": 59.99
  }
  ```
- **Respuesta error (400):** Datos incorrectos introducidos.
- **Respuesta error (404):** No se encontró el libro con el identificador indicado.

---

## Modelo de Respuesta: Order

| Campo       | Tipo           | Descripción                  |
|-------------|----------------|------------------------------|
| id          | Long           | Identificador de la orden    |
| books       | List<Book>     | Lista de libros en la orden  |
| clientID    | String         | Identificador del cliente    |
| totalAmount | Float          | Monto total de la orden      |

---

## Requisitos
- Java 17 o superior
- Maven 3.8+

## Ejecución

1. Clona el repositorio.
2. Navega al directorio del proyecto.
3. Ejecuta:
   ```bash
   mvn spring-boot:run
   ```
