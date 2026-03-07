# BankCore: Ecosistema Bancario Distribuido

![Java](https://img.shields.io/badge/Java-17%2B-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green.svg)
![Microservices](https://img.shields.io/badge/Microservices-3-orange.svg)
![Build](https://img.shields.io/badge/Build-Maven-red.svg)
![Status](https://img.shields.io/badge/Status-In_Development-lightgrey.svg)

**BankCore** es un laboratorio vivo de arquitectura de microservicios diseñado por y para la comunidad de **Bytes Colaborativos**. Este sistema gestiona el ciclo de vida completo de una entidad bancaria: desde el registro seguro de clientes hasta la ejecución de transferencias complejas entre cuentas.

---

## ✨ Características Principales

| 🛡️ Seguridad Total | 💳 Gestión Flexible | 💸 Flujo de Dinero |
| :--- | :--- | :--- |
| Autenticación robusta basada en **JWT** para proteger cada transacción y dato sensible. | Apertura de cuentas de **Ahorro y Corriente** con validación de límites. | Motor de **Transferencias y Depósitos** con actualización de saldo en tiempo real. |

| 🚀 Arquitectura Cloud | 🔍 Transparencia | 🛡️ Resiliencia |
| :--- | :--- | :--- |
| Escalabilidad mediante **Microservicios** independientes y balanceo de carga. | Historial detallado de **Movimientos** con filtros avanzados. | Respuestas inteligentes ante fallos mediante **Feign Fallbacks**. |

---

## 🗺️ ¿Cómo fluye la información?

En lugar de un esquema rígido, el sistema se comporta como un organismo interconectado:

1.  **La Puerta (Gateway):** Todo comienza en el puerto `8080`, el guardián que valida quién eres.
2.  **El Mapa (Discovery):** El sistema consulta a **Eureka** para saber qué servicios están vivos.
3.  **El Cerebro (Customers):** Valida tu identidad y tus permisos antes de dejarte operar.
4.  **La Acción (Accounts):** Procesa tus transferencias y depósitos en las bases de datos correspondientes.

---

## 🛠️ Herramientas de Vanguardia

Para este proyecto hemos seleccionado el "Golden Stack" de la industria:

*   ☕ **Java 17+** & **Spring Boot 3.x** como base de potencia.
*   ☁️ **Spring Cloud** (Gateway, Eureka, OpenFeign) para la orquestación.
*   🐘 **PostgreSQL** para una persistencia de datos profesional.
*   🐳 **Docker** para un despliegue consistente en cualquier entorno.

---

## 🏁 ¡Pruébalo ahora!

Si tienes **Docker** instalado, levanta todo el ecosistema con un solo comando:

```bash
docker-compose up -d --build
```

> **Tip:** Una vez arriba, visita el panel de control de servicios en [http://localhost:8761](http://localhost:8761).

---

## 📂 Biblioteca Técnica (Solo si quieres profundizar)

<details>
<summary><b>🔧 Mapa de Puertos y Servicios</b></summary>

| Servicio | Puerto | Función |
|----------|--------|---------|
| `api-gateway` | 8080 | Entrada única y Seguridad |
| `ms-customers` | 8081 | Usuarios y Autenticación |
| `ms-accounts` | 8082 | Cuentas y Transferencias |
| `eureka-server` | 8761 | Directorio de Servicios |
</details>

<details>
<summary><b>📋 Principales Acciones de la API</b></summary>

Todas las peticiones pasan por el Gateway (`localhost:8080`):
*   **Usuarios:** Registro y Login vía `/api/auth/`
*   **Cuentas:** Creación y consulta vía `/api/accounts/`
*   **Movimientos:** Transferencias vía `/api/transfers/`
</details>

---

## 🤝 Colaboración en Bytes Colaborativos

Este proyecto crece con tus ideas. Si quieres mejorar el código o añadir funciones:
1. Haz un **Fork**. 2. Crea tu **Branch**. 3. Envía tu **Pull Request**.

---

## ✨ Contribuidores Principales

Este proyecto es impulsado por la dedicación de nuestros desarrolladores:

*   👤 **[Diogenes Quintero](https://github.com/dio-quincarDev)** - Arquitectura y Desarrollo Core.
*   👤 **[BeckanG728](https://github.com/BeckanG728)** - Arquitectura y Desarrollo Core.

---

**Desarrollado con ❤️ por la comunidad de Bytes Colaborativos.**
