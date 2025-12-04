# 🍬 Dulcinea App

## Evaluación Parcial 2 - Desarrollo de Aplicaciones Móviles (DSY1105)

### 👥 Integrantes del Equipo
- **Nombre Estudiante 1** - Rol: Desarrollo frontend y UI/UX
- **Nombre Estudiante 2** - Rol: Desarrollo backend y base de datos

### 📱 Funcionalidades Implementadas

#### ✅ Requisitos Cumplidos
- **Interfaz visual organizada** con navegación clara
- **Formularios validados** con retroalimentación visual en tiempo real
- **Validaciones manejadas desde lógica** (ViewModel)
- **Animaciones funcionales** en productos y transiciones
- **Estructura modular** con patrón MVVM
- **Persistencia local** con Room Database
- **Dos recursos nativos integrados**: Cámara/Galería y Notificaciones

#### 🎯 Características Técnicas
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Base de datos local**: Room con entidades User y Product
- **Consumo de APIs**: Retrofit para servicios REST
- **Serialización**: Gson para JSON
- **Concurrencia**: Coroutines para operaciones asíncronas
- **Navegación**: Jetpack Navigation Compose
- **UI**: Jetpack Compose con Material Design 3
- **Recursos nativos**:
    - Cámara y selección de galería (ProfileScreen)
    - Notificaciones del sistema (CartScreen)
    - Permisos de internet y red

### 🔗 Consumo de APIs REST

#### API Externa (Prueba de Concepto)
- **Endpoint**: `GET https://jsonplaceholder.typicode.com/posts`
- **Propósito**: Validar configuración de Retrofit y flujo de datos
- **Tecnología**: Retrofit 2.9.0 + Gson + Coroutines
- **Ubicación en código**: `network/api/ExternalApiService.kt`

#### Nuestros Microservicios (Spring Boot) - En desarrollo
- **User Service**: `http://10.0.2.2:8081/api/users/`
    - `POST /register` - Registro de usuarios
    - `POST /login` - Autenticación
    - `GET /{id}` - Obtener usuario por ID
    - `PUT /{id}` - Actualizar perfil

- **Product Service**: `http://10.0.2.2:8082/api/products/`
    - `GET /` - Listar todos los productos
    - `GET /{id}` - Obtener producto por ID
    - `POST /` - Crear nuevo producto
    - `PUT /{id}` - Actualizar producto
    - `DELETE /{id}` - Eliminar producto

#### Arquitectura de Red
    App → ViewModel → Repository → [Room Database + Retrofit Services]
    ↳ API Externa (JSONPlaceholder)
    ↳ User Service (Spring Boot)
    ↳ Product Service (Spring Boot)

#### Manejo de Estados
- **Loading**: Indicador durante peticiones HTTP
- **Error**: Mensajes para errores de conexión/timeout
- **Success**: Mostrar datos obtenidos de APIs
- **Offline**: Cache local con Room Database

### 🧪 Pruebas Unitarias
- **Herramientas**: JUnit 4, MockK, Coroutines Test
- **Cobertura**: ViewModels, Validators, Repositories
- **Ubicación**: `app/src/test/java/cl/duoc/dulcinea/app/`

### 📦 Generación de APK Firmado
- **Keystore**: `dulcinea.jks` (incluido en `/release`)
- **APK firmado**: `app-release.apk`
- **Pasos**: Build → Generate Signed Bundle / APK en Android Studio

### 📊 Estado del Proyecto

#### ✅ Completado
- [x] Consumo de API externa (JSONPlaceholder)
- [x] Configuración completa de Retrofit
- [x] Manejo de estados (loading, error, success)
- [x] Integración en navegación existente
- [x] Permisos y configuración de red

#### 🚧 En Progreso
- [ ] Conexión con microservicios propios
- [ ] Pruebas unitarias
- [ ] Generación de APK firmado
- [ ] Documentación de endpoints

#### 📋 Pendiente
- [ ] Despliegue de microservicios Spring Boot
- [ ] Sincronización local/remota
- [ ] Autenticación con tokens JWT