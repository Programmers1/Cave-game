package com.mojang.minecraft.particle;

import com.mojang.minecraft.level.Tesselator;

public class Particle extends com.mojang.minecraft.Entity {
  private float xd;
  private float yd;
  private float zd;
  public int tex;
  private float uo;
  private float vo;
  private int age = 0;
  private int lifetime = 0;
  private float size;
  
  public Particle(com.mojang.minecraft.level.Level level, float x, float y, float z, float xa, float ya, float za, int tex)
  {
    super(level);
    this.tex = tex;
    setSize(0.2F, 0.2F);
    heightOffset = (bbHeight / 2.0F);
    setPos(x, y, z);
    
    xd = (xa + (float)(Math.random() * 2.0D - 1.0D) * 0.4F);
    yd = (ya + (float)(Math.random() * 2.0D - 1.0D) * 0.4F);
    zd = (za + (float)(Math.random() * 2.0D - 1.0D) * 0.4F);
    float speed = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
    
    float dd = (float)Math.sqrt(xd * xd + yd * yd + zd * zd);
    xd = (xd / dd * speed * 0.4F);
    yd = (yd / dd * speed * 0.4F + 0.1F);
    zd = (zd / dd * speed * 0.4F);
    

    uo = ((float)Math.random() * 3.0F);
    vo = ((float)Math.random() * 3.0F);
    
    size = ((float)(Math.random() * 0.5D + 0.5D));
    
    lifetime = ((int)(4.0D / (Math.random() * 0.9D + 0.1D)));
    age = 0;
  }
  
  public void tick()
  {
    xo = x;
    yo = y;
    zo = z;
    
    if (age++ >= lifetime) { remove();
    }
    yd = ((float)(yd - 0.04D));
    move(xd, yd, zd);
    xd *= 0.98F;
    yd *= 0.98F;
    zd *= 0.98F;
    
    if (onGround)
    {
      xd *= 0.7F;
      zd *= 0.7F;
    }
  }
  
  public void render(Tesselator t, float a, float xa, float ya, float za, float xa2, float za2)
  {
    float u0 = (tex % 16 + uo / 4.0F) / 16.0F;
    float u1 = u0 + 0.015609375F;
    float v0 = (tex / 16 + vo / 4.0F) / 16.0F;
    float v1 = v0 + 0.015609375F;
    float r = 0.1F * size;
    
    float x = xo + (this.x - xo) * a;
    float y = yo + (this.y - yo) * a;
    float z = zo + (this.z - zo) * a;
    t.vertexUV(x - xa * r - xa2 * r, y - ya * r, z - za * r - za2 * r, u0, v1);
    t.vertexUV(x - xa * r + xa2 * r, y + ya * r, z - za * r + za2 * r, u0, v0);
    t.vertexUV(x + xa * r + xa2 * r, y + ya * r, z + za * r + za2 * r, u1, v0);
    t.vertexUV(x + xa * r - xa2 * r, y - ya * r, z + za * r - za2 * r, u1, v1);
  }
}
