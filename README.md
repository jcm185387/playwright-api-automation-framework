# 🚀 API Automation Framework - Playwright & Java

Este repositorio contiene un framework de automatización para pruebas de API REST, desarrollado con **Java** y **Playwright**. El proyecto está diseñado bajo el patrón de arquitectura **Service Object Model (SOM)**, lo que garantiza un código modular, mantenible y escalable.

## 🛠️ Tecnologías y Herramientas
* **Lenguaje:** Java 17
* **Herramienta de Automatización:** [Playwright for Java](https://playwright.dev/java/)
* **Gestor de Dependencias:** Maven
* **Librerías de Prueba:** JUnit 5
* **API de Prueba:** [Restful-Booker](https://restful-booker.herokuapp.com/)

## 🏗️ Arquitectura del Proyecto
El framework se divide en las siguientes capas:
* `src/main/java/services/`: Contiene las clases de servicio que encapsulan las peticiones HTTP (GET, POST, etc.) y los endpoints.
* `src/test/java/tests/`: Contiene los scripts de prueba donde se ejecutan las aserciones (Asserts) y validaciones de lógica de negocio.

## ⚙️ Cómo ejecutar las pruebas
Para correr los tests desde la terminal, utiliza el siguiente comando de Maven:
```bash
mvn test
```

### 💡 Decisiones de Arquitectura
* **Test Independence:** Cada prueba genera sus propios datos de entrada. Esto garantiza que los fallos no se propaguen en cascada y facilita el debugging.
* **Service Layer:** Toda la interacción con la API está encapsulada en `BookingService`, permitiendo que los tests se enfoquen únicamente en las aserciones de negocio.
Autor: Juan Cruz Mogica - QA Automation Engineer