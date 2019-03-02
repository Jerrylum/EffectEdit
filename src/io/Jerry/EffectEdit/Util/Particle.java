package io.Jerry.EffectEdit.Util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public enum Particle
{
  EXPLOSION_NORMAL,  EXPLOSION_LARGE,  EXPLOSION_HUGE,  FIREWORKS_SPARK,  WATER_BUBBLE,  WATER_SPLASH,  WATER_WAKE,  SUSPENDED,  SUSPENDED_DEPTH,  CRIT,  CRIT_MAGIC,  SMOKE_NORMAL,  SMOKE_LARGE,  SPELL,  SPELL_INSTANT,  SPELL_MOB,  SPELL_MOB_AMBIENT,  SPELL_WITCH,  DRIP_WATER,  DRIP_LAVA,  VILLAGER_ANGRY,  VILLAGER_HAPPY,  TOWN_AURA,  NOTE,  PORTAL,  ENCHANTMENT_TABLE,  FLAME,  LAVA,  FOOTSTEP,  CLOUD,  REDSTONE,  SNOWBALL,  SNOW_SHOVEL,  SLIME,  HEART,  BARRIER,  ITEM_CRACK(ItemStack.class),  BLOCK_CRACK(MaterialData.class),  BLOCK_DUST(MaterialData.class),  WATER_DROP,  ITEM_TAKE,  MOB_APPEARANCE,  DRAGON_BREATH,  END_ROD,  DAMAGE_INDICATOR,  SWEEP_ATTACK;
  
  private final Class<?> dataType;
  
  private Particle()
  {
    this.dataType = Void.class;
  }
  
  private Particle(Class<?> data)
  {
    this.dataType = data;
  }
  
  public Class<?> getDataType()
  {
    return this.dataType;
  }
}