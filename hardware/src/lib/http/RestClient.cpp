#include "RestClient.hpp"

HTTPClient httpClient; // HTTP client object

void getRequest(const String url, String &response)
{
    httpClient.begin(url);
    int httpCode = httpClient.GET();
    if (httpCode > 0)
    {
        response = httpClient.getString();
    }
    else
    {
        response = "Error: " + String(httpCode);
    }
    httpClient.end();
}

void postRequest(const String url, String &payload, String &response)
{
    httpClient.begin(url);
    httpClient.addHeader("Content-Type", "application/json");
    int httpCode = httpClient.POST(payload);
    if (httpCode > 0)
    {
        response = httpClient.getString();
    }
    else
    {
        response = "Error: " + String(httpCode);
    }
    httpClient.end();
}