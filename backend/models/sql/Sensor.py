from sqlalchemy import Column, Integer, String, Float, TIMESTAMP, Table, MetaData, func

metadata = MetaData()

sensor_mq_data = Table(
    "sensor_mq_data",
    metadata,
    Column("id", Integer, primary_key=True, autoincrement=True),
    Column("sensor_type", String(50), nullable=False),  # Tipo de sensor (MQ-135, etc.)
    Column("lat", Float, nullable=False),  # Latitud
    Column("lon", Float, nullable=False),  # Longitud
    Column("value", Float, nullable=False),  # Valor leído del sensor
    Column("timestamp", TIMESTAMP, server_default=func.current_timestamp())  # Fecha automática
)