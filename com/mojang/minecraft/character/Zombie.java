package com.mojang.minecraft.character;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.Textures;
import com.mojang.minecraft.level.Level;
import org.lwjgl.opengl.GL11;

public class Zombie extends Entity
{
  public float rot;
  public float timeOffs;
  public float speed;
  public float rotA;
  private static ZombieModel zombieModel = new ZombieModel();
  
  public Zombie(Level level, float x, float y, float z)
  {
    super(level);
    rotA = ((float)(Math.random() + 1.0D) * 0.01F);
    setPos(x, y, z);
    timeOffs = ((float)Math.random() * 1239813.0F);
    rot = ((float)(Math.random() * 3.141592653589793D * 2.0D));
    speed = 1.0F;
  }
  
  public void tick()
  {
    xo = x;
    yo = y;
    zo = z;
    float xa = 0.0F;
    float ya = 0.0F;
    
    if (y < -100.0F) { remove();
    }
    rot += rotA;
    rotA = ((float)(rotA * 0.99D));
    rotA = ((float)(rotA + (Math.random() - Math.random()) * Math.random() * Math.random() * 0.07999999821186066D));
    xa = (float)Math.sin(rot);
    ya = (float)Math.cos(rot);
    
    if ((onGround) && (Math.random() < 0.08D))
    {
      yd = 0.5F;
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
  
  public void render(float a)
  {
    GL11.glEnable(3553);
    GL11.glBindTexture(3553, Textures.loadTexture("/char.png", 9728));
    
    GL11.glPushMatrix();
    double time = System.nanoTime() / 1.0E9D * 10.0D * speed + timeOffs;
    
    float size = 0.058333334F;
    float yy = (float)(-Math.abs(Math.sin(time * 0.6662D)) * 5.0D - 23.0D);
    GL11.glTranslatef(xo + (x - xo) * a, yo + (y - yo) * a, zo + (z - zo) * a);
    GL11.glScalef(1.0F, -1.0F, 1.0F);
    GL11.glScalef(size, size, size);
    GL11.glTranslatef(0.0F, yy, 0.0F);
    float c = 57.29578F;
    GL11.glRotatef(rot * c + 180.0F, 0.0F, 1.0F, 0.0F);
    
    zombieModel.render((float)time);
    GL11.glPopMatrix();
    GL11.glDisable(3553);
  }
}
