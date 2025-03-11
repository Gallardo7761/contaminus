# ⚠ REQUISITOS PREVIOS
- Descargar [MariaDB 10.6.21](https://mariadb.org/download/?t=mariadb&p=mariadb&r=10.6.21&os=windows&cpu=x86_64&pkg=msi&mirror=raiolanetworks)
- Descargar [DBeaver](https://dbeaver.io/download/)
- Descargar Node.js
    - Para descargar Node recomiendo fnm (Fast Node Manager) que lo podéis descargar [aquí](https://miarma.net/descargas)
    - Para instalarlo: `fnm install 23.7.0`

# Para lanzar la API en local 
1. `pip install -r requirements.txt`
2. `uvicorn main:app --reload --port <puerto que quieras>`

# Para lanzar la web en modo desarrollador
1. `npm install`
2. `npm run dev`

# Para transpilar la web de React a HTML/CSS/JS Vanilla
`npm run build` <br>
La encontraréis en `dist/`

# Script SQL
```sql
USE dad;

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
	status BLOB NOT NULL,
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

-- Grupo único
INSERT INTO groups (groupName) VALUES
('ContaminUS');

-- Dispositivos dentro del grupo 1
INSERT INTO devices (groupId, deviceName) VALUES
(1, 'Raspberry Pi Sensor Hub'),
(1, 'ESP32 Outdoor Monitor'),
(1, 'Arduino Air Quality Station');

-- Sensores asignados a los dispositivos
INSERT INTO sensors (deviceId, sensorType, unit, status) VALUES
(1, 'GPS', '', 1),
(1, 'AirQuality', 'ppm', 1),
(2, 'Temperature', '°C', 1),
(2, 'Humidity', '%', 1),
(3, 'CO Sensor', 'ppm', 1);

-- Actuadores asignados a los dispositivos
INSERT INTO actuators (deviceId, status) VALUES
(1, 0x01),  -- Actuador encendido
(2, 0x00),  -- Actuador apagado
(3, 0x01);  -- Actuador encendido

-- Datos de GPS (sensores tipo GPS)
INSERT INTO gps_values (sensorId, lat, lon) VALUES
(1, 37.3886, -5.9823),  -- Centro de Sevilla
(1, 37.4010, -5.9980),  -- Isla de la Cartuja
(1, 37.3431, -5.9812);  -- Bellavista

-- Datos de calidad del aire (sensores de aire)
INSERT INTO air_values (sensorId, temperature, humidity, carbonMonoxide) VALUES
(2, 28.5, 60.2, 0.4),
(2, 30.1, 55.8, 0.6),
(2, 27.3, 65.4, 0.3),
(3, 25.8, 50.7, 0.2),
(3, 29.2, 58.9, 0.5);
```
