
from pydantic import BaseModel
from datetime import datetime
from typing import Optional

class SensorModel(BaseModel):
    id: Optional[int] # pq el modelo se usará también para INSERT
    sensor_type: str
    lat: float  
    lon: float
    value: float
    timestamp: Optional[datetime] = None