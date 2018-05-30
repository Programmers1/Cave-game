package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import java.util.Random;

public class GrassTile
  extends Tile
{
  protected GrassTile(int id)
  {
    super(id);
    tex = 3;
  }
  
  protected int getTexture(int face)
  {
    if (face == 1) return 0;
    if (face == 0) return 2;
    return 3;
  }
  
  public void tick(Level level, int x, int y, int z, Random random)
  {
    if (!level.isLit(x, y, z))
    {
      level.setTile(x, y, z, dirtid);
    }
    else
    {
      for (int i = 0; i < 4; i++)
      {
        int xt = x + random.nextInt(3) - 1;
        int yt = y + random.nextInt(5) - 3;
        int zt = z + random.nextInt(3) - 1;
        if ((level.getTile(xt, yt, zt) == dirtid) && (level.isLit(xt, yt, zt)))
        {
          level.setTile(xt, yt, zt, grassid);
        }
      }
    }
  }
}
