CREATE TABLE measures(
	measureId INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	deviceId INT NOT NULL,
	sensorType VARCHAR(64) NOT NULL,
	lat FLOAT NOT NULL,
	lon FLOAT NOT NULL,
	value FLOAT NOT NULL,
	timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
	FOREIGN KEY (deviceId) REFERENCES devices(deviceId)
);

CREATE TABLE devices(
	deviceId INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	deviceName VARCHAR(64) DEFAULT NULL
);

INSERT INTO devices (deviceName) VALUES
('ESP32_Aire_Centro'),
('ESP32_Temp_Centro'),
('ESP32_Humedad_Centro'),
('ESP32_Aire_Norte'),
('ESP32_Temp_Norte'),
('ESP32_Humedad_Norte');

INSERT INTO measures (deviceId, sensorType, lat, lon, value, timestamp) VALUES
-- Medidas del ESP32_Aire_Centro (MQ-135)
(1, 'MQ-135', 37.3886, -5.9823, 45.2, '2025-03-10 12:05:00'),
(1, 'MQ-135', 37.3886, -5.9823, 47.8, '2025-03-10 12:10:00'),
(1, 'MQ-135', 37.3886, -5.9823, 43.5, '2025-03-10 12:15:00'),

-- Medidas del ESP32_Temp_Centro (DHT11_Temperature)
(2, 'DHT11_Temperature', 37.3890, -5.9840, 22.5, '2025-03-10 12:05:00'),
(2, 'DHT11_Temperature', 37.3890, -5.9840, 23.1, '2025-03-10 12:15:00'),

-- Medidas del ESP32_Humedad_Centro (DHT11_Humidity)
(3, 'DHT11_Humidity', 37.3890, -5.9840, 60.3, '2025-03-10 12:05:00'),
(3, 'DHT11_Humidity', 37.3890, -5.9840, 58.7, '2025-03-10 12:15:00'),

-- Medidas del ESP32_Aire_Norte (MQ-135)
(4, 'MQ-135', 37.3915, -5.9857, 48.7, '2025-03-10 12:05:00'),
(4, 'MQ-135', 37.3915, -5.9857, 50.2, '2025-03-10 12:10:00'),
(4, 'MQ-135', 37.3915, -5.9857, 47.3, '2025-03-10 12:20:00'),

-- Medidas del ESP32_Temp_Norte (DHT11_Temperature)
(5, 'DHT11_Temperature', 37.3915, -5.9857, 23.1, '2025-03-10 12:05:00'),
(5, 'DHT11_Temperature', 37.3915, -5.9857, 22.9, '2025-03-10 12:15:00'),

-- Medidas del ESP32_Humedad_Norte (DHT11_Humidity)
(6, 'DHT11_Humidity', 37.3915, -5.9857, 58.2, '2025-03-10 12:05:00'),
(6, 'DHT11_Humidity', 37.3915, -5.9857, 57.6, '2025-03-10 12:15:00');


CREATE OR REPLACE VIEW v_DevicesMeasures AS
	SELECT * FROM devices NATURAL JOIN measures;
