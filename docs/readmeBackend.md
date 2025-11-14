# ⚙️ EmprendeRed — Backend (Spring Boot)

El backend de **EmprendeRed** gestiona la lógica del negocio, el registro y autenticación de usuarios mediante contraseñas cifradas y tokens JWT.

---

## 🧱 Tecnologías utilizadas
- Java 21 lts
- Spring Boot 3.5.7
- Spring Web / JPA / Validation
- PostgreSQL
- JWT (Json Web Token)
- Maven

---

## 🧰 Requisitos previos / Dependencias
- Java 21 o superior
- Maven 3.8+
- PostgreSQL en ejecución
- IDE recomendado: IntelliJ IDEA o VS Code con extensiones de Java

---

## ⚙️ Configuración de la base de datos
Edita el archivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://aws-1-us-east-1.pooler.
spring.datasource.username=postgres.gajmoogvgyzcxbvpqzfw
spring.datasource.password=Adminpdn123
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

## Instrucciones de instalación y ejecución

git clone https://github.com/JesusEmilio16/EmprendeRed_Back_End.git

## Ejecuta el proyecto

en intellij dale al boton de ejecutar arriba en otro editor ejecuta:

mvn spring-boot:run

## Estructura de carpetas

backend/
├── src/main/java/com/example/demo/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   ├── security/
│   └── service/
├── resources/
│   └── application.properties
└── pom.xml

## Autenticación

*El usuario envía su email y contraseña a /auth/login
*Si las credenciales son válidas, se genera un token JWT.
*El token se usa en el frontend para acceder a rutas seguras.

## Despliegue del proyecto del front-end en Railway:

   1. Lo primero es ingresar a Railway y loguearse.

   2. Ingresar al apartado de dashboard.

   3. Seleccionar boton "nuevo proyecto".

   4. Seleccionar la opcion deseada, ya sea repositorio, base de datos, etc.

   5. En nuestro caso seleccionamos repositorio y luego se selecciona el repositorio que se desea subir.

   6. Despues se configura las variables (host,puerto,enlace de base de datos, nombre de usuarios, password, url) esos datos son pertenecientes a los colocados en el "application porperties", y al final se le da a la opcion de "deployment".

   ya con eso estaria el backend desplegado.
