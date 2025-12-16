# 🍬 Dulcinea App

## Proyecto Académico – Desarrollo de Aplicaciones Móviles (DSY1105)

Aplicación móvil de e-commerce para pastelería, desarrollada como proyecto full-stack, integrando una **app Android** con **microservicios Spring Boot**.

---

## 👥 Integrantes
- **Erick González** – Desarrollo Frontend Android, Backend Spring Boot y Base de Datos  

> Proyecto desarrollado de forma individual.

---

## 🧱 Arquitectura General

```
APP ANDROID (Frontend)
• Kotlin + Jetpack Compose
• Arquitectura MVVM + Repository
• Room Database (persistencia local)
• Retrofit (consumo de APIs REST)
• 25+ pruebas unitarias
        │ HTTP / JSON
        ▼
BACKEND SPRING BOOT (Microservicios)
• User Service (puerto 8081) - Gestión de usuarios
• Product Service (puerto 8082) - Catálogo de productos
• Spring Boot + Java 17
• Spring Data JPA + H2 Database
• API REST con CORS habilitado
```

---

## 📱 Funcionalidades Implementadas

### ✅ Frontend – App Android
- **Autenticación completa**: Login, Registro y **Recuperación de Contraseña**
- Validaciones en tiempo real desde ViewModel
- Catálogo de productos con animaciones
- Carrito de compras con persistencia local
- Perfil de usuario con cámara y galería (recurso nativo)
- Notificaciones del sistema (recurso nativo)
- Consumo de API externa (JSONPlaceholder)
- Conexión con backend propio (User Service y Product Service)
- Arquitectura MVVM completamente aplicada
- Persistencia local con Room Database
- **POO Avanzado**: Herencia, Polimorfismo, Interfaces, Clases Abstractas

### ✅ Backend – Spring Boot
- **User Service** (`http://localhost:8081`)
  - Registro, Login y Recuperación de contraseña
  - Gestión completa de usuarios
  - API REST funcional

- **Product Service** (`http://localhost:8082`)
  - Catálogo de productos
  - CRUD completo
  - Búsqueda y filtros

- Base de datos H2 en memoria
- Consola H2 habilitada
- Configuración de CORS para Android

---

## 📡 Endpoints Implementados

### User Service – `http://localhost:8081/api/users`

| Método | Endpoint | Descripción |
|------|--------|------------|
| GET | `/health` | Health check del servicio |
| POST | `/register` | Registro de usuario |
| POST | `/login` | Autenticación |
| POST | `/forgot-password` | Recuperación de contraseña |
| GET | `/` | Listar usuarios |
| GET | `/{id}` | Obtener usuario por ID |
| GET | `/email/{email}` | Obtener usuario por email |
| PUT | `/{id}` | Actualizar usuario |

### Product Service – `http://localhost:8082/api/products`

| Método | Endpoint | Descripción |
|------|--------|------------|
| GET | `/health` | Health check |
| GET | `/` | Obtener todos los productos |
| POST | `/` | Crear producto |
| GET | `/{id}` | Obtener producto por ID |

### API Externa (Prueba de Concepto)

| Método | Endpoint | Uso |
|------|--------|-----|
| GET | `https://jsonplaceholder.typicode.com/posts` | Validar Retrofit |

---

## 🧪 Pruebas Unitarias

- **25 pruebas unitarias** ejecutadas exitosamente
- Cobertura en lógica de validación y modelos
- Framework: JUnit 4 + Kotlin
- Ejecución mediante Gradle

```bash
./gradlew test
BUILD SUCCESSFUL
```

---

## 📦 APK Firmado

- **Keystore:** `dulcinea.jks`
- **APK:** `app-release.apk`
- **Proceso:** Firma desde Android Studio
- **Ubicación:** `/app/release/app-release.apk`

---

## 🚀 Cómo Ejecutar el Proyecto

### Prerrequisitos
- Java 17+
- Android Studio (Electric Eel o superior)
- IntelliJ IDEA
- Postman (opcional)

### Backend – Microservicios

**User Service**
```bash
cd user-service
./gradlew bootRun
# http://localhost:8081
```

**Product Service**
```bash
cd product-service
./gradlew bootRun
# http://localhost:8082
```

**Consolas H2**
- User Service: `http://localhost:8081/h2-console`
- Product Service: `http://localhost:8082/h2-console`

### Frontend – Android
- Abrir el proyecto en Android Studio
- Ejecutar en emulador o dispositivo físico

**Credenciales de prueba**
- Email: `cliente@dulcinea.cl`
- Password: `cliente123`

---

## 🛠 Tecnologías Utilizadas

### Android
- Kotlin
- Jetpack Compose + Material 3
- Arquitectura MVVM
- Room Database
- Retrofit 2.9.0 + Gson
- Coroutines
- Navigation Compose

### Backend
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- H2 Database
- Gradle Kotlin DSL

### Herramientas
- Android Studio
- IntelliJ IDEA
- Postman
- Git + GitHub

---

## 🎯 Cumplimiento de Requisitos Académicos

- ✅ POO con arquitectura correcta  
- ✅ Persistencia local y externa  
- ✅ Integración de recursos nativos  
- ✅ App móvil funcional con microservicios  
- ✅ Pruebas unitarias  
- ✅ APK firmado + documentación  

---

## 📁 Estructura del Proyecto

```
Dulcinea/
├── android-app/
│   └── app/src/main/java/cl/duoc/dulcinea/app/
│       ├── ui/
│       ├── viewmodel/
│       ├── repository/
│       ├── network/
│       ├── model/
│       └── database/
├── user-service/
├── product-service/
└── README.md
```

---

## 👨‍💻 Autor

**Erick González**  
Desarrollo Full Stack  
Android + Spring Boot  
Documentación y pruebas

---

## 📄 Licencia

Proyecto académico desarrollado para la evaluación  
**DSY1105 – Desarrollo de Aplicaciones Móviles**
