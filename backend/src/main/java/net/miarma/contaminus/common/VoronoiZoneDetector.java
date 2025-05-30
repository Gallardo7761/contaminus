package net.miarma.contaminus.common;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.geojson.GeoJsonReader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VoronoiZoneDetector {

    private static class Zone {
        Polygon polygon;
        Integer groupId;

        public Zone(Polygon polygon, Integer groupId) {
            this.polygon = polygon;
            this.groupId = groupId;
        }
    }

    private static final List<Zone> zones = new ArrayList<>();
    private static final GeometryFactory geometryFactory = new GeometryFactory();
    private final Gson gson = new Gson();

    private VoronoiZoneDetector(String geojsonUrl, boolean isUrl) {
        String geojsonStr;

        try {
        	if(isUrl) {
            	try(InputStream is = URL.of(URI.create(geojsonUrl), null).openStream()) {
    				geojsonStr = new String(is.readAllBytes());
    			}
    		} else {
    			geojsonStr = Files.readString(new File(geojsonUrl).toPath());
            }
        } catch (Exception e) {
			Constants.LOGGER.error("⚠️ Error al cargar el GeoJSON: " + e.getMessage());
			throw new RuntimeException("Error al cargar el GeoJSON", e);
		}
        
        JsonObject root = JsonParser.parseString(geojsonStr).getAsJsonObject();
        JsonArray features = root.getAsJsonArray("features");
        GeoJsonReader reader = new GeoJsonReader(geometryFactory);

        for (int i = 0; i < features.size(); i++) {
            JsonObject feature = features.get(i).getAsJsonObject();

            Integer groupId = feature
                .getAsJsonObject("properties")
                .get("groupId")
                .getAsInt();

            JsonObject geometryJson = feature.getAsJsonObject("geometry");
            String geometryStr = gson.toJson(geometryJson);

            Geometry geometry = null;
			try {
				geometry = reader.read(geometryStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            if (geometry instanceof Polygon polygon) {
                zones.add(new Zone(polygon, groupId));
            } else {
                Constants.LOGGER.error("⚠️ Geometría ignorada: no es un polígono");
            }
        }
    }
    
    public static VoronoiZoneDetector create(String geojsonUrl, boolean isUrl) {
		return new VoronoiZoneDetector(geojsonUrl, isUrl);
	}

    public Integer getZoneForPoint(double lon, double lat) {
        Point p = geometryFactory.createPoint(new Coordinate(lon, lat));

        for (Zone z : zones) {
            if (z.polygon.covers(p)) {
                return z.groupId;
            }
        }

        return null; // no está dentro de ninguna zona
    }
    
    public static void main(String[] args) throws Exception {
        VoronoiZoneDetector detector = new VoronoiZoneDetector("https://miarma.net/files/voronoi_sevilla_geovoronoi.geojson", true);

        double lon = -5.9752;
        double lat = 37.3887;

        Integer actuatorId = detector.getZoneForPoint(lon, lat);
        if (actuatorId != null) {
            System.out.println("📍 El punto pertenece al actuator: " + actuatorId);
        } else {
            System.out.println("🚫 El punto no pertenece a ninguna zona");
        }
    }
}
