package com.mojang.minecraft.particle;

import com.mojang.minecraft.Player;
import com.mojang.minecraft.Textures;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.Tesselator;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;


public class ParticleEngine
{
  protected Level level;
  private List<Particle> particles = new ArrayList();
  
  public ParticleEngine(Level level)
  {
    this.level = level;
  }
  
  public void add(Particle p)
  {
    particles.add(p);
  }
  
  public void tick()
  {
    for (int i = 0; i < particles.size(); i++)
    {
      Particle p = (Particle)particles.get(i);
      p.tick();
      if (removed)
      {
        particles.remove(i--);
      }
    }
  }
  
  public void render(Player player, float a, int layer)
  {
    GL11.glEnable(3553);
    int id = Textures.loadTexture("/terrain.png", 9728);
    GL11.glBindTexture(3553, id);
    float xa = -(float)Math.cos(yRot * 3.141592653589793D / 180.0D);
    float za = -(float)Math.sin(yRot * 3.141592653589793D / 180.0D);
    
    float xa2 = -za * (float)Math.sin(xRot * 3.141592653589793D / 180.0D);
    float za2 = xa * (float)Math.sin(xRot * 3.141592653589793D / 180.0D);
    float ya = (float)Math.cos(xRot * 3.141592653589793D / 180.0D);
    
    Tesselator t = Tesselator.instance;
    GL11.glColor4f(0.8F, 0.8F, 0.8F, 1.0F);
    t.init();
    for (int i = 0; i < particles.size(); i++)
    {
      Particle p = (Particle)particles.get(i);
      if ((p.isLit() ^ layer == 1))
      {
        p.render(t, a, xa, ya, za, xa2, za2);
      }
    }
    t.flush();
    GL11.glDisable(3553);
  }
}
