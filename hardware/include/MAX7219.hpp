#pragma once

#include <MD_Parola.h>
#include <MD_MAX72xx.h>
#include <SPI.h>

#define HARDWARE_TYPE MD_MAX72XX::FC16_HW
#define MAX_DEVICES 4 // 4 modulos 8x8
#define DATA_PIN 19
#define CS_PIN 18
#define CLK_PIN 17

struct MAX7219Status_t
{
    String status;
    String actuatorStatus;
};

void MAX7219_Init();
void MAX7219_DisplayText(const char *text, textPosition_t align, uint16_t speed, uint16_t pause);
bool MAX7219_Animate();
void MAX7219_ResetAnimation();
void MAX7219_ClearDisplay();
void MAX7219_SetBrightness(uint8_t brightness);