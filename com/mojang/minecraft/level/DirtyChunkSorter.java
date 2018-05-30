package com.mojang.minecraft.level;

import com.mojang.minecraft.Player;
import java.util.Comparator;

public class DirtyChunkSorter
  implements Comparator<Chunk>
{
  private Player player;
  private Frustum frustum;
  private long now = System.currentTimeMillis();
  
  public DirtyChunkSorter(Player player, Frustum frustum)
  {
    this.player = player;
    this.frustum = frustum;
  }
  
  public int compare(Chunk c0, Chunk c1)
  {
    boolean i0 = frustum.isVisible(aabb);
    boolean i1 = frustum.isVisible(aabb);
    if ((i0) && (!i1)) return -1;
    if ((i1) && (!i0)) return 1;
    int t0 = (int)((now - dirtiedTime) / 2000L);
    int t1 = (int)((now - dirtiedTime) / 2000L);
    if (t0 < t1) return -1;
    if (t0 > t1) return 1;
    return c0.distanceToSqr(player) < c1.distanceToSqr(player) ? -1 : 1;
  }
}
