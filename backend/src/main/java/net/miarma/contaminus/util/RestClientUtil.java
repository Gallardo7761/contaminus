package net.miarma.contaminus.util;

import java.util.Map;

import com.google.gson.Gson;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;

public class RestClientUtil {

    private final WebClient client;
    private final Gson gson;

    public RestClientUtil(WebClient client) {
        this.client = client;
        this.gson = new Gson();
    }

    public <T> Future<T> getRequest(int port, String host, String resource, Class<T> classType) {
        return client.getAbs(host + ":" + port + resource)
            .send()
            .map(response -> gson.fromJson(response.bodyAsString(), classType));
    }

    public <T> Future<T> getRequestWithParams(int port, String host, String resource, Class<T> classType,
                                              Map<String, String> params) {
        HttpRequest<Buffer> httpRequest = client.getAbs(host + ":" + port + "/" + resource);
        params.forEach(httpRequest::addQueryParam);

        return httpRequest.send()
            .map(response -> gson.fromJson(response.bodyAsString(), classType));
    }

    public <B, T> Future<T> postRequest(int port, String host, String resource, B body, Class<T> classType) {
        JsonObject jsonBody = new JsonObject(gson.toJson(body));
        return client.postAbs(host + ":" + port + "/" + resource)
            .sendJsonObject(jsonBody)
            .map(response -> gson.fromJson(response.bodyAsString(), classType));
    }

    public <B, T> Future<T> putRequest(int port, String host, String resource, B body, Class<T> classType) {
        JsonObject jsonBody = new JsonObject(gson.toJson(body));
        return client.putAbs(host + ":" + port + "/" + resource)
            .sendJsonObject(jsonBody)
            .map(response -> gson.fromJson(response.bodyAsString(), classType));
    }

    public Future<String> deleteRequest(int port, String host, String resource) {
        return client.deleteAbs(host + ":" + port + "/" + resource)
            .send()
            .map(response -> response.bodyAsString());
    }
}
