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

DROP TABLE IF EXISTS gps_values;
DROP TABLE IF EXISTS air_values;
DROP TABLE IF EXISTS actuators;
DROP TABLE IF EXISTS sensors;
DROP TABLE IF EXISTS devices;
DROP TABLE IF EXISTS groups;

CREATE TABLE IF NOT EXISTS groups(
	groupId INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	groupName VARCHAR(64) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS devices(
	deviceId INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	groupId INT NOT NULL,
	deviceName VARCHAR(64) DEFAULT NULL,
	FOREIGN KEY (groupId) REFERENCES groups(groupId)
);

CREATE TABLE IF NOT EXISTS sensors(
	sensorId INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	deviceId INT NOT NULL,
	sensorType VARCHAR(64) NOT NULL,
	unit VARCHAR(8) NOT NULL,
	status INT NOT NULL,
	timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
	FOREIGN KEY (deviceId) REFERENCES devices(deviceId)
);

CREATE TABLE IF NOT EXISTS actuators (
	actuatorId INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	deviceId INT NOT NULL,
	status INT NOT NULL,
	timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
	FOREIGN KEY (deviceId) REFERENCES devices(deviceId)
);

CREATE TABLE IF NOT EXISTS gps_values(
	valueId INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	sensorId INT NOT NULL,
	lat FLOAT NOT NULL,
	lon FLOAT NOT NULL,
	timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
	FOREIGN KEY (sensorId) REFERENCES sensors(sensorId)
);

CREATE TABLE IF NOT EXISTS air_values (
	valueId INT PRIMARY KEY AUTO_INCREMENT NOT NULL ,
	sensorId INT NOT NULL,
	temperature FLOAT NOT NULL,
	humidity FLOAT NOT NULL,
	carbonMonoxide FLOAT NOT NULL,
	timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
	FOREIGN KEY (sensorId) REFERENCES sensors(sensorId)
);

CREATE OR REPLACE VIEW v_sensor_values AS
SELECT 
    s.sensorId,
    s.deviceId,
    s.sensorType,
    s.unit,
    s.status AS sensorStatus,
    s.timestamp AS sensorTimestamp,
    av.temperature,
    av.humidity,
    av.carbonMonoxide,
    av.timestamp AS airValuesTimestamp,
    gv.lat,
    gv.lon,
    gv.timestamp AS gpsTimestamp
FROM sensors s
LEFT JOIN air_values av ON s.sensorId = av.sensorId
LEFT JOIN gps_values gv ON s.sensorId = gv.sensorId;

CREATE OR REPLACE VIEW v_latest_values AS
SELECT 
    s.deviceId,
    s.sensorId,
    s.sensorType,
    s.unit,
    s.status AS sensorStatus,
    s.timestamp AS sensorTimestamp,
    av.temperature,
    av.humidity,
    av.carbonMonoxide,
    av.timestamp AS airValuesTimestamp
FROM sensors s
LEFT JOIN air_values av 
    ON s.sensorId = av.sensorId 
    AND av.timestamp = (
        SELECT MAX(timestamp) 
        FROM air_values 
        WHERE sensorId = s.sensorId
    );

INSERT INTO groups (groupName) VALUES ('Grupo 1');

INSERT INTO devices (groupId, deviceName) VALUES
(1, 'Dispositivo 1'),
(1, 'Dispositivo 2'),
(1, 'Dispositivo 3');

-- Sensores para el Dispositivo 1
INSERT INTO sensors (deviceId, sensorType, unit, status) VALUES
(1, 'GPS', 'N/A', 1),
(1, 'Temperature & Humidity', '°C/%', 1),
(1, 'CO', 'ppm', 1);

-- Sensores para el Dispositivo 2
INSERT INTO sensors (deviceId, sensorType, unit, status) VALUES
(2, 'GPS', 'N/A', 1),
(2, 'Temperature & Humidity', '°C/%', 1),
(2, 'CO', 'ppm', 1);

-- Sensores para el Dispositivo 3
INSERT INTO sensors (deviceId, sensorType, unit, status) VALUES
(3, 'GPS', 'N/A', 1),
(3, 'Temperature & Humidity', '°C/%', 1),
(3, 'CO', 'ppm', 1);

-- Valores de GPS para el Dispositivo 1
INSERT INTO gps_values (sensorId, lat, lon) VALUES
(1, 37.3861, -5.9921),
(2, 37.3870, -5.9900),
(3, 37.3885, -5.9930);

-- Valores de GPS para el Dispositivo 2
INSERT INTO gps_values (sensorId, lat, lon) VALUES
(4, 37.3850, -5.9910),
(5, 37.3865, -5.9935),
(6, 37.3870, -5.9950);

-- Valores de GPS para el Dispositivo 3
INSERT INTO gps_values (sensorId, lat, lon) VALUES
(7, 37.3860, -5.9920),
(8, 37.3875, -5.9915),
(9, 37.3890, -5.9905);

-- Valores de Temperatura, Humedad y CO para el Dispositivo 1
INSERT INTO air_values (sensorId, temperature, humidity, carbonMonoxide) VALUES
(1, 22.5, 45.0, 0.02),
(2, 23.0, 46.5, 0.01),
(3, 21.8, 48.0, 0.03);

-- Valores de Temperatura, Humedad y CO para el Dispositivo 2
INSERT INTO air_values (sensorId, temperature, humidity, carbonMonoxide) VALUES
(4, 24.5, 50.0, 0.04),
(5, 25.0, 49.5, 0.05),
(6, 23.5, 47.0, 0.02);

-- Valores de Temperatura, Humedad y CO para el Dispositivo 3
INSERT INTO air_values (sensorId, temperature, humidity, carbonMonoxide) VALUES
(7, 21.0, 44.0, 0.01),
(8, 22.0, 45.5, 0.03),
(9, 21.5, 46.0, 0.02);

```
