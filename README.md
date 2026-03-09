# Proyecto prueba - CRUD Productos Alan Arellano

El proyecto se puede iniciar con el comando `./mvnw spring-boot:run`

Tecnologías
-Spring Boot 3.5
-BD H2
-Swagger
-Jacoco Report
-Lombook

## Usuarios

Para acceder a los endpoints requiere estar autenticado.
enum `UserRole`, *ADMIN* or *USER*.

## Endpoints

| Método | Path | Permisos| Descripción |
|--------| ------ |------------------------|
| GET    | /productos | Usuarios | Muestra todos los productos |
| GET    | /productos/ID | Usuarios | Muestra todos los productos filtrados por ID |
| GET    | /productos/precio/min&max | Usuarios | Muestra todos los productos filtrados por un rango de precio|
| GET    | /productos/categoria/cat | Usuarios | Muestra todos los productos filtrador por categoria |
| GET    | /productos/activos | Usuarios | Muestra todos los productos con estatus activo |
| POST   | /productos | Usuarios autenticados | Agrega un nuevo producto |
| PUT    | /productos/ID | Usuarios autenticados | Actualiza los detalles de un producto |
| DELETE | /productos/ID | Usuarios autenticados | Cambia el estatus de un producto |

## Swagger UI

URL para acceder a Swagger http://localhost:8081/swagger-ui.html

## Postman Colecciton

Se anexa colección de postman dentro de la carpeta de resources.
