from fastapi import APIRouter, Body, HTTPException, Query
from fastapi import APIRouter, Body, HTTPException, Query
from typing import Optional
from pydantic import BaseModel
from sqlalchemy import select, insert
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from models.sql.Sensor import sensor_mq_data
from db import database
from models.schemas.SensorModel import SensorModel

router = APIRouter()

@router.get("")
async def get_all(
     _sort: Optional[str] = Query(None, alias="_sort", description="Campo por el cual ordenar los resultados"),
    _order: Optional[str] = Query("asc", description="Orden de los resultados, 'asc' o 'desc'"),
    _limit: Optional[int] = Query(100, ge=1, description="Número máximo de resultados a mostrar"),
):
    query = select(sensor_mq_data)

    if _sort:
        if _order == "desc":
            query = query.order_by(getattr(sensor_mq_data.c, _sort).desc())
        else:
            query = query.order_by(getattr(sensor_mq_data.c, _sort))

    if _limit:
        query = query.limit(_limit)

    async with database.transaction():
        result = await database.fetch_all(query)
    return result

@router.get("/sensor")
async def get_by_params(
    id: Optional[int] = Query(None, alias="id", description="ID del sensor a buscar"),
    sensor_type: Optional[str] = Query(None, alias="sensor_type", description="Tipo de sensor"),
    min_value: Optional[float] = Query(None, alias="min_value", description="Valor mínimo del sensor"),
    max_value: Optional[float] = Query(None, alias="max_value", description="Valor máximo del sensor"),
    min_temp: Optional[float] = Query(None, alias="min_temp", description="Temperatura mínima"),
    max_temp: Optional[float] = Query(None, alias="max_temp", description="Temperatura máxima"),
    min_humidity: Optional[float] = Query(None, alias="min_humidity", description="Humedad mínima"),
    max_humidity: Optional[float] = Query(None, alias="max_humidity", description="Humedad máxima"),
):
    query = select(sensor_mq_data)

    if id is not None:
        query = query.where(sensor_mq_data.c.id == id)
    if sensor_type is not None:
        query = query.where(sensor_mq_data.c.sensor_type == sensor_type)
    if min_value is not None:
        query = query.where(sensor_mq_data.c.value >= min_value)
    if max_value is not None:
        query = query.where(sensor_mq_data.c.value <= max_value)
    if min_temp is not None:
        query = query.where(sensor_mq_data.c.temperature >= min_temp)
    if max_temp is not None:
        query = query.where(sensor_mq_data.c.temperature <= max_temp)
    if min_humidity is not None:
        query = query.where(sensor_mq_data.c.humidity >= min_humidity)
    if max_humidity is not None:
        query = query.where(sensor_mq_data.c.humidity <= max_humidity)

    async with database.transaction():
        result = await database.fetch_all(query)
    return result