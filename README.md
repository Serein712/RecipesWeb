# 🥘 MoodForFood – Backend (Spring Boot)

Este repositorio contiene la parte **backend** de la plataforma **MoodForFood**, desarrollada como Trabajo de Fin de Asignatura de **Desarrollo de Servicios Web** (UMH). MoodForFood permite gestionar usuarios, recetas, comentarios y valoraciones en una API REST segura, basada en **Spring Boot 3** y conectada a **PostgreSQL**.

---

## 📖 Descripción general

La aplicación backend ofrece los servicios necesarios para:

- Registro, autenticación y autorización de usuarios (con roles diferenciados: `USER`, `PUBLISHER`, `MODERATOR`, `ADMIN`).
- Gestión de recetas: creación, edición, eliminación y búsqueda de recetas.
- Gestión de comentarios y valoraciones sobre recetas.
- Gestión de perfiles de usuario, con datos públicos o privados según rol.
- Panel de administración: gestión de usuarios, moderación de comentarios y consulta de estadísticas.
- Autenticación basada en **JWT (JSON Web Token)** y control de acceso por roles.
- Exposición de endpoints REST que serán consumidos por un frontend (por ejemplo, React).

---

## 🏗️ Arquitectura y patrones

- **Arquitectura cliente‑servidor (API RESTful)**: separación clara entre backend (Servicios REST) y frontend (aplicación React).
- **Patrón MVC (Modelo‑Vista‑Controlador)** en el backend:
  - **Modelo**: Entidades JPA que representan tablas de la base de datos (User, Recipe, Comment, Rating, etc.).
  - **Controlador**: Endpoints REST anotados con `@RestController` o `@Controller`, que reciben peticiones HTTP y delegan en servicios.
  - **Servicio**: Lógica de negocio encapsulada en clases `@Service` (p. ej., validación, flujo de registro/login, tratamiento de comentarios).
  - **Persistencia**: Repositorios Spring Data JPA (`JpaRepository`) que abstraen el acceso a la base de datos.
- **Seguridad**:
  - **Spring Security** para filtrar peticiones, validar tokens y proteger rutas según roles.
  - **JWT** (JSON Web Token) para el transporte del token de autenticación. Permite un esquema sin estado (stateless) en el backend.
- **Base de datos relacional**:
  - **PostgreSQL** como sistema gestor de base de datos.  
  - **Hibernate** como proveedor JPA (no se emplean DDLS automáticos en producción; se usan scripts o migraciones controladas).

---

## 💻 Tecnologías utilizadas (Backend)

- **Java 17 / 21**  
- **Spring Boot 3.2.x**  
  - Spring Web (Spring MVC)  
  - Spring Data JPA (Hibernate)  
  - Spring Security  
- **JSON Web Token (JWT)**  
- **PostgreSQL** (v12 o superior)  
- **Hibernate**  
- **Maven** (gestor de dependencias y construcción)  
- **Tomcat embebido** (contenedor de servlets proporcionado por Spring Boot)  
- **Lombok** (para generación de getters/setters, constructores y anotaciones de ayuda)  

> **Nota**: El frontend (React + Vite + TailwindCSS) se encuentra en un repositorio separado y consume estos servicios REST.

---

## 🔒 Seguridad y autenticación

1. **Registro**:  
   - `POST /api/auth/register`  
   - Recibe un JSON con `{ username, email, password }`.  
   - Crea un usuario con rol `USER` (u otro rol en endpoints administrativos).  
   - La contraseña se almacena mediante hashing con **bcrypt**.

2. **Login**:
   - `POST /api/auth/login`  
   - Recibe `{ email, password }`.  
   - Si las credenciales son correctas, devuelve un **JWT** con datos del usuario (email, roles, fecha de expiración).

3. **JWT en cada petición**:
   - El frontend debe enviar el header `Authorization: Bearer <token>` en cada solicitud a rutas protegidas.
   - Un filtro (`JwtAuthenticationFilter`) intercepta la petición, valida el token, extrae el usuario y lo inyecta en el contexto de Spring Security.

4. **Control de acceso por roles**:
   - Ejemplo:  
     - `/api/recipes/**` → roles: `USER`, `PUBLISHER`, `ADMIN`  
     - `/api/admin/**` → solo `ADMIN`  
     - `/api/publisher/**` → `PUBLISHER`, `ADMIN`  
   - Configuración en `WebSecurityConfig` con `HttpSecurity.authorizeRequests()` y anotaciones `@PreAuthorize("hasRole('ADMIN')")` en métodos de servicio o controlador.

---

## 📑 API Endpoints (Resumen)

> ***Prefijo base:*** `/api`

| Categoría     | Endpoint                       | Método | Descripción                                                  | Roles                             |
|---------------|--------------------------------|--------|--------------------------------------------------------------|-----------------------------------|
| **Auth**      | `/auth/register`               | POST   | Registrar nuevo usuario.                                      | PUBLIC                            |
|               | `/auth/login`                  | POST   | Iniciar sesión y recibir JWT.                                 | PUBLIC                            |
| **Users**     | `/users/me`                    | GET    | Obtener datos del perfil del usuario autenticado.             | USER, PUBLISHER, MODERATOR, ADMIN |
|               | `/users/{id}`                  | GET    | Obtener perfil de un usuario (público).                       | PUBLIC                            |
|               | `/users/{id}`                  | PATCH  | Modificar datos de perfil propio o (si es ADMIN) de otro.     | USER, ADMIN                       |
|               | `/users/{id}`                  | DELETE | Eliminar cuenta de usuario (propia) o (si es ADMIN) la de otro.| USER, ADMIN                       |
| **Recipes**   | `/recipes`                     | GET    | Listar todas las recetas (con filtros opcionales).            | PUBLIC                            |
|               | `/recipes`                     | POST   | Crear nueva receta.                                           | USER, PUBLISHER                   |
|               | `/recipes/{id}`                | GET    | Obtener detalles de receta por ID.                            | PUBLIC                            |
|               | `/recipes/{id}`                | PATCH  | Editar receta (si es autor o ADMIN).                           | PUBLISHER, ADMIN                  |
|               | `/recipes/{id}`                | DELETE | Eliminar receta (si es autor o ADMIN).                         | PUBLISHER, ADMIN                  |
| **Comments**  | `/recipes/{recipeId}/comments` | POST   | Agregar comentario a una receta.                              | USER, PUBLISHER                   |
|               | `/recipes/{recipeId}/comments/{id}` | PATCH  | Editar comentario propio (o ADMIN).                        | USER, ADMIN                       |
|               | `/recipes/{recipeId}/comments/{id}` | DELETE | Eliminar comentario propio (o ADMIN).                      | USER, ADMIN                       |
| **Ratings**   | `/recipes/{recipeId}/ratings`  | POST   | Agregar calificación a una receta (única por usuario).        | USER, PUBLISHER                   |
|               | `/recipes/{recipeId}/ratings/{id}`  | PATCH  | Modificar calificación propia.                               | USER, PUBLISHER                   |
| **Admin**     | `/admin/users`                 | GET    | Listar todos los usuarios.                                     | ADMIN                             |
|               | `/admin/users/{id}/role`       | PATCH  | Cambiar rol de usuario.                                        | ADMIN                             |
|               | `/admin/users/{id}`            | DELETE | Eliminar usuario (con cascada en recetas, comentarios, etc.).  | ADMIN                             |
|               | `/admin/statistics`            | GET    | Obtener estadísticas generales (usuarios, recetas, comentarios) | ADMIN                             |
|               | `/admin/comments`              | GET    | Listar todos los comentarios pendientes de moderación.         | ADMIN                             |
|               | `/admin/comments/{id}/approve` | PATCH  | Aprobar comentario (o eliminar).                              | ADMIN                             |

> **Nota**: Esta tabla es un resumen. Verifica la implementación concreta en el paquete `controller/`.

