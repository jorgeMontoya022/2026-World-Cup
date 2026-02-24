# 2026-World-Cup
## 📌 Descripción del Proyecto  
Este proyecto consiste en el diseño e implementación de una base de datos relacional y una aplicación de escritorio para la gestión de la información del Campeonato Mundial de Fútbol 2026, el cual se llevará a cabo en México, Estados Unidos y Canadá, y contará con la participación de 48 selecciones nacionales.  El sistema permite administrar de manera estructurada la información relacionada con:  Equipos participantes  Directores técnicos  Jugadores  Confederaciones  Países anfitriones  Ciudades sede  Estadios  Fase de grupos (12 grupos y sus respectivos partidos)  La solución incluye tanto el modelo de base de datos como el desarrollo de una aplicación de escritorio (sin uso de frameworks) conectada a un SGBD previamente definido.  

## 🔐 Sistema de Seguridad  
La aplicación incorpora un sistema básico de autenticación y control de acceso con tres tipos de usuarios:  Administrador (único usuario con este rol)  Puede crear nuevos usuarios  Acceso total al sistema  Usuario Tradicional  Puede ejecutar operaciones CRUD (Crear, Leer, Actualizar y Eliminar)  Usuario Esporádico  Solo puede ejecutar consultas  Además, el sistema cuenta con una Bitácora de Accesos, donde se registran automáticamente las fechas y horas de ingreso y salida de cada usuario.  

## 🔎 Consultas Implementadas  
El sistema permite ejecutar, como mínimo, las siguientes consultas:  Determinar el jugador más costoso por confederación.  Listar los partidos que se jugarán en un estadio seleccionado por el usuario.  Identificar el equipo más costoso que juega en cada país anfitrión (México, Estados Unidos y Canadá) durante la fase de grupos.  Calcular la cantidad de jugadores menores de 21 años por equipo. 

## 📊 Reportes Generados  
La aplicación genera reportes en formato PDF (o mediante herramienta de reportes integrada en la aplicación), incluyendo:  Listado de usuarios que ingresaron y salieron del sistema en una fecha y hora específica.  Jugadores filtrados por peso, estatura y equipo según criterios definidos por el usuario.  Valor total de los jugadores por equipo pertenecientes a una confederación específica.  Listado de los países que disputan partidos en cada país anfitrión.  🛠️ Tecnologías Utilizadas  Base de datos relacional  SGBD previamente definido  Aplicación de escritorio  Programación sin uso de frameworks
