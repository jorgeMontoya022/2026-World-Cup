-- ═══════════════════════════════════════════════════════════════════════════
-- MUNDIAL 2026 — Script de creación de base de datos MySQL
-- Universidad del Quindío · Bases de Datos I
-- ═══════════════════════════════════════════════════════════════════════════

CREATE DATABASE IF NOT EXISTS mundial2026
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE mundial2026;

-- ─── Confederaciones ────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS confederaciones (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    sigla  VARCHAR(10)  NOT NULL UNIQUE
) ENGINE=InnoDB;

INSERT IGNORE INTO confederaciones (nombre, sigla) VALUES
    ('Unión de Asociaciones Europeas de Fútbol',          'UEFA'),
    ('Confederación Sudamericana de Fútbol',              'CONMEBOL'),
    ('Confederación de Fútbol de América del Norte',      'CONCACAF'),
    ('Confederación Africana de Fútbol',                  'CAF'),
    ('Confederación Asiática de Fútbol',                  'AFC'),
    ('Confederación de Fútbol de Oceanía',                'OFC');

-- ─── Grupos ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS grupos (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre CHAR(1) NOT NULL UNIQUE
) ENGINE=InnoDB;

INSERT IGNORE INTO grupos (nombre) VALUES
    ('A'),('B'),('C'),('D'),('E'),('F'),
    ('G'),('H'),('I'),('J'),('K'),('L');

-- ─── Equipos ────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS equipos (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    pais             VARCHAR(100) NOT NULL,
    bandera          VARCHAR(20)  DEFAULT '',
    confederacion_id INT NOT NULL,
    grupo_id         INT NOT NULL,
    valor_plantilla  DECIMAL(10,2) DEFAULT 0.00,
    CONSTRAINT fk_equipo_conf  FOREIGN KEY (confederacion_id) REFERENCES confederaciones(id),
    CONSTRAINT fk_equipo_grupo FOREIGN KEY (grupo_id)         REFERENCES grupos(id)
) ENGINE=InnoDB;

-- 48 equipos - Grupos oficiales Mundial 2026
-- confederacion_id: 1=UEFA  2=CONMEBOL  3=CONCACAF  4=CAF  5=AFC  6=OFC
INSERT IGNORE INTO equipos (pais, bandera, confederacion_id, grupo_id, valor_plantilla) VALUES
-- Grupo A
('Mexico',              'MX', 3,  1, 420.00),
('Sudafrica',           'ZA', 4,  1,  85.00),
('Corea del Sur',       'KR', 5,  1, 210.00),
('Rep. Checa',          'CZ', 1,  1, 310.00),
-- Grupo B
('Canada',              'CA', 3,  2, 330.00),
('Bosnia-Herzegovina',  'BA', 1,  2, 180.00),
('Qatar',               'QA', 5,  2,  70.00),
('Suiza',               'CH', 1,  2, 480.00),
-- Grupo C
('Brasil',              'BR', 2,  3,1200.00),
('Marruecos',           'MA', 4,  3, 380.00),
('Haiti',               'HT', 3,  3,  15.00),
('Escocia',             'GB', 1,  3, 290.00),
-- Grupo D
('Estados Unidos',      'US', 3,  4, 850.00),
('Paraguay',            'PY', 2,  4, 120.00),
('Australia',           'AU', 6,  4, 180.00),
('Turquia',             'TR', 1,  4, 360.00),
-- Grupo E
('Alemania',            'DE', 1,  5,1050.00),
('Curazao',             'CW', 3,  5,  25.00),
('Costa de Marfil',     'CI', 4,  5, 200.00),
('Ecuador',             'EC', 2,  5, 190.00),
-- Grupo F
('Paises Bajos',        'NL', 1,  6, 870.00),
('Japon',               'JP', 5,  6, 290.00),
('Suecia',              'SE', 1,  6, 390.00),
('Tunez',               'TN', 4,  6, 110.00),
-- Grupo G
('Belgica',             'BE', 1,  7, 760.00),
('Egipto',              'EG', 4,  7, 175.00),
('Iran',                'IR', 5,  7, 120.00),
('Nueva Zelanda',       'NZ', 6,  7,  65.00),
-- Grupo H
('Espana',              'ES', 1,  8,1100.00),
('Cabo Verde',          'CV', 4,  8,  30.00),
('Arabia Saudita',      'SA', 5,  8, 140.00),
('Uruguay',             'UY', 2,  8, 580.00),
-- Grupo I
('Francia',             'FR', 1,  9,1450.00),
('Senegal',             'SN', 4,  9, 240.00),
('Irak',                'IQ', 5,  9,  90.00),
('Noruega',             'NO', 1,  9, 520.00),
-- Grupo J
('Argentina',           'AR', 2, 10, 980.00),
('Argelia',             'DZ', 4, 10, 145.00),
('Austria',             'AT', 1, 10, 420.00),
('Jordania',            'JO', 5, 10,  40.00),
-- Grupo K
('Portugal',            'PT', 1, 11, 920.00),
('RD Congo',            'CD', 4, 11,  55.00),
('Uzbekistan',          'UZ', 5, 11,  45.00),
('Colombia',            'CO', 2, 11, 520.00),
-- Grupo L
('Inglaterra',          'EN', 1, 12,1300.00),
('Croacia',             'HR', 1, 12, 430.00),
('Ghana',               'GH', 4, 12, 150.00),
('Panama',              'PA', 3, 12,  70.00);

-- ─── Técnicos ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS tecnicos (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(80)  NOT NULL,
    apellido        VARCHAR(80)  NOT NULL,
    nacionalidad    VARCHAR(80)  NOT NULL,
    equipo_id       INT NOT NULL UNIQUE,
    titulos_ganados INT DEFAULT 0,
    CONSTRAINT fk_tecnico_equipo FOREIGN KEY (equipo_id) REFERENCES equipos(id)
) ENGINE=InnoDB;

-- Directores técnicos oficiales por grupo (imagen de referencia)
-- equipo_id sigue el orden del INSERT de equipos arriba
INSERT IGNORE INTO tecnicos (nombre, apellido, nacionalidad, equipo_id, titulos_ganados) VALUES
-- Grupo A
('Javier',    'Aguirre',          'Mexicano',        1,  0),
('Hugo',      'Broos',            'Belga',           2,  0),
('Hong',      'Myung-bo',         'Surcoreano',      3,  0),
('Miroslav',  'Koubek',           'Checo',           4,  0),
-- Grupo B
('Jesse',     'Marsch',           'Estadounidense',  5,  0),
('Sergej',    'Barbarez',         'Bosnio',          6,  0),
('Julen',     'Lopetegui',        'Español',         7,  0),
('Murat',     'Yakin',            'Suizo',           8,  0),
-- Grupo C
('Carlo',     'Ancelotti',        'Italiano',        9,  4),
('Mohamed',   'Ouahbi',           'Marroquí',       10,  0),
('Sébastien', 'Migné',            'Francés',        11,  0),
('Steve',     'Clarke',           'Escocés',        12,  0),
-- Grupo D
('Mauricio',  'Pochettino',       'Argentino',      13,  0),
('Gustavo',   'Alfaro',           'Argentino',      14,  0),
('Tony',      'Popovic',          'Australiano',    15,  0),
('Vincenzo',  'Montella',         'Italiano',       16,  0),
-- Grupo E
('Julian',    'Nagelsmann',       'Alemán',         17,  0),
('Fred',      'Rutten',           'Neerlandés',     18,  0),
('Emerse',    'Faé',              'Marfileño',      19,  0),
('Sebastián', 'Beccacece',        'Argentino',      20,  0),
-- Grupo F
('Ronald',    'Koeman',           'Neerlandés',     21,  0),
('Hajime',    'Moriyasu',         'Japonés',        22,  0),
('Graham',    'Potter',           'Inglés',         23,  0),
('Sabri',     'Lamouchi',         'Francés',        24,  0),
-- Grupo G
('Rudi',      'Garcia',           'Francés',        25,  0),
('Hossam',    'Hassan',           'Egipcio',        26,  0),
('Amir',      'Ghalenoei',        'Iraní',          27,  0),
('Darren',    'Bazeley',          'Neozelandés',    28,  0),
-- Grupo H
('Luis',      'de la Fuente',     'Español',        29,  1),
('Bubista',   'Bubista',          'Portugués',      30,  0),
('Georgios',  'Donis',            'Griego',         31,  0),
('Marcelo',   'Bielsa',           'Argentino',      32,  2),
-- Grupo I
('Didier',    'Deschamps',        'Francés',        33,  2),
('Pape',      'Thiaw',            'Senegalés',      34,  0),
('Graham',    'Arnold',           'Australiano',    35,  0),
('Ståle',     'Solbakken',        'Noruego',        36,  0),
-- Grupo J
('Lionel',    'Scaloni',          'Argentino',      37,  1),
('Vladimir',  'Petković',         'Suizo',          38,  0),
('Ralf',      'Rangnick',         'Alemán',         39,  0),
('Jamal',     'Sellami',          'Jordano',        40,  0),
-- Grupo K
('Roberto',   'Martínez',         'Español',        41,  0),
('Sébastien', 'Desabre',          'Francés',        42,  0),
('Fabio',     'Cannavaro',        'Italiano',       43,  1),
('Néstor',    'Lorenzo',          'Argentino',      44,  0),
-- Grupo L
('Thomas',    'Tuchel',           'Alemán',         45,  1),
('Zlatko',    'Dalić',            'Croata',         46,  1),
('Carlos',    'Queiroz',          'Portugués',      47,  0),
('Thomas',    'Christiansen',     'Danés',          48,  0);

-- ─── Jugadores ──────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS jugadores (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    nombre           VARCHAR(80)  NOT NULL,
    apellido         VARCHAR(80)  NOT NULL,
    fecha_nacimiento DATE,
    posicion         ENUM('Portero','Defensa','Centrocampista','Delantero') NOT NULL,
    numero_camiseta  TINYINT UNSIGNED DEFAULT 0,
    peso             DECIMAL(5,2) DEFAULT 0.00,  -- kg
    estatura         DECIMAL(4,2) DEFAULT 0.00,  -- metros
    valor            DECIMAL(10,2) DEFAULT 0.00, -- millones €
    equipo_id        INT NOT NULL,
    CONSTRAINT fk_jugador_equipo FOREIGN KEY (equipo_id) REFERENCES equipos(id)
) ENGINE=InnoDB;

-- Jugadores de muestra — representativos por grupo
-- equipo_id sigue el orden del INSERT de equipos (1=México … 48=Panamá)
INSERT IGNORE INTO jugadores (nombre, apellido, fecha_nacimiento, posicion, numero_camiseta, peso, estatura, valor, equipo_id) VALUES
-- ── Grupo A ──────────────────────────────────────────────────────────────────
-- México (id 1)
('Guillermo', 'Ochoa',        '1985-07-13', 'Portero',         1, 80.0, 1.83,  3.00,  1),
('Hirving',   'Lozano',       '1995-07-30', 'Delantero',      22, 70.0, 1.74, 18.00,  1),
('Edson',     'Álvarez',      '1997-10-24', 'Centrocampista',  4, 78.0, 1.85, 30.00,  1),
('Santiago',  'Giménez',      '2001-04-18', 'Delantero',       9, 80.0, 1.87, 45.00,  1),
-- Corea del Sur (id 3)
('Son',       'Heung-min',    '1992-07-08', 'Delantero',       7, 78.0, 1.83, 25.00,  3),
('Kim',       'Min-jae',      '1996-05-15', 'Defensa',         3, 90.0, 1.90, 55.00,  3),
-- ── Grupo B ──────────────────────────────────────────────────────────────────
-- Canadá (id 5)
('Alphonso',  'Davies',       '2000-11-02', 'Defensa',        19, 76.0, 1.80, 80.00,  5),
('Jonathan',  'David',        '2000-01-14', 'Delantero',       9, 75.0, 1.80, 65.00,  5),
-- Suiza (id 8)
('Granit',    'Xhaka',        '1992-09-27', 'Centrocampista', 10, 80.0, 1.85, 20.00,  8),
('Xherdan',   'Shaqiri',      '1991-10-10', 'Delantero',      23, 68.0, 1.69,  8.00,  8),
-- ── Grupo C ──────────────────────────────────────────────────────────────────
-- Brasil (id 9)
('Vinicius',  'Júnior',       '2000-07-12', 'Delantero',       7, 73.0, 1.76,180.00,  9),
('Rodrygo',   'Goes',         '2001-01-09', 'Delantero',      11, 68.0, 1.74,100.00,  9),
('Alisson',   'Becker',       '1992-10-02', 'Portero',         1, 91.0, 1.93, 50.00,  9),
('Casemiro',  'Casemiro',     '1992-02-23', 'Centrocampista',  5, 84.0, 1.85, 40.00,  9),
('Endrick',   'Felipe',       '2006-07-21', 'Delantero',      16, 72.0, 1.74, 60.00,  9),
-- Marruecos (id 10)
('Achraf',    'Hakimi',       '1998-11-04', 'Defensa',         2, 73.0, 1.81, 70.00, 10),
('Hakim',     'Ziyech',       '1993-03-19', 'Delantero',      22, 68.0, 1.81, 12.00, 10),
-- Escocia (id 12)
('Andrew',    'Robertson',    '1994-03-11', 'Defensa',         3, 70.0, 1.78, 25.00, 12),
('Scott',     'McTominay',    '1996-12-08', 'Centrocampista',  8, 80.0, 1.91, 35.00, 12),
-- ── Grupo D ──────────────────────────────────────────────────────────────────
-- Estados Unidos (id 13)
('Christian', 'Pulisic',      '1998-09-18', 'Delantero',      10, 70.0, 1.77, 35.00, 13),
('Tyler',     'Adams',        '1999-02-14', 'Centrocampista',  4, 73.0, 1.74, 25.00, 13),
('Folarin',   'Balogun',      '2001-07-03', 'Delantero',       9, 75.0, 1.78, 30.00, 13),
-- Turquía (id 16)
('Hakan',     'Çalhanoğlu',   '1994-02-08', 'Centrocampista', 10, 78.0, 1.79, 40.00, 16),
('Arda',      'Güler',        '2005-02-25', 'Centrocampista', 11, 68.0, 1.76, 60.00, 16),
-- ── Grupo E ──────────────────────────────────────────────────────────────────
-- Alemania (id 17)
('Florian',   'Wirtz',        '2003-05-03', 'Centrocampista', 10, 70.0, 1.76,130.00, 17),
('Jamal',     'Musiala',      '2003-02-26', 'Centrocampista',  6, 70.0, 1.75,120.00, 17),
('Manuel',    'Neuer',        '1986-03-27', 'Portero',         1, 93.0, 1.93, 10.00, 17),
('Kai',       'Havertz',      '1999-06-11', 'Delantero',       7, 79.0, 1.89, 65.00, 17),
-- Ecuador (id 20)
('Moisés',    'Caicedo',      '2001-11-02', 'Centrocampista',  4, 75.0, 1.80,100.00, 20),
('Enner',     'Valencia',     '1989-11-04', 'Delantero',      13, 75.0, 1.74,  5.00, 20),
-- ── Grupo F ──────────────────────────────────────────────────────────────────
-- Países Bajos (id 21)
('Virgil',    'van Dijk',     '1991-07-08', 'Defensa',         4, 92.0, 1.93, 35.00, 21),
('Cody',      'Gakpo',        '2000-05-07', 'Delantero',      11, 80.0, 1.89, 80.00, 21),
('Xavi',      'Simons',       '2003-04-21', 'Centrocampista', 10, 70.0, 1.77, 80.00, 21),
-- Japón (id 22)
('Takefusa',  'Kubo',         '2001-06-04', 'Delantero',      10, 67.0, 1.73, 45.00, 22),
('Wataru',    'Endo',         '1993-02-09', 'Centrocampista',  3, 78.0, 1.75, 18.00, 22),
-- ── Grupo G ──────────────────────────────────────────────────────────────────
-- Bélgica (id 25)
('Kevin',     'De Bruyne',    '1991-06-28', 'Centrocampista',  7, 70.0, 1.81, 40.00, 25),
('Romelu',    'Lukaku',       '1993-05-13', 'Delantero',       9, 94.0, 1.91, 15.00, 25),
('Lois',      'Openda',       '2000-02-16', 'Delantero',      11, 72.0, 1.79, 70.00, 25),
-- Egipto (id 26)
('Mohamed',   'Salah',        '1992-06-15', 'Delantero',      10, 71.0, 1.75, 30.00, 26),
-- ── Grupo H ──────────────────────────────────────────────────────────────────
-- España (id 29)
('Pedri',     'González',     '2002-11-25', 'Centrocampista',  8, 60.0, 1.74,120.00, 29),
('Lamine',    'Yamal',        '2007-07-13', 'Delantero',      19, 60.0, 1.76,150.00, 29),
('Unai',      'Simón',        '1997-06-11', 'Portero',         1, 84.0, 1.90, 35.00, 29),
('Dani',      'Olmo',         '1998-05-07', 'Centrocampista', 10, 70.0, 1.79, 70.00, 29),
-- Uruguay (id 32)
('Federico',  'Valverde',     '1998-07-22', 'Centrocampista',  8, 75.0, 1.82, 90.00, 32),
('Darwin',    'Núñez',        '1999-06-24', 'Delantero',       9, 81.0, 1.87, 80.00, 32),
-- ── Grupo I ──────────────────────────────────────────────────────────────────
-- Francia (id 33)
('Kylian',    'Mbappé',       '1998-12-20', 'Delantero',      10, 73.0, 1.78,180.00, 33),
('Antoine',   'Griezmann',    '1991-03-21', 'Delantero',       7, 73.0, 1.76, 35.00, 33),
('Mike',      'Maignan',      '1995-07-03', 'Portero',         1, 84.0, 1.91, 55.00, 33),
('Aurélien',  'Tchouaméni',   '2000-01-27', 'Centrocampista',  8, 80.0, 1.87, 80.00, 33),
-- Noruega (id 36)
('Erling',    'Haaland',      '2000-07-21', 'Delantero',       9, 88.0, 1.94,200.00, 36),
('Martin',    'Ødegaard',     '1998-12-17', 'Centrocampista',  8, 68.0, 1.78, 90.00, 36),
-- ── Grupo J ──────────────────────────────────────────────────────────────────
-- Argentina (id 37)
('Lionel',    'Messi',        '1987-06-24', 'Delantero',      10, 72.0, 1.70, 35.00, 37),
('Emiliano',  'Martínez',     '1992-09-02', 'Portero',         1, 88.0, 1.95, 40.00, 37),
('Julián',    'Álvarez',      '2000-01-31', 'Delantero',        9, 70.0, 1.70, 90.00, 37),
('Rodrigo',   'De Paul',      '1994-05-24', 'Centrocampista',  7, 75.0, 1.80, 45.00, 37),
-- Austria (id 39)
('Marcel',    'Sabitzer',     '1994-03-17', 'Centrocampista',  8, 76.0, 1.80, 22.00, 39),
('David',     'Alaba',        '1992-06-24', 'Defensa',         6, 78.0, 1.80, 15.00, 39),
-- ── Grupo K ──────────────────────────────────────────────────────────────────
-- Portugal (id 41)
('Cristiano', 'Ronaldo',      '1985-02-05', 'Delantero',       7, 83.0, 1.87, 15.00, 41),
('Rafael',    'Leão',         '1999-06-10', 'Delantero',      11, 79.0, 1.88, 80.00, 41),
('Bernardo',  'Silva',        '1994-08-10', 'Centrocampista', 10, 64.0, 1.73, 60.00, 41),
('Rúben',     'Dias',         '1997-05-14', 'Defensa',         4, 76.0, 1.87, 70.00, 41),
-- Colombia (id 44)
('Luis',      'Díaz',         '1997-01-13', 'Delantero',       7, 73.0, 1.78, 80.00, 44),
('James',     'Rodríguez',    '1991-07-12', 'Centrocampista', 10, 75.0, 1.80, 12.00, 44),
('Richard',   'Ríos',         '2000-09-08', 'Centrocampista',  8, 72.0, 1.76, 35.00, 44),
-- ── Grupo L ──────────────────────────────────────────────────────────────────
-- Inglaterra (id 45)
('Jude',      'Bellingham',   '2003-06-29', 'Centrocampista', 22, 75.0, 1.86,180.00, 45),
('Harry',     'Kane',         '1993-07-28', 'Delantero',       9, 86.0, 1.88,100.00, 45),
('Phil',      'Foden',        '2000-05-28', 'Centrocampista', 47, 69.0, 1.71, 90.00, 45),
('Bukayo',    'Saka',         '2001-09-05', 'Delantero',       7, 72.0, 1.78,120.00, 45),
-- Croacia (id 46)
('Luka',      'Modrić',       '1985-09-09', 'Centrocampista', 10, 66.0, 1.72, 10.00, 46),
('Ivan',      'Perišić',      '1989-02-02', 'Delantero',       4, 79.0, 1.86,  8.00, 46),
-- Ghana (id 47)
('Mohammed',  'Kudus',        '2000-08-02', 'Centrocampista', 10, 72.0, 1.77, 55.00, 47),
('Jordan',    'Ayew',         '1991-09-11', 'Delantero',       9, 75.0, 1.79,  5.00, 47);

-- ─── Ciudades ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS ciudades (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    pais   VARCHAR(50)  NOT NULL
) ENGINE=InnoDB;

INSERT IGNORE INTO ciudades (nombre, pais) VALUES
-- México
('Ciudad de México', 'México'),
('Guadalajara',      'México'),
('Monterrey',        'México'),
-- USA
('Nueva York',       'USA'),
('Los Ángeles',      'USA'),
('Dallas',           'USA'),
('San Francisco',    'USA'),
('Seattle',          'USA'),
('Boston',           'USA'),
('Miami',            'USA'),
('Atlanta',          'USA'),
-- Canadá
('Toronto',          'Canadá'),
('Vancouver',        'Canadá');

-- ─── Estadios ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS estadios (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(150) NOT NULL,
    ciudad_id  INT NOT NULL,
    capacidad  INT DEFAULT 0,
    CONSTRAINT fk_estadio_ciudad FOREIGN KEY (ciudad_id) REFERENCES ciudades(id)
) ENGINE=InnoDB;

INSERT IGNORE INTO estadios (nombre, ciudad_id, capacidad) VALUES
-- México
('Estadio Azteca',                  1, 87523),
('Estadio Akron',                   2, 49850),
('Estadio BBVA',                    3, 53500),
-- USA
('MetLife Stadium',                 4, 82500),
('SoFi Stadium',                    5, 70240),
('AT&T Stadium',                    6, 80000),
('Levi\'s Stadium',                7, 68500),
('Lumen Field',                     8, 69000),
('Gillette Stadium',                9, 65878),
('Hard Rock Stadium',              10, 65326),
('Mercedes-Benz Stadium',          11, 71000),
-- Canadá
('BMO Field',                      12, 45736),
('BC Place',                       13, 54500);

-- ─── Partidos ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS partidos (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    equipo_local_id     INT NOT NULL,
    equipo_visitante_id INT NOT NULL,
    estadio_id          INT NOT NULL,
    grupo_id            INT NOT NULL,
    fecha_hora          DATETIME,
    goles_local         TINYINT UNSIGNED,
    goles_visitante     TINYINT UNSIGNED,
    CONSTRAINT fk_partido_local     FOREIGN KEY (equipo_local_id)     REFERENCES equipos(id),
    CONSTRAINT fk_partido_visitante FOREIGN KEY (equipo_visitante_id) REFERENCES equipos(id),
    CONSTRAINT fk_partido_estadio   FOREIGN KEY (estadio_id)          REFERENCES estadios(id),
    CONSTRAINT fk_partido_grupo     FOREIGN KEY (grupo_id)            REFERENCES grupos(id)
) ENGINE=InnoDB;

-- Partidos de muestra — fase de grupos (3 partidos por grupo = 36 total)
-- Cada grupo: partido 1 vs 4, 2 vs 3, 1 vs 3, 2 vs 4, 1 vs 2, 3 vs 4
-- equipo_id: 1=México 2=Sudáfrica 3=Corea del Sur 4=Rep.Checa
--            5=Canadá 6=Bosnia 7=Qatar 8=Suiza
--            9=Brasil 10=Marruecos 11=Haití 12=Escocia
--           13=EE.UU. 14=Paraguay 15=Australia 16=Turquía
--           17=Alemania 18=Curazao 19=C.Marfil 20=Ecuador
--           21=P.Bajos 22=Japón 23=Suecia 24=Túnez
--           25=Bélgica 26=Egipto 27=Irán 28=N.Zelanda
--           29=España 30=C.Verde 31=A.Saudita 32=Uruguay
--           33=Francia 34=Senegal 35=Irak 36=Noruega
--           37=Argentina 38=Argelia 39=Austria 40=Jordania
--           41=Portugal 42=RD Congo 43=Uzbekistán 44=Colombia
--           45=Inglaterra 46=Croacia 47=Ghana 48=Panamá
INSERT IGNORE INTO partidos (equipo_local_id, equipo_visitante_id, estadio_id, grupo_id, fecha_hora) VALUES
-- ── Grupo A ──────────────────────────────────────────────────────────────────
( 1,  4,  1, 1, '2026-06-11 20:00:00'),  -- México vs Rep. Checa — Azteca
( 2,  3,  3, 1, '2026-06-11 17:00:00'),  -- Sudáfrica vs Corea del Sur — BBVA
( 1,  3,  2, 1, '2026-06-15 20:00:00'),  -- México vs Corea del Sur — Akron
( 2,  4,  4, 1, '2026-06-15 17:00:00'),  -- Sudáfrica vs Rep. Checa — MetLife
( 1,  2,  1, 1, '2026-06-19 22:00:00'),  -- México vs Sudáfrica — Azteca
( 3,  4,  5, 1, '2026-06-19 22:00:00'),  -- Corea del Sur vs Rep. Checa — SoFi
-- ── Grupo B ──────────────────────────────────────────────────────────────────
( 5,  8, 12, 2, '2026-06-12 20:00:00'),  -- Canadá vs Suiza — BMO Field
( 6,  7,  9, 2, '2026-06-12 17:00:00'),  -- Bosnia vs Qatar — Gillette
( 5,  7, 13, 2, '2026-06-16 20:00:00'),  -- Canadá vs Qatar — BC Place
( 6,  8,  4, 2, '2026-06-16 17:00:00'),  -- Bosnia vs Suiza — MetLife
( 5,  6, 12, 2, '2026-06-20 22:00:00'),  -- Canadá vs Bosnia — BMO Field
( 7,  8,  8, 2, '2026-06-20 22:00:00'),  -- Qatar vs Suiza — Lumen Field
-- ── Grupo C ──────────────────────────────────────────────────────────────────
( 9, 12,  6, 3, '2026-06-13 20:00:00'),  -- Brasil vs Escocia — AT&T
(10, 11,  7, 3, '2026-06-13 17:00:00'),  -- Marruecos vs Haití — Levi's
( 9, 11, 11, 3, '2026-06-17 20:00:00'),  -- Brasil vs Haití — Mercedes-Benz
(10, 12,  4, 3, '2026-06-17 17:00:00'),  -- Marruecos vs Escocia — MetLife
( 9, 10,  6, 3, '2026-06-21 22:00:00'),  -- Brasil vs Marruecos — AT&T
(11, 12,  5, 3, '2026-06-21 22:00:00'),  -- Haití vs Escocia — SoFi
-- ── Grupo D ──────────────────────────────────────────────────────────────────
(13, 16,  4, 4, '2026-06-14 20:00:00'),  -- EE.UU. vs Turquía — MetLife
(14, 15, 10, 4, '2026-06-14 17:00:00'),  -- Paraguay vs Australia — Hard Rock
(13, 15,  6, 4, '2026-06-18 20:00:00'),  -- EE.UU. vs Australia — AT&T
(14, 16,  9, 4, '2026-06-18 17:00:00'),  -- Paraguay vs Turquía — Gillette
(13, 14,  4, 4, '2026-06-22 22:00:00'),  -- EE.UU. vs Paraguay — MetLife
(15, 16,  8, 4, '2026-06-22 22:00:00'),  -- Australia vs Turquía — Lumen Field
-- ── Grupo E ──────────────────────────────────────────────────────────────────
(17, 20,  6, 5, '2026-06-15 20:00:00'),  -- Alemania vs Ecuador — AT&T
(18, 19, 11, 5, '2026-06-15 17:00:00'),  -- Curazao vs C.Marfil — Mercedes-Benz
(17, 19,  4, 5, '2026-06-19 20:00:00'),  -- Alemania vs C.Marfil — MetLife
(18, 20,  5, 5, '2026-06-19 17:00:00'),  -- Curazao vs Ecuador — SoFi
(17, 18,  6, 5, '2026-06-23 22:00:00'),  -- Alemania vs Curazao — AT&T
(19, 20,  7, 5, '2026-06-23 22:00:00'),  -- C.Marfil vs Ecuador — Levi's
-- ── Grupo F ──────────────────────────────────────────────────────────────────
(21, 24,  5, 6, '2026-06-16 20:00:00'),  -- P.Bajos vs Túnez — SoFi
(22, 23, 12, 6, '2026-06-16 17:00:00'),  -- Japón vs Suecia — BMO Field
(21, 23,  4, 6, '2026-06-20 20:00:00'),  -- P.Bajos vs Suecia — MetLife
(22, 24, 13, 6, '2026-06-20 17:00:00'),  -- Japón vs Túnez — BC Place
(21, 22,  5, 6, '2026-06-24 22:00:00'),  -- P.Bajos vs Japón — SoFi
(23, 24,  8, 6, '2026-06-24 22:00:00'),  -- Suecia vs Túnez — Lumen Field
-- ── Grupo G ──────────────────────────────────────────────────────────────────
(25, 28, 11, 7, '2026-06-17 20:00:00'),  -- Bélgica vs N.Zelanda — Mercedes-Benz
(26, 27, 10, 7, '2026-06-17 17:00:00'),  -- Egipto vs Irán — Hard Rock
(25, 27,  4, 7, '2026-06-21 20:00:00'),  -- Bélgica vs Irán — MetLife
(26, 28,  9, 7, '2026-06-21 17:00:00'),  -- Egipto vs N.Zelanda — Gillette
(25, 26, 11, 7, '2026-06-25 22:00:00'),  -- Bélgica vs Egipto — Mercedes-Benz
(27, 28,  7, 7, '2026-06-25 22:00:00'),  -- Irán vs N.Zelanda — Levi's
-- ── Grupo H ──────────────────────────────────────────────────────────────────
(29, 32,  1, 8, '2026-06-18 20:00:00'),  -- España vs Uruguay — Azteca
(30, 31,  2, 8, '2026-06-18 17:00:00'),  -- Cabo Verde vs A.Saudita — Akron
(29, 31,  3, 8, '2026-06-22 20:00:00'),  -- España vs A.Saudita — BBVA
(30, 32,  1, 8, '2026-06-22 17:00:00'),  -- Cabo Verde vs Uruguay — Azteca
(29, 30,  1, 8, '2026-06-26 22:00:00'),  -- España vs Cabo Verde — Azteca
(31, 32,  2, 8, '2026-06-26 22:00:00'),  -- A.Saudita vs Uruguay — Akron
-- ── Grupo I ──────────────────────────────────────────────────────────────────
(33, 36,  5, 9, '2026-06-19 20:00:00'),  -- Francia vs Noruega — SoFi
(34, 35,  6, 9, '2026-06-19 17:00:00'),  -- Senegal vs Irak — AT&T
(33, 35,  4, 9, '2026-06-23 20:00:00'),  -- Francia vs Irak — MetLife
(34, 36, 12, 9, '2026-06-23 17:00:00'),  -- Senegal vs Noruega — BMO Field
(33, 34,  5, 9, '2026-06-27 22:00:00'),  -- Francia vs Senegal — SoFi
(35, 36,  8, 9, '2026-06-27 22:00:00'),  -- Irak vs Noruega — Lumen Field
-- ── Grupo J ──────────────────────────────────────────────────────────────────
(37, 40,  6, 10, '2026-06-20 20:00:00'), -- Argentina vs Jordania — AT&T
(38, 39, 11, 10, '2026-06-20 17:00:00'), -- Argelia vs Austria — Mercedes-Benz
(37, 39,  4, 10, '2026-06-24 20:00:00'), -- Argentina vs Austria — MetLife
(38, 40,  9, 10, '2026-06-24 17:00:00'), -- Argelia vs Jordania — Gillette
(37, 38,  6, 10, '2026-06-28 22:00:00'), -- Argentina vs Argelia — AT&T
(39, 40,  7, 10, '2026-06-28 22:00:00'), -- Austria vs Jordania — Levi's
-- ── Grupo K ──────────────────────────────────────────────────────────────────
(41, 44,  5, 11, '2026-06-21 20:00:00'), -- Portugal vs Colombia — SoFi
(42, 43, 13, 11, '2026-06-21 17:00:00'), -- RD Congo vs Uzbekistán — BC Place
(41, 43,  4, 11, '2026-06-25 20:00:00'), -- Portugal vs Uzbekistán — MetLife
(42, 44, 10, 11, '2026-06-25 17:00:00'), -- RD Congo vs Colombia — Hard Rock
(41, 42,  5, 11, '2026-06-29 22:00:00'), -- Portugal vs RD Congo — SoFi
(43, 44,  8, 11, '2026-06-29 22:00:00'), -- Uzbekistán vs Colombia — Lumen Field
-- ── Grupo L ──────────────────────────────────────────────────────────────────
(45, 48,  4, 12, '2026-06-22 20:00:00'), -- Inglaterra vs Panamá — MetLife
(46, 47, 11, 12, '2026-06-22 17:00:00'), -- Croacia vs Ghana — Mercedes-Benz
(45, 47,  9, 12, '2026-06-26 20:00:00'), -- Inglaterra vs Ghana — Gillette
(46, 48, 10, 12, '2026-06-26 17:00:00'), -- Croacia vs Panamá — Hard Rock
(45, 46,  4, 12, '2026-06-30 22:00:00'), -- Inglaterra vs Croacia — MetLife
(47, 48,  6, 12, '2026-06-30 22:00:00'); -- Ghana vs Panamá — AT&T

-- ─── Usuarios ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS usuarios (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(50)  NOT NULL UNIQUE,
    password_hash   CHAR(64)     NOT NULL,  -- SHA-256 hex
    nombre_completo VARCHAR(150) NOT NULL,
    tipo            ENUM('ADMINISTRADOR','TRADICIONAL','ESPORADICO') NOT NULL,
    activo          BOOLEAN DEFAULT TRUE,
    ultimo_acceso   DATETIME
) ENGINE=InnoDB;

-- Contraseña por defecto: "admin123" (SHA-256)
-- Hash de "admin123" = 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
INSERT IGNORE INTO usuarios (username, password_hash, nombre_completo, tipo, activo) VALUES
('admin',
 '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9',
 'Administrador del Sistema',
 'ADMINISTRADOR',
 TRUE);

-- ─── Bitácora ────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS bitacora (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id   INT NOT NULL,
    fecha_entrada DATETIME NOT NULL,
    fecha_salida  DATETIME,
    CONSTRAINT fk_bitacora_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
) ENGINE=InnoDB;

-- ─── Índices de rendimiento ──────────────────────────────────────────────────
DROP PROCEDURE IF EXISTS crear_indice;
DELIMITER $$
CREATE PROCEDURE crear_indice(IN tbl VARCHAR(64), IN idx VARCHAR(64), IN def TEXT)
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.statistics
        WHERE table_schema = DATABASE()
          AND table_name   = tbl
          AND index_name   = idx
    ) THEN
        SET @sql = CONCAT('CREATE INDEX ', idx, ' ON ', tbl, ' ', def);
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END$$
DELIMITER ;

CALL crear_indice('jugadores', 'idx_jugadores_equipo',  '(equipo_id)');
CALL crear_indice('jugadores', 'idx_jugadores_valor',   '(valor DESC)');
CALL crear_indice('partidos',  'idx_partidos_estadio',  '(estadio_id)');
CALL crear_indice('partidos',  'idx_partidos_grupo',    '(grupo_id)');
CALL crear_indice('bitacora',  'idx_bitacora_usuario',  '(usuario_id)');
CALL crear_indice('bitacora',  'idx_bitacora_entrada',  '(fecha_entrada)');

DROP PROCEDURE IF EXISTS crear_indice;
