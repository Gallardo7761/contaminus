import geopandas as gpd # esto es necesario para leer el GeoJSON
import matplotlib.pyplot as plt # esto es necesario para graficar
import numpy as np # esto es necesario para manejar los arrays
import json # esto es necesario para guardar el GeoJSON
from shapely.geometry import Polygon # esto es necesario para crear la caja de recorte
from geovoronoi import voronoi_regions_from_coords # esto es necesario para crear el Voronoi

# se cargan los puntos (coordenadas) de los actuadores
# en formato GeoJSON
points_gdf = gpd.read_file("sevilla.geojson")
coords = np.array([[geom.x, geom.y] for geom in points_gdf.geometry])

# esto es una "caja" alrededor de Sevilla para recortar el Voronoi
# para que las regiones no acotadas no sean infinitas
seville_boundary = Polygon([
    (-6.10, 37.30),
    (-5.85, 37.30),
    (-5.85, 37.45),
    (-6.10, 37.45)
])
area_gdf = gpd.GeoDataFrame(geometry=[seville_boundary], crs="EPSG:4326")

# se genera el Voronoi con las coordenadas de los actuadores
# y la caja de recorte (unión de todo)
region_polys, region_pts = voronoi_regions_from_coords(coords, area_gdf.union_all())

# dibuja con matplotlib
fig, ax = plt.subplots(figsize=(10, 10))

for poly in region_polys.values():
    x, y = poly.exterior.xy
    ax.fill(x, y, alpha=0.3, edgecolor='black')

ax.plot(coords[:, 0], coords[:, 1], 'ro')
for idx, coord in enumerate(coords):
    ax.text(coord[0], coord[1], points_gdf.iloc[idx]["name"], fontsize=8, ha='center')

ax.set_title("Zonas Voronoi por Actuator (GeoVoronoi)")
plt.axis("equal")
plt.grid(True)
plt.tight_layout()
plt.show()

# Guardar GeoJSON
features = []
for idx, region in region_polys.items():
    point_index = region_pts[idx]  # índice del punto original
    name = points_gdf.loc[point_index, "name"]
    if isinstance(name, gpd.GeoSeries):
        name = name.iloc[0]  # o el que tú quieras
    name = str(name)

    feature = {
        "type": "Feature",
        "properties": {
            "actuatorId": name
        },
        "geometry": json.loads(gpd.GeoSeries([region]).to_json())["features"][0]["geometry"]
    }
    features.append(feature)

geojson_output = {
    "type": "FeatureCollection",
    "features": features
}

with open("voronoi_sevilla_geovoronoi.geojson", "w") as f:
    json.dump(geojson_output, f, indent=2)

print("✅ GeoJSON guardado como 'voronoi_sevilla_geovoronoi.geojson'")
