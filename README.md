# ⚽ Mundial 2026 — Sistema de Gestión

> Aplicación de escritorio **JavaFX** para la administración completa del Campeonato Mundial de Fútbol 2026 (México · Estados Unidos · Canadá).

---

## 📋 Tabla de contenidos

- [Tecnologías](#-tecnologías)
- [Requisitos previos](#-requisitos-previos)
- [Configuración de la base de datos](#-configuración-de-la-base-de-datos)
- [Ejecutar la aplicación](#-ejecutar-la-aplicación)
- [Credenciales por defecto](#-credenciales-por-defecto)
- [Funcionalidades](#-funcionalidades)
- [Estructura del proyecto](#-estructura-del-proyecto)

---

## 🛠 Tecnologías

| Capa | Tecnología |
|------|-----------|
| Lenguaje | Java 21 |
| UI | JavaFX 21 + FXML |
| Base de datos | MySQL 8 (JDBC puro, sin ORM) |
| Reportes | iText PDF 5.5.13 |
| Construcción | Maven |

---

## ✅ Requisitos previos
it
Antes de ejecutar el proyecto, asegúrate de tener instalado:

- **JDK 21** o superior
- **Maven 3.8+**
- **MySQL 9.x** corriendo en `localhost:3306`

---

## 🗄 Configuración de la base de datos

### 1. Crear la base de datos

Ejecuta el script SQL incluido en el proyecto desde MySQL Workbench o la consola:

```sql
SOURCE src/main/resources/co/edu/uniquindio/cup/world_app/db/mundial2026.sql;
```

Esto creará la base de datos `mundial2026` con todas las tablas y datos iniciales.

### 2. Configurar credenciales de conexión

Edita el archivo de conexión con tus credenciales de MySQL:

```
src/main/java/co/edu/uniquindio/cup/world_app/util/ConexionDB.java
```

```java
private static final String URL      = "jdbc:mysql://localhost:3306/mundial2026?useSSL=false&serverTimezone=UTC";
private static final String USUARIO  = "root";      // ← tu usuario
private static final String PASSWORD = "root";      // ← tu contraseña
```

---

## ▶ Ejecutar la aplicación

```bash
mvn javafx:run
```

---

## 🔑 Credenciales por defecto

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| `admin` | `admin123` | Administrador |

> ℹ️ El administrador puede crear y gestionar usuarios adicionales desde el módulo **Gestión de Usuarios**.

### Tipos de usuario

| Tipo | Permisos |
|------|----------|
| **Administrador** | Acceso total: CRUD, consultas, reportes, usuarios, bitácora |
| **Tradicional** | Consultas y visualización de datos |
| **Esporádico** | Acceso limitado de solo lectura |

---

## 🚀 Funcionalidades

### CRUD — Gestión de entidades

| Módulo | Descripción |
|--------|-------------|
| **Equipos** | 48 equipos con confederación, grupo asignado y valor de plantilla |
| **Jugadores** | Datos completos: posición, dorsal, peso, estatura, valor de mercado |
| **Directores Técnicos** | Asociados a cada equipo participante |
| **Estadios** | 16 estadios en México, Estados Unidos y Canadá |
| **Partidos** | Fase de grupos: 12 grupos × 3 partidos cada uno |

### Consultas

1. 🏆 Jugador más costoso por confederación
2. 🏟 Partidos programados en un estadio seleccionado
3. 💰 Equipo más costoso por país sede (México, USA, Canadá)
4. 🧒 Cantidad de jugadores menores de 21 años por equipo

### Reportes PDF

| Código | Reporte |
|--------|---------|
| R1 | Usuarios que ingresaron/salieron en un rango de fecha y hora |
| R2 | Jugadores filtrados por peso, estatura y equipo |
| R3 | Valor total de jugadores por equipo en una confederación |
| R4 | Partidos organizados por país anfitrión |

### Seguridad

- 🔒 Autenticación con hash **SHA-256** en contraseñas
- 👥 Control de permisos por rol en cada pantalla
- 📋 **Bitácora automática** de ingresos y salidas de sesión
- 🚫 Bloqueo tras intentos fallidos de login

---

## 📁 Estructura del proyecto

```
src/
├── main/
│   ├── java/.../world_app/
│   │   ├── model/           ← Entidades del dominio (Equipo, Jugador, Partido…)
│   │   ├── repository/      ← DAOs — acceso a MySQL con JDBC puro
│   │   ├── service/         ← Lógica de negocio y validaciones
│   │   ├── controller/      ← Controladores JavaFX vinculados a FXML
│   │   │   └── form/        ← Controladores de formularios modales
│   │   └── util/
│   │       ├── ConexionDB.java      ← Pool/singleton de conexión MySQL
│   │       ├── SeguridadUtil.java   ← Hash SHA-256
│   │       ├── SessionManager.java  ← Sesión activa del usuario
│   │       └── AlertaUtil.java      ← Diálogos reutilizables
│   │
│   └── resources/.../world_app/
│       ├── view/            ← Archivos FXML de cada módulo
│       │   └── form/        ← FXML de formularios de alta/edición
│       ├── css/
│       │   └── styles.css   ← Tema oscuro navy/gold
│       └── db/
│           └── mundial2026.sql  ← Script de creación e inserción inicial
```

---

---

>  Proyecto académico desarrollado en la **Universidad del Quindío**.
