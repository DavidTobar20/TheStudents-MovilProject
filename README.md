# 🎓 TheStudents - Mobile App

Bienvenido al repositorio oficial de **TheStudents**, una plataforma móvil diseñada para que estudiantes universitarios puedan reseñarse, calificarse e interactuar a partir de las materias y periodos académicos que han compartido.

Este proyecto ha sido desarrollado en **Android Studio** utilizando **Kotlin** y **Jetpack Compose**, siguiendo una arquitectura moderna y modular, principios de diseño responsivo y buenas prácticas de desarrollo móvil con interfaces fieles a los prototipos de Figma.

---

## 📋 Tabla de Contenido
- [Visión General](#-visión-general)
- [Funcionalidades Principales](#-funcionalidades-principales)
- [Stack Tecnológico](#-stack-tecnológico)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Buenas Prácticas de Desarrollo](#-buenas-prácticas-de-desarrollo)
- [Equipo de Desarrollo](#-equipo-de-desarrollo)

---

## 🌟 Visión General

**TheStudents** busca fomentar una comunidad académica colaborativa y transparente. La aplicación permite a los estudiantes registrar su historial académico, evaluar el desempeño y dinámicas de trabajo de sus compañeros de clase mediante reseñas constructivas, y mantenerse al día sobre la actividad de su red universitaria.

---

## 🚀 Funcionalidades Principales

* **Autenticación y Seguridad:** Registro, inicio/cierre de sesión, recuperación de contraseña y gestión de políticas de datos.
* **Perfil de Usuario:** Gestión de perfil (biografía, carrera, semestre, fotografía), edición de datos y visualización de perfiles públicos.
* **Gestión Académica:** Administración de materias inscritas por periodos académicos.
* **Sistema de Reseñas y Calificaciones:** Creación, edición y eliminación de calificaciones (0 a 5 estrellas) y comentarios detallados.
* **Interacción Social:** Búsqueda de estudiantes, seguimiento de perfiles, comentarios y reacciones (*like* / *dislike*) en reseñas.
* **Feed y Exploración:** Visualización cronológica de reseñas y publicaciones recientes.
* **Centro de Notificaciones:** Avisos sobre nuevos seguidores, reseñas recibidas, comentarios y reacciones.

---

## 🛠️ Stack Tecnológico

* **Lenguaje:** [Kotlin](https://kotlinlang.org/)
* **Framework UI:** [Jetpack Compose](https://developer.android.com/compose) (Material 3)
* **Navegación:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
* **Iconografía:** Material Icons Extended
* **Arquitectura:** MVVM (Model-View-ViewModel) - *En proceso de implementación completa*
* **Diseño UI/UX:** Prototipado en Figma

---

## 📁 Estructura del Proyecto

```text
app/src/main/java/com/example/thestudents/
│
├── data/                         # Capa de Datos (Modelos y Repositorios)
│   ├── local/                    # Mocks y proveedores de datos locales
│   │   ├── localStudentProvider.kt
│   │   ├── localReviewsProvider.kt
│   │   ├── localNotificationProvider.kt
│   │   ├── localCommentsProvider.kt
│   │   └── localCourseSectionProvider.kt
│   ├── Student.kt                # Modelo de Estudiante
│   ├── Review.kt                 # Modelo de Reseña
│   ├── Comment.kt                # Modelo de Comentario
│   ├── Notification.kt           # Modelo de Notificación
│   └── CourseSection.kt          # Modelo de Sección de Materia
│
├── navigation/                   # Configuración de Navegación
│   ├── AppNavigation.kt          # Host de navegación principal
│   ├── NavItem.kt                # Definición de rutas y destinos
│   └── FixedBottomBar.kt         # Barra de navegación inferior
│
├── ui/                           # Capa de Interfaz de Usuario
│   ├── screens/                  # Arquitectura por Pantalla (Screen, ViewModel, State)
│   │   ├── home/                 # Feed principal y publicaciones
│   │   ├── login/                # Inicio de sesión
│   │   ├── register/             # Registro de nuevos usuarios
│   │   ├── search/               # Buscador global de estudiantes
│   │   ├── studentDetail/        # Detalle de perfil de otros estudiantes
│   │   ├── profile/              # Perfil del usuario autenticado
│   │   ├── editarPerfil/         # Formulario de edición de perfil
│   │   ├── reviews/              # Listado de reseñas por materia
│   │   ├── writeReview/          # Creación de nuevas reseñas
│   │   ├── notifications/        # Historial de actividad y notificaciones
│   │   └── commentsReview/       # Hilos de discusión en reseñas
│   │
│   ├── components/               # Componentes de UI complejos y reutilizables
│   ├── utils/                    # Átomos de UI (Botones, campos de texto, divisores)
│   └── theme/                    # Definición de estilos, colores y tipografía (M3)
│
├── MainActivity.kt               # Punto de entrada de la aplicación
└── TheStudentsApp.kt             # Configuración global de la aplicación
 ```


---

## ⚙️ Instalación y Uso

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/tu-usuario/TheStudents-MovilProject.git
   ```
2. **Abrir en Android Studio:**
   Importa el proyecto y espera a que Gradle sincronice las dependencias.
3. **Ejecutar:**
   Selecciona un emulador o dispositivo físico con Android 8.0 (API 26) o superior y presiona `Run`.

## 👥 Equipo de Desarrollo


Proyecto desarrollado para la asignatura de **Desarrollo Web**:


- David Tobar

- Javier jaimes

- Juan Motta

- Andrés Díaz

 ---

*© 2026 TheStudents. Todos los derechos reservados.* 
