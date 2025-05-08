#pragma once

#include <HTTPClient.h>

void getRequest(HTTPClient &httpClient, const String url, String &response);
void postRequest(HTTPClient &httpClient, const String url, const String &payload, String &response);