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

* **Autenticación y Seguridad:** Registro exclusivo con correo institucional, inicio/cierre de sesión, recuperación de contraseña y gestión de políticas de datos y términos de uso.
* **Perfil de Usuario:** Gestión de perfil (nombre, biografía, carrera, semestre, fotografía), eliminación de cuenta y visualización de perfiles públicos con distribución de calificaciones.
* **Gestión Académica:** Declaración y administración de materias inscritas por periodos académicos.
* **Sistema de Reseñas y Calificaciones:** Creación, edición y eliminación de calificaciones (0 a 5 estrellas) y comentarios detallados para compañeros con materias/periodos compartidos, integrando filtros automáticos contra lenguaje ofensivo.
* **Interacción Social:** Búsqueda en vivo de estudiantes, seguimiento de perfiles, sistema de comentarios y reacciones (*like* / *dislike*) a reseñas publicadas.
* **Feed y Exploración:** Visualización cronológica de reseñas de usuarios seguidos y publicaciones recientes, con filtros avanzados por facultad, carrera y asignatura.
* **Centro de Notificaciones:** Avisos en tiempo real sobre nuevos seguidores, solicitudes, reseñas recibidas, comentarios y reacciones.

---

## 🛠️ Stack Tecnológico

* **Lenguaje:** Kotlin
* **Framework UI:** Jetpack Compose (Material 3)
* **Arquitectura:** Componentes modulares, separación de UI y fuentes de datos
* **Diseño UI/UX:** Prototipado en Figma

---

## 📁 Estructura del Proyecto

```text
app/src/main/java/com/example/thestudents/
│
├── data/                         # Capa de Datos (Modelos y Repositorios)
│   ├── local/                    # Proveedores de datos "falsos" (Mocks) para pruebas
│   │   ├── localStudentProvider.kt
│   │   ├── localReviewsProvider.kt
│   │   └── localNotificationProvider.kt
│   ├── Student.kt                # Modelo de datos para Estudiantes
│   ├── Review.kt                 # Modelo de datos para Reseñas
│   ├── Notification.kt           # Modelo de datos para Notificaciones (Enum y Data Class)
│   └── CourseSection.kt          # Modelo para agrupar materias y alumnos
│
├── ui/                           # Capa de Interfaz de Usuario (Jetpack Compose)
│   ├── screens/                  # Pantallas completas de la aplicación
│   │   ├── home/                 # Pantalla de Inicio
│   │   ├── login/                # Pantalla de Login (Stateful y Stateless)
│   │   ├── profile/              # Pantalla de Perfil de usuario
│   │   ├── reviews/              # Pantalla de listado de reseñas por materia
│   │   ├── search/               # Pantalla de búsqueda de estudiantes
│   │   └── notifications/        # Pantalla de Notificaciones
│   │
│   ├── theme/                    # Configuración visual global (Material Design 3)
│   │   ├── Color.kt              # Paleta de colores (DarkGreen, Cream, Gold, etc.)
│   │   ├── Type.kt               # Configuración de tipografías
│   │   └── Theme.kt              # Definición del tema principal (TheStudentsTheme)
│   │
│   └── utils/                    # Componentes y funciones de utilidad global
│       ├── FixedBottomBar.kt     # Barra de navegación inferior personalizada
│       ├── DiamondDivider.kt     # Divisores visuales estilizados
│       └── CustomButtons.kt      # Botones genéricos (ButtonWithIcon, etc.)
│
└── MainActivity.kt               # Punto de entrada de la aplicación y Host de navegación
 ```


## 👥 Equipo de Desarrollo


Proyecto desarrollado para la asignatura de **Desarrollo Web**:


- David Tobar

- Javier jaimes

- Juan Motta

- Andrés Díaz

 ---

*© 2026 TheStudents. Todos los derechos reservados.* 
