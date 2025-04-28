#include "MAX7219.hpp"

MD_Parola display = MD_Parola(HARDWARE_TYPE, DATA_PIN, CLK_PIN, CS_PIN, MAX_DEVICES);

void MAX7219_Init()
{
    display.begin();
    display.setIntensity(15); // 0-15
    display.displayClear();
}

void MAX7219_DisplayText(const char *text, textPosition_t align, int speed, int pause)
{
    display.displayText(text, align, speed, pause, PA_SCROLL_LEFT, PA_SCROLL_LEFT);
}

void MAX7219_StartAnimation()
{
    display.displayAnimate();
}

void MAX7219_StopAnimation()
{
    display.displayReset();
}

void MAX7219_ClearDisplay()
{
    display.displayClear();
}

void MAX7219_SetBrightness(uint8_t brightness)
{
    display.setIntensity(brightness);
}