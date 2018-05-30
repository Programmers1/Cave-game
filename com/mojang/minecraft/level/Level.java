package com.mojang.minecraft.level;

import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.phys.AABB;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class Level
{
  private static final int TILE_UPDATE_INTERVAL = 400;
  public final int width;
  public final int height;
  public final int depth;
  private byte[] blocks;
  private int[] lightDepths;
  private ArrayList<LevelListener> levelListeners = new ArrayList();
  private Random random = new Random();
  
  public Level(int w, int h, int d)
  {
    width = w;
    height = h;
    depth = d;
    blocks = new byte[w * h * d];
    lightDepths = new int[w * h];
    
    boolean mapLoaded = load();
    if (!mapLoaded) { generateMap();
    }
    calcLightDepths(0, 0, w, h);
  }
  
  private void generateMap()
  {
    int w = width;
    int h = height;
    int d = depth;
    int[] heightmap1 = new PerlinNoiseFilter(0).read(w, h);
    int[] heightmap2 = new PerlinNoiseFilter(0).read(w, h);
    int[] cf = new PerlinNoiseFilter(1).read(w, h);
    int[] rockMap = new PerlinNoiseFilter(1).read(w, h);
    
    for (int x = 0; x < w; x++) {
      for (int y = 0; y < d; y++) {
        for (int z = 0; z < h; z++)
        {
          int dh1 = heightmap1[(x + z * width)];
          int dh2 = heightmap2[(x + z * width)];
          int cfh = cf[(x + z * width)];
          
          if (cfh < 128) { dh2 = dh1;
          }
          int dh = dh1;
          if (dh2 > dh) {
            dh = dh2;
          } else
            dh2 = dh1;
          dh = dh / 8 + d / 3;
          
          int rh = rockMap[(x + z * width)] / 8 + d / 3;
          if (rh > dh - 2) { rh = dh - 2;
          }
          
          int i = (y * height + z) * width + x;
          int id = 0;
          if (y == dh) id = grassid;
          if (y < dh) id = dirtid;
          if (y <= rh) id = rockid;
          blocks[i] = ((byte)id);
        }
      }
    }
  }
  
  public boolean load() {
    try {
      DataInputStream dis = new DataInputStream(new java.util.zip.GZIPInputStream(new java.io.FileInputStream(new java.io.File("level.dat"))));
      dis.readFully(blocks);
      calcLightDepths(0, 0, width, height);
      for (int i = 0; i < levelListeners.size(); i++)
        ((LevelListener)levelListeners.get(i)).allChanged();
      dis.close();
      return true;
    }
    catch (Exception e)
    {
      e.printStackTrace(); }
    return false;
  }
  

  public void save()
  {
    try
    {
      DataOutputStream dos = new DataOutputStream(new java.util.zip.GZIPOutputStream(new java.io.FileOutputStream(new java.io.File("level.dat"))));
      dos.write(blocks);
      dos.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public void calcLightDepths(int x0, int y0, int x1, int y1)
  {
    for (int x = x0; x < x0 + x1; x++) {
      for (int z = y0; z < y0 + y1; z++)
      {
        int oldDepth = lightDepths[(x + z * width)];
        int y = depth - 1;
        while ((y > 0) && (!isLightBlocker(x, y, z)))
          y--;
        lightDepths[(x + z * width)] = y;
        
        if (oldDepth != y)
        {
          int yl0 = oldDepth < y ? oldDepth : y;
          int yl1 = oldDepth > y ? oldDepth : y;
          for (int i = 0; i < levelListeners.size(); i++)
            ((LevelListener)levelListeners.get(i)).lightColumnChanged(x, z, yl0, yl1);
        }
      }
    }
  }
  
  public void addListener(LevelListener levelListener) {
    levelListeners.add(levelListener);
  }
  
  public void removeListener(LevelListener levelListener)
  {
    levelListeners.remove(levelListener);
  }
  











  public boolean isLightBlocker(int x, int y, int z)
  {
    Tile tile = Tile.tiles[getTile(x, y, z)];
    if (tile == null) return false;
    return tile.blocksLight();
  }
  
  public ArrayList<AABB> getCubes(AABB aABB)
  {
    ArrayList<AABB> aABBs = new ArrayList();
    int x0 = (int)x0;
    int x1 = (int)(x1 + 1.0F);
    int y0 = (int)y0;
    int y1 = (int)(y1 + 1.0F);
    int z0 = (int)z0;
    int z1 = (int)(z1 + 1.0F);
    
    if (x0 < 0) x0 = 0;
    if (y0 < 0) y0 = 0;
    if (z0 < 0) z0 = 0;
    if (x1 > width) x1 = width;
    if (y1 > depth) y1 = depth;
    if (z1 > height) { z1 = height;
    }
    for (int x = x0; x < x1; x++) {
      for (int y = y0; y < y1; y++)
        for (int z = z0; z < z1; z++)
        {
          Tile tile = Tile.tiles[getTile(x, y, z)];
          if (tile != null)
          {
            AABB aabb = tile.getAABB(x, y, z);
            if (aabb != null) aABBs.add(aabb);
          }
        }
    }
    return aABBs;
  }
  









  public boolean setTile(int x, int y, int z, int type)
  {
    if ((x < 0) || (y < 0) || (z < 0) || (x >= width) || (y >= depth) || (z >= height)) return false;
    if (type == blocks[((y * height + z) * width + x)]) { return false;
    }
    blocks[((y * height + z) * width + x)] = ((byte)type);
    calcLightDepths(x, z, 1, 1);
    for (int i = 0; i < levelListeners.size(); i++) {
      ((LevelListener)levelListeners.get(i)).tileChanged(x, y, z);
    }
    return true;
  }
  
  public boolean isLit(int x, int y, int z)
  {
    if ((x < 0) || (y < 0) || (z < 0) || (x >= width) || (y >= depth) || (z >= height)) return true;
    return y >= lightDepths[(x + z * width)];
  }
  
  public int getTile(int x, int y, int z)
  {
    if ((x < 0) || (y < 0) || (z < 0) || (x >= width) || (y >= depth) || (z >= height)) return 0;
    return blocks[((y * height + z) * width + x)];
  }
  
  public boolean isSolidTile(int x, int y, int z)
  {
    Tile tile = Tile.tiles[getTile(x, y, z)];
    if (tile == null) return false;
    return tile.isSolid();
  }
  
  int unprocessed = 0;
  
  public void tick()
  {
    unprocessed += width * height * depth;
    int ticks = unprocessed / 400;
    unprocessed -= ticks * 400;
    for (int i = 0; i < ticks; i++)
    {
      int x = random.nextInt(width);
      int y = random.nextInt(depth);
      int z = random.nextInt(height);
      Tile tile = Tile.tiles[getTile(x, y, z)];
      if (tile != null)
      {
        tile.tick(this, x, y, z, random);
      }
    }
  }
}
