#pragma once

#include <HTTPClient.h>

void getRequest(const String url, String &response);
void postRequest(const String url, const String &payload, String &response);