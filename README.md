# BankCore - Sistema de Gestión Bancaria Distribuido

![Java](https://img.shields.io/badge/Java-17%2B-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green.svg)
![Microservices](https://img.shields.io/badge/Microservices-2-orange.svg)
![Build](https://img.shields.io/badge/Build-Maven-red.svg)
![Status](https://img.shields.io/badge/Status-In_Development-lightgrey.svg)

## Visión General del Proyecto

Sistema bancario distribuido basado en una arquitectura de microservicios, diseñado para gestionar clientes, cuentas bancarias y transacciones. El objetivo principal es la gestión independiente de bases de datos por servicio, comunicación síncrona entre microservicios y una seguridad robusta basada en JWT.

### Microservicios

-   **`ms-customers` (Puerto 8081)**: Gestiona clientes, autenticación y autorización.
-   **`ms-accounts` (Puerto 8082)**: Gestiona cuentas bancarias y transacciones, consumiendo `ms-customers` para validaciones.

### Tecnologías Clave

-   **Lenguaje**: Java 17+
-   **Framework**: Spring Boot 3.x
-   **Seguridad**: Spring Security + JWT
-   **Base de datos**: PostgreSQL
-   **Comunicación**: OpenFeign
-   **Mapeo**: MapStruct
-   **Build**: Maven

## Estado Actual del Proyecto (Microservicio `ms-customers`)

El microservicio `ms-customers` está en fase de desarrollo, con un enfoque reciente en la implementación y configuración del sistema de autenticación y autorización basado en JWT, y un robusto manejo de errores.

### Avances Recientes:

Se han realizado modificaciones significativas en los siguientes componentes para establecer una autenticación JWT funcional y un control de errores mejorado:

-   **`AuthController`**: Completado para gestionar las operaciones de `register` y `login`, interactuando con el servicio de autenticación.
-   **`SecurityConfig`**: Configuración fundamental de Spring Security establecida, incluyendo la cadena de filtros HTTP, `PasswordEncoder`, `AuthenticationProvider` y `AuthenticationManager`, para asegurar la aplicación y permitir el flujo de JWT.
-   **`JwtService`**: Interfaz y su implementación (`JwtServiceImpl`) actualizadas para incluir el email, `customerId` y `UserRole` en la generación de tokens JWT y permitir su extracción, asegurando que el token contenga toda la información necesaria.
-   **`AuthServiceImpl`**: Modificado para utilizar la nueva firma de `JwtService.generateToken`, asegurando que el email, `customerId` y `UserRole` se incluyan correctamente en el token durante el registro y el login. También se ha actualizado para lanzar `InvalidCredentialsException` en casos de credenciales inválidas.
-   **`JwtAuthFilter`**: Corregido para extraer el email del token JWT y utilizarlo para cargar los detalles del usuario a través del `UserDetailsService`, validando así las solicitudes autenticadas.
-   **Manejo Global de Excepciones**: Implementado un `GlobalExceptionHandler` (`@ControllerAdvice`) que, junto con la nueva `ErrorResponse` y `InvalidCredentialsException`, proporciona respuestas de error controladas y estandarizadas (ej. `401 Unauthorized`, `400 Bad Request`, `409 Conflict`) para diversos escenarios como credenciales inválidas, validaciones fallidas y duplicidad de datos.

Estos cambios consolidan el flujo de autenticación y autorización, permitiendo el registro de nuevos clientes y el inicio de sesión con tokens JWT que incluyen `customerId` y `UserRole`, y asegurando un manejo de errores consistente y amigable para el consumidor de la API.

## Próximos Pasos Sugeridos

1.  **Compilación y Verificación**: Asegurar la compilación exitosa del proyecto con todos los cambios implementados.
2.  **Pruebas Exhaustivas de Errores**: Realizar pruebas detalladas de los endpoints de autenticación (`/api/auth/register`, `/api/auth/login`) para verificar que el manejo de errores funciona como se espera en escenarios de:
    *   Registro con DNI o email duplicados.
    *   Credenciales incorrectas (email/contraseña).
    *   Payloads de solicitud inválidos.
    Asegurar que las respuestas incluyan los códigos de estado HTTP correctos y el formato `ErrorResponse` esperado.
