package com.mojang.minecraft;


public class Timer
{
  private static final long NS_PER_SECOND = 1000000000L;
  private static final long MAX_NS_PER_UPDATE = 1000000000L;
  private static final int MAX_TICKS_PER_UPDATE = 100;
  private float ticksPerSecond;
  private long lastTime;
  public int ticks;
  public float a;
  public float timeScale = 1.0F;
  public float fps = 0.0F;
  public float passedTime = 0.0F;
  
  public Timer(float ticksPerSecond)
  {
    this.ticksPerSecond = ticksPerSecond;
    lastTime = System.nanoTime();
  }
  
  public void advanceTime()
  {
    long now = System.nanoTime();
    long passedNs = now - lastTime;
    lastTime = now;
    
    if (passedNs < 0L) passedNs = 0L;
    if (passedNs > 1000000000L) { passedNs = 1000000000L;
    }
    fps = ((float)(1000000000L / passedNs));
    
    passedTime += (float)passedNs * timeScale * ticksPerSecond / 1.0E9F;
    
    ticks = ((int)passedTime);
    if (ticks > 100) ticks = 100;
    passedTime -= ticks;
    a = passedTime;
  }
}
