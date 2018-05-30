package com.mojang.minecraft.level;

import com.mojang.minecraft.Player;

public class Chunk { public com.mojang.minecraft.phys.AABB aabb;
  public final Level level;
  public final int x0;
  public final int y0;
  public final int z0;
  public final int x1;
  public final int y1;
  public final int z1;
  public final float x;
  public final float y;
  public final float z; private boolean dirty = true;
  private int lists = -1;
  public long dirtiedTime = 0L;
  
  private static Tesselator t = Tesselator.instance;
  
  public static int updates = 0;
  
  public Chunk(Level level, int x0, int y0, int z0, int x1, int y1, int z1)
  {
    this.level = level;
    this.x0 = x0;
    this.y0 = y0;
    this.z0 = z0;
    this.x1 = x1;
    this.y1 = y1;
    this.z1 = z1;
    
    x = ((x0 + x1) / 2.0F);
    y = ((y0 + y1) / 2.0F);
    z = ((z0 + z1) / 2.0F);
    
    aabb = new com.mojang.minecraft.phys.AABB(x0, y0, z0, x1, y1, z1);
    lists = org.lwjgl.opengl.GL11.glGenLists(2);
  }
  

  private static long totalTime = 0L;
  private static int totalUpdates = 0;
  
  private void rebuild(int layer)
  {
    dirty = false;
    
    updates += 1;
    
    long before = System.nanoTime();
    org.lwjgl.opengl.GL11.glNewList(lists + layer, 4864);
    t.init();
    int tiles = 0;
    for (int x = x0; x < x1; x++)
      for (int y = y0; y < y1; y++)
        for (int z = z0; z < z1; z++)
        {
          int tileId = level.getTile(x, y, z);
          if (tileId > 0)
          {
            com.mojang.minecraft.level.tile.Tile.tiles[tileId].render(t, level, layer, x, y, z);
            tiles++;
          }
        }
    t.flush();
    org.lwjgl.opengl.GL11.glEndList();
    long after = System.nanoTime();
    if (tiles > 0)
    {
      totalTime += after - before;
      totalUpdates += 1;
    }
  }
  




  public void rebuild()
  {
    rebuild(0);
    rebuild(1);
  }
  
  public void render(int layer)
  {
    org.lwjgl.opengl.GL11.glCallList(lists + layer);
  }
  
  public void setDirty()
  {
    if (!dirty)
    {
      dirtiedTime = System.currentTimeMillis();
    }
    dirty = true;
  }
  
  public boolean isDirty()
  {
    return dirty;
  }
  
  public float distanceToSqr(Player player)
  {
    float xd = x - x;
    float yd = y - y;
    float zd = z - z;
    return xd * xd + yd * yd + zd * zd;
  }
}
