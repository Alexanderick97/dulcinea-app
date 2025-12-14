# 🍬 Dulcinea App

## Proyecto Académico – Desarrollo de Aplicaciones Móviles (DSY1105)

Aplicación móvil de e-commerce para pastelería, desarrollada como proyecto full‑stack, integrando una **app Android** con **microservicios Spring Boot**.

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

        │ HTTP / JSON
        ▼

BACKEND SPRING BOOT (Microservicios)
• User Service (puerto 8081)
• Spring Boot + Java 17
• Spring Data JPA + H2 Database
• API REST con CORS habilitado
```

---

## 📱 Funcionalidades Implementadas

### Frontend – App Android
- Autenticación de usuarios (login y registro)
- Validaciones en tiempo real desde ViewModel
- Catálogo de productos con animaciones
- Carrito de compras con persistencia local
- Perfil de usuario con cámara y galería
- Notificaciones del sistema
- Consumo de API externa (JSONPlaceholder)
- Conexión con backend propio (User Service)
- Arquitectura MVVM completamente aplicada
- Persistencia local con Room Database

### Backend – Spring Boot
- Microservicio **User Service**
- Gestión completa de usuarios
- API REST funcional
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
| GET | `/` | Listar usuarios |
| GET | `/{id}` | Obtener usuario por ID |
| GET | `/email/{email}` | Obtener usuario por email |
| PUT | `/{id}` | Actualizar usuario |

### API Externa (Prueba de Concepto)

| Método | Endpoint | Uso |
|------|--------|-----|
| GET | `https://jsonplaceholder.typicode.com/posts` | Validar Retrofit |

---

## 🔧 Tecnologías Utilizadas

### Android
- Kotlin
- Jetpack Compose + Material 3
- MVVM
- Room
- Retrofit 2.9.0
- Gson
- Coroutines
- Navigation Compose

### Backend
- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Gradle Kotlin DSL

---

## 🧪 Pruebas

- 25 pruebas unitarias para validadores
- JUnit 4 + MockK
- Tests ejecutados exitosamente
- Cobertura en lógica de validación

---

## 📦 APK Firmado

- Keystore: `dulcinea.jks`
- APK generado: `app-release.apk`
- Proceso realizado desde Android Studio

---

## 🚀 Cómo Ejecutar el Proyecto

### Prerrequisitos
- Java 17+
- Android Studio
- IntelliJ IDEA
- Postman (opcional)

### Backend – User Service

```bash
cd user-service
./gradlew bootRun
```

O ejecutar directamente desde IntelliJ:
- `UserServiceApplication.java`

### Frontend – Android
- Abrir proyecto en Android Studio
- Ejecutar en emulador o dispositivo físico

---

## 📊 Estado del Proyecto

### Completado
- Arquitectura definida
- App Android funcional
- Backend User Service operativo
- Comunicación App ↔ Backend
- APK firmado

### Próximos Pasos
- Integración de Product Service
- Tests de ViewModel
- Documentación OpenAPI

---

## 📌 Notas

Proyecto con fines académicos. La estructura y decisiones técnicas están orientadas a demostrar buenas prácticas de desarrollo móvil y backend.

