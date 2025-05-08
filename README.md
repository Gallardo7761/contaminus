# ⚠ REQUISITOS PREVIOS (SÓLO DAD)
- Descargar [MariaDB 10.6.21](https://mariadb.org/download/?t=mariadb&p=mariadb&r=10.6.21&os=windows&cpu=x86_64&pkg=msi&mirror=raiolanetworks)
- Descargar [DBeaver](https://dbeaver.io/download/)
- Descargar [Postman](https://www.postman.com/downloads/)

## Para lanzar la API con Vert.X 
1. Run -> Run Configurations -> Java Application
2. Como clase principal: `io.vertx.core.Launcher`
3. Como argumentos: `net.miarma.contaminus.server.MainVerticle`

# ⚠ REQUISITOS PREVIOS (DAD + Hack4Change)
- Descargar Node.js
    - Para descargar Node recomiendo fnm (Fast Node Manager) que lo podéis descargar [aquí](https://miarma.net/descargas)
    - Para instalarlo: `fnm install 23.7.0`
    -   
## Para lanzar la web en modo desarrollador
1. Abrir terminal (normalmente en VSCode) en la carpeta ContaminUS/frontend
2. `npm install`
3. `npm run dev`

## Para transpilar la web de React a HTML/CSS/JS Vanilla
`npm run build` <br>
La encontraréis en `dist/`

# ⚙️ Script para crear la BD
```sql
USE dad;

DROP TABLE IF EXISTS co_values;
DROP TABLE IF EXISTS weather_values;
DROP TABLE IF EXISTS gps_values;
DROP TABLE IF EXISTS actuators;
DROP TABLE IF EXISTS sensors;
DROP TABLE IF EXISTS devices;
DROP TABLE IF EXISTS groups;

CREATE TABLE IF NOT EXISTS groups(
	groupId INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	groupName VARCHAR(64) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS devices(
	deviceId CHAR(6) PRIMARY KEY NOT NULL,
	groupId INT NOT NULL,
	deviceName VARCHAR(64) DEFAULT NULL,
	FOREIGN KEY (groupId) REFERENCES groups(groupId) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sensors(
	sensorId INT NOT NULL,
	deviceId CHAR(6) NOT NULL,
	sensorType VARCHAR(64) NOT NULL,
	unit VARCHAR(8) NOT NULL,
	status INT NOT NULL,
	timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
	PRIMARY KEY (deviceId, sensorId),
	FOREIGN KEY (deviceId) REFERENCES devices(deviceId) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS actuators (
	actuatorId INT NOT NULL,
	deviceId CHAR(6) NOT NULL,
	status INT NOT NULL,
	timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
	PRIMARY KEY (deviceId, actuatorId),
	FOREIGN KEY (deviceId) REFERENCES devices(deviceId) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS gps_values(
	valueId INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	deviceId CHAR(6) NOT NULL,
	sensorId INT NOT NULL,
	lat FLOAT NOT NULL,
	lon FLOAT NOT NULL,
	timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
	FOREIGN KEY (deviceId, sensorId) REFERENCES sensors(deviceId, sensorId) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS weather_values (
	valueId INT PRIMARY KEY AUTO_INCREMENT NOT NULL ,
	deviceId CHAR(6) NOT NULL,
	sensorId INT NOT NULL,
	temperature FLOAT NOT NULL,
	humidity FLOAT NOT NULL,
	pressure FLOAT NOT NULL,
	timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
	FOREIGN KEY (deviceId, sensorId) REFERENCES sensors(deviceId, sensorId) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS co_values (
	valueId INT PRIMARY KEY AUTO_INCREMENT NOT NULL ,
	deviceId CHAR(6) NOT NULL,
	sensorId INT NOT NULL,
	value FLOAT NOT NULL,
	timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
	FOREIGN KEY (deviceId, sensorId) REFERENCES sensors(deviceId, sensorId) ON DELETE CASCADE
);

CREATE OR REPLACE VIEW v_sensor_values AS
SELECT 
    s.sensorId,
    s.deviceId,
    s.sensorType,
    s.unit,
    s.status AS sensorStatus,
    wv.temperature,
    wv.humidity,
    wv.pressure,
    cv.value AS carbonMonoxide,
    gv.lat,
    gv.lon,
    COALESCE(gv.timestamp, wv.timestamp, cv.timestamp) AS timestamp -- el primero no nulo
FROM sensors s
LEFT JOIN weather_values wv ON s.deviceId = wv.deviceId AND s.sensorId = wv.sensorId
LEFT JOIN co_values cv ON s.deviceId = cv.deviceId AND s.sensorId = cv.sensorId
LEFT JOIN gps_values gv ON s.deviceId = gv.deviceId AND s.sensorId = gv.sensorId;


CREATE OR REPLACE VIEW v_latest_values AS
SELECT 
    s.deviceId,
    s.sensorId,
    s.sensorType,
    s.unit,
    s.status AS sensorStatus,
    s.timestamp AS sensorTimestamp,
    wv.temperature,
    wv.humidity,
    wv.pressure,
    cv.value AS carbonMonoxide,
    gv.lat,
    gv.lon,
    COALESCE(gv.timestamp, wv.timestamp, cv.timestamp) AS airValuesTimestamp -- el primero no nulo
FROM sensors s
LEFT JOIN weather_values wv ON s.deviceId = wv.deviceId AND s.sensorId = wv.sensorId
LEFT JOIN co_values cv ON s.deviceId = cv.deviceId AND s.sensorId = cv.sensorId
LEFT JOIN gps_values gv ON s.deviceId = gv.deviceId AND s.sensorId = gv.sensorId
WHERE (wv.timestamp = (SELECT MAX(timestamp) FROM weather_values WHERE sensorId = s.sensorId)
       OR cv.timestamp = (SELECT MAX(timestamp) FROM co_values WHERE sensorId = s.sensorId)
		OR gv.timestamp = (SELECT MAX(timestamp) FROM gps_values WHERE sensorId = s.sensorId));


-- VISTAS AUXILIARES
CREATE OR REPLACE VIEW v_co_by_device AS
SELECT 
    s.deviceId,
    c.value AS carbonMonoxide,
    c.timestamp
FROM sensors s
JOIN co_values c ON s.deviceId = c.deviceId AND s.sensorId = c.sensorId
WHERE s.sensorType = 'CO';

CREATE OR REPLACE VIEW v_gps_by_device AS
SELECT 
    s.deviceId,
    g.lat,
    g.lon,
    g.timestamp
FROM sensors s
JOIN gps_values g ON s.deviceId = g.deviceId AND s.sensorId = g.sensorId
WHERE s.sensorType = 'GPS';

CREATE OR REPLACE VIEW v_weather_by_device AS
SELECT 
    s.deviceId,
    w.temperature AS temperature,
    w.humidity AS humidity,
    w.pressure AS pressure,
    w.timestamp
FROM sensors s
JOIN weather_values w ON s.deviceId = w.deviceId AND s.sensorId = w.sensorId
WHERE s.sensorType = 'Temperature & Humidity';
-- VISTAS AUXILIARES

CREATE OR REPLACE VIEW v_pollution_map AS
SELECT 
    d.deviceId,
    d.deviceName,
    g.lat,
    g.lon,
    c.carbonMonoxide,
    c.timestamp
FROM devices d
LEFT JOIN v_co_by_device c ON d.deviceId = c.deviceId
LEFT JOIN v_gps_by_device g ON d.deviceId = g.deviceId 
    AND (g.timestamp <= c.timestamp OR g.timestamp IS NULL)
WHERE c.carbonMonoxide IS NOT NULL
ORDER BY d.deviceId, c.timestamp;

CREATE OR REPLACE VIEW v_sensor_history_by_device AS
SELECT 
    d.deviceId,
    d.deviceName,
    w.temperature AS value,
    'temperature' AS valueType,
    w.timestamp
FROM devices d
JOIN v_weather_by_device w ON d.deviceId = w.deviceId
UNION ALL
SELECT 
    d.deviceId,
    d.deviceName,
    w.humidity AS value,
    'humidity' AS valueType,
    w.timestamp
FROM devices d
JOIN v_weather_by_device w ON d.deviceId = w.deviceId
UNION ALL
SELECT 
    d.deviceId,
    d.deviceName,
    c.carbonMonoxide AS value,
    'carbonMonoxide' AS valueType,
    c.timestamp
FROM devices d
JOIN v_co_by_device c ON d.deviceId = c.deviceId
ORDER BY deviceId, timestamp;

-- Insertar grupos
INSERT INTO groups (groupName) VALUES ('Grupo 1');

-- Insertar dispositivos
INSERT INTO devices (deviceId, groupId, deviceName) VALUES
('6A6098', 1, 'Dispositivo 1');

-- Sensores para el Dispositivo 6A6098
INSERT INTO sensors (sensorId, deviceId, sensorType, unit, status) VALUES
(1, '6A6098', 'GPS', 'N/A', 1),
(2, '6A6098', 'Temperature & Humidity', '°C/%', 1),
(3, '6A6098', 'CO', 'ppm', 1);

-- ACtuadores para el Dispositivo 6A6098
INSERT INTO actuators (actuatorId, deviceId, status, timestamp) VALUES
(1, '6A6098', 1, CURRENT_TIMESTAMP());
```
