package com.mojang.minecraft;

import com.mojang.minecraft.level.Level;
import org.lwjgl.input.Keyboard;

public class Player
  extends Entity
{
  public Player(Level level)
  {
    super(level);
    heightOffset = 1.62F;
  }
  
  public void tick()
  {
    xo = x;
    yo = y;
    zo = z;
    float xa = 0.0F;
    float ya = 0.0F;
    
    if (Keyboard.isKeyDown(19))
    {
      resetPos();
    }
    if ((Keyboard.isKeyDown(200)) || (Keyboard.isKeyDown(17))) ya -= 1.0F;
    if ((Keyboard.isKeyDown(208)) || (Keyboard.isKeyDown(31))) ya += 1.0F;
    if ((Keyboard.isKeyDown(203)) || (Keyboard.isKeyDown(30))) xa -= 1.0F;
    if ((Keyboard.isKeyDown(205)) || (Keyboard.isKeyDown(32))) xa += 1.0F;
    if ((Keyboard.isKeyDown(57)) || (Keyboard.isKeyDown(219)))
    {
      if (onGround)
      {
        yd = 0.5F;
      }
    }
    
    moveRelative(xa, ya, onGround ? 0.1F : 0.02F);
    
    yd = ((float)(yd - 0.08D));
    move(xd, yd, zd);
    xd *= 0.91F;
    yd *= 0.98F;
    zd *= 0.91F;
    
    if (onGround)
    {
      xd *= 0.7F;
      zd *= 0.7F;
    }
  }
}
