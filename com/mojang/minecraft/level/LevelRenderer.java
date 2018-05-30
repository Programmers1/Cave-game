package com.mojang.minecraft.level;

import com.mojang.minecraft.HitResult;
import com.mojang.minecraft.Player;
import com.mojang.minecraft.Textures;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.phys.AABB;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.lwjgl.opengl.GL11;



public class LevelRenderer
  implements LevelListener
{
  public static final int MAX_REBUILDS_PER_FRAME = 8;
  public static final int CHUNK_SIZE = 16;
  private Level level;
  private Chunk[] chunks;
  private int xChunks;
  private int yChunks;
  private int zChunks;
  
  public LevelRenderer(Level level)
  {
    this.level = level;
    level.addListener(this);
    
    xChunks = (width / 16);
    yChunks = (depth / 16);
    zChunks = (height / 16);
    
    chunks = new Chunk[xChunks * yChunks * zChunks];
    for (int x = 0; x < xChunks; x++) {
      for (int y = 0; y < yChunks; y++)
        for (int z = 0; z < zChunks; z++)
        {
          int x0 = x * 16;
          int y0 = y * 16;
          int z0 = z * 16;
          int x1 = (x + 1) * 16;
          int y1 = (y + 1) * 16;
          int z1 = (z + 1) * 16;
          
          if (x1 > width) x1 = width;
          if (y1 > depth) y1 = depth;
          if (z1 > height) z1 = height;
          chunks[((x + y * xChunks) * zChunks + z)] = new Chunk(level, x0, y0, z0, x1, y1, z1);
        }
    }
  }
  
  public List<Chunk> getAllDirtyChunks() {
    ArrayList<Chunk> dirty = null;
    for (int i = 0; i < chunks.length; i++)
    {
      Chunk chunk = chunks[i];
      if (chunk.isDirty())
      {
        if (dirty == null) dirty = new ArrayList();
        dirty.add(chunk);
      }
    }
    return dirty;
  }
  
  public void render(Player player, int layer)
  {
    GL11.glEnable(3553);
    int id = Textures.loadTexture("/terrain.png", 9728);
    GL11.glBindTexture(3553, id);
    Frustum frustum = Frustum.getFrustum();
    for (int i = 0; i < chunks.length; i++)
    {
      if (frustum.isVisible(chunks[i].aabb)) chunks[i].render(layer);
    }
    GL11.glDisable(3553);
  }
  
  public void updateDirtyChunks(Player player)
  {
    List<Chunk> dirty = getAllDirtyChunks();
    if (dirty == null) { return;
    }
    Collections.sort(dirty, new DirtyChunkSorter(player, Frustum.getFrustum()));
    for (int i = 0; (i < 8) && (i < dirty.size()); i++)
    {
      ((Chunk)dirty.get(i)).rebuild();
    }
  }
  
  public void pick(Player player, Frustum frustum)
  {
    Tesselator t = Tesselator.instance;
    float r = 3.0F;
    AABB box = bb.grow(r, r, r);
    int x0 = (int)x0;
    int x1 = (int)(x1 + 1.0F);
    int y0 = (int)y0;
    int y1 = (int)(y1 + 1.0F);
    int z0 = (int)z0;
    int z1 = (int)(z1 + 1.0F);
    
    GL11.glInitNames();
    GL11.glPushName(0);
    GL11.glPushName(0);
    for (int x = x0; x < x1; x++)
    {
      GL11.glLoadName(x);
      GL11.glPushName(0);
      for (int y = y0; y < y1; y++)
      {
        GL11.glLoadName(y);
        GL11.glPushName(0);
        for (int z = z0; z < z1; z++)
        {
          Tile tile = Tile.tiles[level.getTile(x, y, z)];
          if ((tile != null) && (frustum.isVisible(tile.getTileAABB(x, y, z))))
          {
            GL11.glLoadName(z);
            GL11.glPushName(0);
            for (int i = 0; i < 6; i++)
            {
              GL11.glLoadName(i);
              t.init();
              tile.renderFaceNoTexture(t, x, y, z, i);
              t.flush();
            }
            GL11.glPopName();
          }
        }
        GL11.glPopName();
      }
      GL11.glPopName();
    }
    GL11.glPopName();
    GL11.glPopName();
  }
  
  public void renderHit(HitResult h)
  {
    Tesselator t = Tesselator.instance;
    GL11.glEnable(3042);
    
    GL11.glBlendFunc(770, 1);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, ((float)Math.sin(System.currentTimeMillis() / 100.0D) * 0.2F + 0.4F) * 0.5F);
    t.init();
    Tile.rock.renderFaceNoTexture(t, x, y, z, f);
    t.flush();
    GL11.glDisable(3042);
  }
  
  public void setDirty(int x0, int y0, int z0, int x1, int y1, int z1)
  {
    x0 /= 16;
    x1 /= 16;
    y0 /= 16;
    y1 /= 16;
    z0 /= 16;
    z1 /= 16;
    
    if (x0 < 0) x0 = 0;
    if (y0 < 0) y0 = 0;
    if (z0 < 0) z0 = 0;
    if (x1 >= xChunks) x1 = xChunks - 1;
    if (y1 >= yChunks) y1 = yChunks - 1;
    if (z1 >= zChunks) { z1 = zChunks - 1;
    }
    for (int x = x0; x <= x1; x++) {
      for (int y = y0; y <= y1; y++) {
        for (int z = z0; z <= z1; z++)
        {
          chunks[((x + y * xChunks) * zChunks + z)].setDirty(); }
      }
    }
  }
  
  public void tileChanged(int x, int y, int z) {
    setDirty(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1);
  }
  
  public void lightColumnChanged(int x, int z, int y0, int y1)
  {
    setDirty(x - 1, y0 - 1, z - 1, x + 1, y1 + 1, z + 1);
  }
  
  public void allChanged()
  {
    setDirty(0, 0, 0, level.width, level.depth, level.height);
  }
}
