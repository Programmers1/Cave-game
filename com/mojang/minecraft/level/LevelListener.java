package com.mojang.minecraft.level;

public abstract interface LevelListener
{
  public abstract void tileChanged(int paramInt1, int paramInt2, int paramInt3);
  
  public abstract void lightColumnChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract void allChanged();
}
