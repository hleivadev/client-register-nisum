# client-register

API RESTful para registro de usuarios con validaciones, JWT, persistencia con H2 y buenas prácticas de arquitectura.

---

## Tecnologías utilizadas

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- Spring Security + JWT
- H2 Database (en memoria)
- Maven
- JUnit + Mockito
- Swagger / OpenAPI
- Lombok

---

## Configuración rápida

### 1. Clona el repositorio

git clone https://github.com/hleivadev/client-register.git  
cd client-register

### 2. Ejecuta el proyecto

./mvnw spring-boot:run

El backend quedará corriendo en:  
http://localhost:8080

---

## Registro de Usuario (POST `/api/v1/users/register`)

### Request body:

{
  "name": "Juan Rodriguez",
  "email": "juan@rodriguez.org",
  "password": "Password123",
  "phones": [
    {
      "number": "1234567",
      "cityCode": "1",
      "countryCode": "57"
    }
  ]
}

### Respuesta esperada (201 Created)

{
  "id": "uuid-generado",
  "created": "2025-04-29T19:01:12.250",
  "modified": "2025-04-29T19:01:12.250",
  "lastLogin": "2025-04-29T19:01:12.250",
  "token": "jwt-generado",
  "isActive": true,
  "email": "juan@rodriguez.org",
  "name": "Juan Rodriguez",
  "phones": [
    {
      "number": "1234567",
      "cityCode": "1",
      "countryCode": "57"
    }
  ]
}

### Errores posibles

- 400 Bad Request  
  {"mensaje": "La contraseña no cumple con el formato requerido"}

- 409 Conflict  
  {"mensaje": "El correo ya registrado"}

---

## Login (POST `/api/v1/auth/login`)

### Request body:

{
  "email": "juan@rodriguez.org",
  "password": "Password123"
}

### Respuesta esperada (200 OK)

{
  "token": "jwt-generado",
  "email": "juan@rodriguez.org"
}

### Errores posibles

- 400 Bad Request  
  {"mensaje": "Email o contraseña incorrectos"}

- 500 Internal Server Error  
  {"mensaje": "Error inesperado"}

---

## Validaciones

- El correo se valida con expresión regular (@Email y regex).
- La clave debe cumplir con formato configurable desde `application.properties`:

password.regex=^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{8,}$

---

## Configuración adicional

server.port=8080

# JWT  
jwt.secret=mi-secreto-jwt-super-fuerte  
jwt.expiration=3600000

# H2  
spring.datasource.url=jdbc:h2:mem:clientdb  
spring.h2.console.enabled=true

---

## Swagger UI

Disponible en:  
http://localhost:8080/swagger-ui.html

---

## Tests

Ejecuta los tests con:

./mvnw test

Incluye pruebas unitarias con Mockito para `UserService` y pruebas de integración con `MockMvc` para el `UserController`.

---

## Estructura del Proyecto (Arquitectura Hexagonal)

client-register/  
├── application/  
│   └── service/               # Lógica de negocio (casos de uso)  
├── domain/  
│   └── model/                 # Entidades y objetos de dominio  
├── infrastructure/  
│   ├── adapter/  
│   │   ├── controller/        # Controladores REST (entrada)  
│   │   └── repository/        # Adaptadores de persistencia (salida)  
│   └── config/                # Configuración general (security, swagger, etc.)  
├── ClientRegisterApplication.java  

Flujo  
Controller (Entrada): Recibe la petición.  
Service (Aplicación): Ejecuta la lógica de negocio.  
Repository (Salida): Interactúa con la base de datos.  
Entities (Dominio): Contienen las reglas del negocio.

---

## Script SQL para base de datos H2 (client-register)

DROP TABLE IF EXISTS PHONE;  
DROP TABLE IF EXISTS USER;

CREATE TABLE USER (  
    id UUID PRIMARY KEY,  
    name VARCHAR(100) NOT NULL,  
    email VARCHAR(100) NOT NULL UNIQUE,  
    password VARCHAR(255) NOT NULL,  
    created TIMESTAMP NOT NULL,  
    modified TIMESTAMP NOT NULL,  
    last_login TIMESTAMP NOT NULL,  
    is_active BOOLEAN NOT NULL,  
    token VARCHAR(255) NOT NULL  
);

CREATE TABLE PHONE (  
    id IDENTITY PRIMARY KEY,  
    number VARCHAR(20) NOT NULL,  
    city_code VARCHAR(10) NOT NULL,  
    country_code VARCHAR(10) NOT NULL,  
    user_id UUID NOT NULL,  
    FOREIGN KEY (user_id) REFERENCES USER(id) ON DELETE CASCADE  
);

-- Datos de prueba para tests  
INSERT INTO USER (id, name, email, password, created, modified, last_login, is_active, token)  
VALUES (  
    RANDOM_UUID(),  
    'Usuario Test',  
    'test@correo.com',  
    'encryptedpassword',  
    CURRENT_TIMESTAMP(),  
    CURRENT_TIMESTAMP(),  
    CURRENT_TIMESTAMP(),  
    TRUE,  
    'fake-jwt-token'  
);

INSERT INTO PHONE (number, city_code, country_code, user_id)  
SELECT '1234567', '1', '57', id FROM USER WHERE email = 'test@correo.com';

---

## Licencia

MIT © 2025 - Héctor Leiva (https://github.com/hleivadev)
