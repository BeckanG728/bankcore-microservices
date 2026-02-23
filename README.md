# BankCore - Sistema de Gestión Bancaria Distribuido

![Java](https://img.shields.io/badge/Java-17%2B-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green.svg)
![Microservices](https://img.shields.io/badge/Microservices-3-orange.svg)
![Build](https://img.shields.io/badge/Build-Maven-red.svg)
![Status](https://img.shields.io/badge/Status-In_Development-lightgrey.svg)

## Visión General

Sistema bancario distribuido con arquitectura de microservicios para gestionar clientes, cuentas y transacciones.

### Arquitectura

```
┌─────────────────┐     ┌──────────────────┐
│   API Gateway   │────▶│  Eureka Server   │
│    (8080)       │     │    (8761)        │
└────────┬────────┘     └──────────────────┘
         │
    ┌────┴─────────────────────────┐
    │                              │
    ▼                              ▼
┌──────────────┐           ┌──────────────┐
│ ms-customers │           │ ms-accounts  │
│  (8081)      │           │  (8082)      │
│  + Auth/JWT  │           │  + Cuentas   │
└──────┬───────┘           └──────┬───────┘
       │                          │
       ▼                          ▼
┌──────────────┐           ┌──────────────┐
│  PostgreSQL  │           │  PostgreSQL  │
│  (5433)      │           │  (5434)      │
└──────────────┘           └──────────────┘
```

### Servicios

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| **api-gateway** | 8080 | Punto de entrada único y enrutamiento |
| **ms-customers** | 8081 | Clientes, autenticación y JWT |
| **ms-accounts** | 8082 | Cuentas y transferencias |
| **eureka-server** | 8761 | Service Discovery |

### Stack Tecnológico

- **Backend**: Java 17+, Spring Boot 3.x, Spring Cloud 2025.0.1
- **Seguridad**: Spring Security + JWT (0.12.6)
- **Service Discovery**: Netflix Eureka
- **Gateway**: Spring Cloud Gateway
- **BD**: PostgreSQL 15
- **Comunicación**: OpenFeign
- **Build**: Maven

---

## API Gateway

### Funcionamiento

El API Gateway actúa como **punto de entrada único** y se encarga de:

1. **Enrutamiento dinámico**: Descubre automáticamente los servicios mediante Eureka
2. **Balanceo de carga**: Distribuye peticiones entre instancias (`lb://servicio`)
3. **Centralización**: Unifica el acceso a todos los microservicios

### Configuración Clave

**`application.yaml`** - Rutas definidas:
```yaml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
      routes:
        - id: ms-customers
          uri: lb://ms-customers
          predicates:
            - Path=/api/auth/**, /api/customers/**
        - id: ms-accounts
          uri: lb://ms-accounts
          predicates:
            - Path=/api/accounts/**, /api/transfers/**

eureka:
  client:
    service-url:
      defaultZone: http://eureka-server:8761/eureka
```

**`pom.xml`** - Dependencias esenciales:
```xml
spring-cloud-starter-gateway
spring-cloud-starter-netflix-eureka-client
```

---

## Configuración por Microservicio

### Dependencias Comunes (`pom.xml`)

Todos los microservicios comparten:

```xml
<!-- Spring Boot Parent -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.10</version>
</parent>

<!-- Spring Cloud BOM -->
<spring-cloud.version>2025.0.1</spring-cloud.version>

<!-- Dependencias base -->
spring-boot-starter-web
spring-boot-starter-data-jpa
spring-boot-starter-security
spring-boot-starter-validation
postgresql (runtime)
lombok (optional)

<!-- Eureka Client (todos) -->
spring-cloud-starter-netflix-eureka-client
```

**Específicas por servicio:**
| Servicio | Dependencias Extra |
|----------|-------------------|
| api-gateway | `spring-cloud-starter-gateway` |
| ms-customers | `jjwt-api/impl/jackson (0.12.6)`, `mapstruct (1.6.3)` |
| ms-accounts | `spring-cloud-starter-openfeign` |

### Configuración Eureka (`application.yaml`)

Todos los servicios usan la misma configuración base:

```yaml
eureka:
  client:
    service-url:
      defaultZone: ${EUREKA_CLIENT_SERVICEURL_DEFAULTZONE:http://eureka-server:8761/eureka}
    register-with-eureka: true
    fetch-registry: true
  instance:
    prefer-ip-address: true
    hostname: ${HOSTNAME:<nombre-servicio>}
```

### Dockerfile (común para todos)

```dockerfile
FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=builder /app/target/*.jar app.jar
EXPOSE <puerto>
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## Despliegue

### Levantar servicios

```bash
docker-compose up -d --build
```

### Comandos útiles

```bash
docker-compose ps          # Ver estado
docker-compose logs -f     # Ver logs
docker-compose down        # Detener
```

### Accesos

| Servicio | URL |
|----------|-----|
| Eureka Dashboard | http://localhost:8761 |
| API Gateway | http://localhost:8080 |

---

## Endpoints (vía API Gateway)

### Autenticación

| Método | Endpoint | Body |
|--------|----------|------|
| POST | `/api/auth/register` | `{ "email": "...", "password": "...", "name": "..." }` |
| POST | `/api/auth/login` | `{ "email": "...", "password": "..." }` |

### Clientes (requiere JWT)

| Método | Endpoint | Headers |
|--------|----------|---------|
| GET | `/api/customer/me` | `Authorization: Bearer <token>` |
| GET | `/api/customer/{id}/validate` | - |

---

## Variables de Entorno

| Variable | Descripción |
|----------|-------------|
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | URL del Eureka Server |
| `SPRING_DATASOURCE_URL` | Conexión PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | Usuario BD |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña BD |
| `JWT_SECRET` | Clave secreta JWT |
| `HOSTNAME` | Nombre del host |

---

## Próximos Pasos

1. Completar implementación de `ms-accounts`
2. Pruebas de integración entre servicios
3. Implementar circuit breaker (Resilience4j)
4. Agregar documentación Swagger/OpenAPI
