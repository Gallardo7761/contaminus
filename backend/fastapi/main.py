from fastapi import FastAPI
from contextlib import asynccontextmanager
from fastapi.middleware.cors import CORSMiddleware

from fastapi.responses import JSONResponse
from config import API_PREFIX
from fastapi.openapi.utils import get_openapi
from endpoints import sensors
from db import database

@asynccontextmanager
async def lifespan(app: FastAPI):
    await database.connect()  # Conecta la base de datos al iniciar
    yield
    await database.disconnect()  # Desconecta al finalizar

app = FastAPI(
    title="Mi API de Calidad del Aire",
    description="API para obtener mediciones de calidad del aire en Sevilla",
    version="1.0.0",
    docs_url=f"{API_PREFIX}/swagger",  # Cambia la URL de Swagger UI
    lifespan=lifespan,
    openapi_url="/dad/openapi.json"
)

app.add_middleware(CORSMiddleware, allow_origins=["*"], allow_methods=["*"], allow_headers=["*"])

@app.get(f"{API_PREFIX}/")
async def root():
    return {"message": "Has llegado a la raíz de la API"}

@app.get(f"{API_PREFIX}/openapi.json", include_in_schema=False)
async def get_openapi_json():
    return JSONResponse(content=get_openapi(title="Mi API de Calidad del Aire", version="1.0.0", routes=app.routes))

app.include_router(sensors.router, prefix=f"{API_PREFIX}/sensors", tags=["sensors"])