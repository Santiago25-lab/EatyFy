# EatyFy Backend

API REST desarrollada con Spring Boot para la aplicación EatyFy. Proporciona servicios backend para la gestión de usuarios, restaurantes, reseñas, promociones y notificaciones.

## Tecnologías

- **Framework**: Spring Boot 3.5.7
- **Java**: Versión 17
- **Base de datos**: PostgreSQL (Railway)
- **ORM**: Hibernate/JPA
- **Seguridad**: Spring Security + JWT
- **Validación**: Bean Validation
- **Email**: Spring Mail (configurado para desarrollo)
- **Build**: Maven

## Estructura del Proyecto

```
src/main/java/com/myapp/
├── Application.java              # Clase principal
├── config/                       # Configuraciones
│   ├── AppConfig.java
│   ├── CacheConfig.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtUtil.java
│   ├── SecurityConfig.java
│   └── SecurityHeadersConfig.java
├── controller/                   # Controladores REST
│   ├── AuthController.java
│   ├── CitiesController.java
│   ├── HealthController.java
│   ├── HomeController.java
│   ├── MenuItemController.java
│   ├── NotificationController.java
│   ├── PromotionController.java
│   ├── RecommendationController.java
│   ├── RestaurantController.java
│   ├── ReviewController.java
│   └── UserController.java
├── entity/                       # Entidades JPA
│   ├── MenuItem.java
│   ├── Notification.java
│   ├── Promotion.java
│   ├── Restaurant.java
│   ├── Review.java
│   └── User.java
├── repository/                   # Repositorios JPA
│   ├── MenuItemRepository.java
│   ├── NotificationRepository.java
│   ├── PromotionRepository.java
│   ├── RestaurantRepository.java
│   ├── ReviewRepository.java
│   └── UserRepository.java
└── service/                      # Servicios de negocio
    ├── EmailService.java
    ├── GeocodingService.java
    ├── MenuItemService.java
    ├── NotificationService.java
    ├── PromotionService.java
    ├── RecommendationService.java
    ├── RestaurantService.java
    ├── ReviewService.java
    ├── UserDetailsServiceImpl.java
    └── UserService.java
```

## Configuración

### Base de Datos (application.properties)
```properties
# Database configuration (Railway PostgreSQL)
spring.datasource.url=jdbc:postgresql://shuttle.proxy.rlwy.net:55900/railway
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username=postgres
spring.datasource.password=********

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Server
server.port=8084

# JWT
jwt.secret=myVeryLongSecretKeyThatIsAtLeast256BitsLongForSecurityPurposes123456789
jwt.expiration=86400000

# CORS
cors.allowed-origins=http://localhost:3000

# Email (development)
spring.mail.host=localhost
spring.mail.port=1025
```

## API Endpoints

### Autenticación
- `POST /api/auth/register` - Registrar nuevo usuario
- `POST /api/auth/login` - Iniciar sesión

### Usuarios
- `GET /api/users` - Obtener todos los usuarios (admin)
- `GET /api/users/{id}` - Obtener usuario por ID
- `GET /api/users/profile` - Obtener perfil del usuario actual
- `PUT /api/users/profile` - Actualizar perfil del usuario actual
- `DELETE /api/users/{id}` - Eliminar usuario

### Restaurantes
- `GET /api/restaurants` - Obtener todos los restaurantes (con filtros opcionales)
- `GET /api/restaurants/search` - Buscar restaurantes por ciudad/presupuesto
- `GET /api/restaurants/{id}` - Obtener restaurante por ID
- `POST /api/restaurants` - Crear nuevo restaurante (autenticado)
- `PUT /api/restaurants/{id}` - Actualizar restaurante (dueño)
- `DELETE /api/restaurants/{id}` - Eliminar restaurante (dueño)

### Reseñas
- `GET /api/reviews/restaurant/{restaurantId}` - Obtener reseñas de un restaurante
- `GET /api/reviews/my` - Obtener reseñas del usuario actual
- `POST /api/reviews` - Crear nueva reseña

### Ítems de Menú
- `GET /api/menu-items/restaurant/{restaurantId}` - Obtener menú de un restaurante
- `POST /api/menu-items` - Crear ítem de menú
- `PUT /api/menu-items/{id}` - Actualizar ítem de menú
- `DELETE /api/menu-items/{id}` - Eliminar ítem de menú

### Notificaciones
- `GET /api/notifications` - Obtener notificaciones del usuario
- `PUT /api/notifications/{id}/read` - Marcar notificación como leída

### Promociones
- `GET /api/promotions` - Obtener promociones (con filtro de ciudad)

## Instalación y Ejecución

### Prerrequisitos
- Java 17
- Maven 3.6+
- PostgreSQL (o acceso a Railway)

### Ejecutar la aplicación
```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8084`

### Ejecutar tests
```bash
mvn test
```

## Base de Datos

### Tablas Principales
- `app_users` - Usuarios de la aplicación
- `restaurant` - Restaurantes registrados
- `menu_item` - Ítems del menú
- `review` - Reseñas de usuarios
- `notification` - Notificaciones para usuarios
- `promotion` - Promociones de restaurantes

### Datos Iniciales
La aplicación incluye un `CommandLineRunner` que agrega restaurantes de ejemplo al iniciar si la base de datos está vacía.

## Seguridad

- **Autenticación**: JWT tokens
- **Autorización**: Spring Security con roles (USER, RESTAURANT)
- **CORS**: Configurado para permitir requests del frontend
- **Validación**: Bean Validation en entidades y DTOs

## Servicios Externos

- **Geocodificación**: OpenStreetMap Nominatim API para obtener coordenadas de direcciones
- **Email**: Configurado para desarrollo (consola), preparado para servicios como SendGrid

## Desarrollo

### Agregar nueva entidad
1. Crear clase en `entity/`
2. Crear repositorio en `repository/`
3. Crear servicio en `service/`
4. Crear controlador en `controller/`

### Endpoints REST
- Usar `@RestController` para controladores
- `@RequestMapping` para definir rutas base
- `@Autowired` para inyección de dependencias
- Manejo de errores con `ResponseEntity`

## Testing

La aplicación incluye tests básicos con JUnit y Spring Boot Test.

## Despliegue

La aplicación está preparada para desplegarse en servicios como:
- Railway
- Heroku
- AWS Elastic Beanstalk
- Google Cloud Run

## Contribución

1. Seguir las convenciones de código existentes
2. Agregar tests para nueva funcionalidad
3. Actualizar documentación según sea necesario
4. Usar commits descriptivos

## Licencia

Este proyecto es parte de EatyFy - Todos los derechos reservados.