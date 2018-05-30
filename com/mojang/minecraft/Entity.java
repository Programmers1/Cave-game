package com.mojang.minecraft;

import com.mojang.minecraft.phys.AABB;

public class Entity { protected com.mojang.minecraft.level.Level level;
  public float xo;
  public float yo;
  public float zo;
  public float x;
  public float y;
  public float z;
  public float xd;
  public float yd;
  public float zd;
  public float yRot;
  public float xRot; public AABB bb; public boolean onGround = false;
  
  public boolean removed = false;
  protected float heightOffset = 0.0F;
  
  protected float bbWidth = 0.6F;
  protected float bbHeight = 1.8F;
  
  public Entity(com.mojang.minecraft.level.Level level)
  {
    this.level = level;
    resetPos();
  }
  
  protected void resetPos()
  {
    float x = (float)Math.random() * level.width;
    float y = level.depth + 10;
    float z = (float)Math.random() * level.height;
    setPos(x, y, z);
  }
  
  public void remove()
  {
    removed = true;
  }
  
  protected void setSize(float w, float h)
  {
    bbWidth = w;
    bbHeight = h;
  }
  
  protected void setPos(float x, float y, float z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    float w = bbWidth / 2.0F;
    float h = bbHeight / 2.0F;
    bb = new AABB(x - w, y - h, z - w, x + w, y + h, z + w);
  }
  
  public void turn(float xo, float yo)
  {
    yRot = ((float)(yRot + xo * 0.15D));
    xRot = ((float)(xRot - yo * 0.15D));
    if (xRot < -90.0F) xRot = -90.0F;
    if (xRot > 90.0F) xRot = 90.0F;
  }
  
  public void tick()
  {
    xo = x;
    yo = y;
    zo = z;
  }
  
  public void move(float xa, float ya, float za)
  {
    float xaOrg = xa;
    float yaOrg = ya;
    float zaOrg = za;
    
    java.util.List<AABB> aABBs = level.getCubes(bb.expand(xa, ya, za));
    
    for (int i = 0; i < aABBs.size(); i++)
      ya = ((AABB)aABBs.get(i)).clipYCollide(bb, ya);
    bb.move(0.0F, ya, 0.0F);
    
    for (int i = 0; i < aABBs.size(); i++)
      xa = ((AABB)aABBs.get(i)).clipXCollide(bb, xa);
    bb.move(xa, 0.0F, 0.0F);
    
    for (int i = 0; i < aABBs.size(); i++)
      za = ((AABB)aABBs.get(i)).clipZCollide(bb, za);
    bb.move(0.0F, 0.0F, za);
    
    onGround = ((yaOrg != ya) && (yaOrg < 0.0F));
    
    if (xaOrg != xa) xd = 0.0F;
    if (yaOrg != ya) yd = 0.0F;
    if (zaOrg != za) { zd = 0.0F;
    }
    x = ((bb.x0 + bb.x1) / 2.0F);
    y = (bb.y0 + heightOffset);
    z = ((bb.z0 + bb.z1) / 2.0F);
  }
  
  public void moveRelative(float xa, float za, float speed)
  {
    float dist = xa * xa + za * za;
    if (dist < 0.01F) { return;
    }
    dist = speed / (float)Math.sqrt(dist);
    xa *= dist;
    za *= dist;
    

    float sin = (float)Math.sin(yRot * 3.141592653589793D / 180.0D);
    float cos = (float)Math.cos(yRot * 3.141592653589793D / 180.0D);
    
    xd += xa * cos - za * sin;
    zd += za * cos + xa * sin;
  }
  
  public boolean isLit()
  {
    int xTile = (int)x;
    int yTile = (int)y;
    int zTile = (int)z;
    return level.isLit(xTile, yTile, zTile);
  }
}
