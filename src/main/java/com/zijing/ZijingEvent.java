package com.zijing;

import org.lwjgl.input.Keyboard;

import com.zijing.data.message.OpenServerGUIMessage;
import com.zijing.data.playerdata.ShepherdCapability;
import com.zijing.data.playerdata.ShepherdProvider;
import com.zijing.entity.EntityDisciple;
import com.zijing.gui.GuiPlayeryCapability;
import com.zijing.items.tool.ItemArmorZijingBody;
import com.zijing.items.tool.ItemArmorZijingBoots;
import com.zijing.items.tool.ItemArmorZijingHelmet;
import com.zijing.items.tool.ItemArmorZijingLegs;
import com.zijing.itf.EntityShepherdCapability;
import com.zijing.util.EntityUtil;
import com.zijing.util.EnumGender;
import com.zijing.util.SkillEntity;
import com.zijing.util.StringUtil;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

public class ZijingEvent {
	
	@SubscribeEvent
	public void entityFall(LivingFallEvent event) {
		if (!event.getEntity().world.isRemote) {
			if(event.getEntity() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)event.getEntity();
				ItemStack mainHandStack = player.getHeldItemMainhand();
				ItemStack offhandStack =  player.getHeldItemOffhand();
				if(event.getDistance() > 3 && ((null != mainHandStack && mainHandStack.getItem() == BaseControl.itemZilingZhu) || (null != offhandStack && offhandStack.getItem() == BaseControl.itemZilingZhu)) && ShepherdProvider.hasCapabilityFromPlayer(player)) {
					ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
					if(shepherdCapability.getMagic() >= SkillEntity.MagicSkill_ImmuneFallDamage) {
						event.setDistance(0);
						shepherdCapability.setMagic(shepherdCapability.getMagic() - SkillEntity.MagicSkill_ImmuneFallDamage);
						ShepherdProvider.updateChangeToClient(player);
					}else {
						player.sendMessage(StringUtil.magicIsNotEnough(SkillEntity.MagicSkill_ImmuneFallDamage));
					}
				}
			}
		}
	}

//	@SubscribeEvent
//	public void entityAttack(CriticalHitEvent event) {
//		Entity target = event.getTarget();
//		if (!target.world.isRemote) {
//			EntityPlayer player = event.getEntityPlayer();
//		}
//	}

	
	@SubscribeEvent
	public void livingHurt(LivingHurtEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (!entity.world.isRemote && event.getAmount() > 0) {

			ShepherdCapability shepherdCapability = entity instanceof EntityShepherdCapability ? ((EntityShepherdCapability)entity).getShepherdCapability() : ShepherdProvider.getCapabilityFromPlayer(entity);
			if(null != shepherdCapability) {

				int totalDefense = 0;
				Iterable<ItemStack> armorList = entity.getArmorInventoryList();
				for(ItemStack itemStack : armorList) {
					Item item = itemStack.getItem();
					if(item instanceof ItemArmor) {
						totalDefense += ((ItemArmor)item).damageReduceAmount;
					}
				}
				totalDefense += shepherdCapability.getPhysicalDefense();
				
				if(totalDefense > 30) {
					double reduction = Math.min((totalDefense - 30) * ZijingMod.config.getDAMAGE_REDUCTION_K(), event.getAmount());
					float amount = event.getAmount() - (float)reduction;
					event.setAmount(amount);
					if(entity instanceof EntityPlayer) {
						entity.sendMessage(StringUtil.damageReduction(reduction, amount));
					}
				}
			}
		}
	}
	
	
	@SubscribeEvent
	public void entityAttack(LivingAttackEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (!entity.world.isRemote && event.getAmount() > 0) {
			Iterable<ItemStack> armorList = entity.getArmorInventoryList();
			for(ItemStack stack: armorList) {
				if(null != stack && stack.getItem() == BaseControl.itemArmorZijingHelmet && null == entity.getActivePotionEffect(MobEffects.WATER_BREATHING)) {
					entity.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, ItemArmorZijingHelmet.effectTick, 0));
				}
				if(null != stack && stack.getItem() == BaseControl.itemArmorZijingBody && null == entity.getActivePotionEffect(MobEffects.REGENERATION)) {
					entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, ItemArmorZijingBody.effectTick, 0));
				}
				if(null != stack && stack.getItem() == BaseControl.itemArmorZijingLegs && null == entity.getActivePotionEffect(MobEffects.RESISTANCE)) {
					entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, ItemArmorZijingLegs.effectTick, 0));
				}
				if(null != stack && stack.getItem() == BaseControl.itemArmorZijingBoots && null == entity.getActivePotionEffect(MobEffects.SPEED)) {
					entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, ItemArmorZijingBoots.effectTick, 0));
				}
			}
		}
	}

	@SubscribeEvent
	public void playerClone(PlayerEvent.Clone event){
		if(event.isWasDeath()){
			EntityPlayer newPlayer = event.getEntityPlayer();
			EntityPlayer oldPlayer = event.getOriginal();
			if(ShepherdProvider.hasCapabilityFromPlayer(newPlayer) && ShepherdProvider.hasCapabilityFromPlayer(oldPlayer)) {
				ShepherdCapability newCapb = ShepherdProvider.getCapabilityFromPlayer(newPlayer);
				ShepherdCapability oldCapb = ShepherdProvider.getCapabilityFromPlayer(event.getOriginal());
				newCapb.readNBT(null, oldCapb.writeNBT(null));
				newCapb.setBlood(1);
				newCapb.setMagic(0);
    			EntityUtil.setPlayerAllValue(newPlayer, newCapb);
			}
		}
	}
	
    @SubscribeEvent
    public void entityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
        	EntityPlayer player = (EntityPlayer) event.getEntity();
        	if(ShepherdProvider.hasCapabilityFromPlayer(player)){
    			ShepherdCapability newCapb = ShepherdProvider.getCapabilityFromPlayer(player);
    			EntityUtil.setPlayerAllValue(player, newCapb);
        	}
        }
    }

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof EntityPlayer && !ShepherdProvider.hasCapabilityFromPlayer(event.getObject()))
    		event.addCapability(new ResourceLocation(ShepherdCapability.NAME), new ShepherdProvider());
    }

	public static final KeyBinding key1 = new KeyBinding("key.zijingmod", Keyboard.KEY_R, "key.categories.misc");
	
	@SubscribeEvent
	public void bindingKeys(InputEvent.KeyInputEvent event) {
		if (Keyboard.isKeyDown(key1.getKeyCode())) {
			if (!FMLClientHandler.instance().isGUIOpen(GuiPlayeryCapability.MyGuiContainer.class)) {
				BaseControl.netWorkWrapper.sendToServer(new OpenServerGUIMessage(GuiPlayeryCapability.GUIID, 0, 0, 0));
			}
		}
	}
	
	@SubscribeEvent
	public void restore(TickEvent.PlayerTickEvent event) {
		if(event.phase == Phase.START && event.side ==  Side.SERVER ) {
			EntityPlayer player = event.player;
			if(ShepherdProvider.hasCapabilityFromPlayer(player) && !player.isDead && player.getHealth() > 0) {
				ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
				FoodStats playerFoodStats = player.getFoodStats();
				if((player.getHealth() < player.getMaxHealth() || shepherdCapability.getMagic() < shepherdCapability.getMaxMagic()) && playerFoodStats.getFoodLevel() + playerFoodStats.getSaturationLevel() > 1) {
					if(player.getHealth() < player.getMaxHealth()) {
						player.setHealth(player.getHealth() + (float)shepherdCapability.getBloodRestore());
						shepherdCapability.setBlood(player.getHealth());
						playerFoodStats.addExhaustion(ZijingMod.config.getRESTORE_NEED_FOOD());
					}
					if(shepherdCapability.getMagic() < shepherdCapability.getMaxMagic()) {
						shepherdCapability.setMagic(Math.min(shepherdCapability.getMagic() + shepherdCapability.getMagicRestore(), shepherdCapability.getMaxMagic()));
						playerFoodStats.addExhaustion(ZijingMod.config.getRESTORE_NEED_FOOD());
					}
					ShepherdProvider.updateChangeToClient(player);
				}
				if(player.getHealth() != shepherdCapability.getBlood()) {
					shepherdCapability.setBlood(player.getHealth());
					ShepherdProvider.updateChangeToClient(player);
				}
			}
		}
	}

	@SubscribeEvent
	public void wntityInteractWithVillager(PlayerInteractEvent.EntityInteract event) {
		if(event.getTarget() instanceof EntityVillager && ((EntityVillager)event.getTarget()).isChild()) {
			EntityVillager villager = (EntityVillager)event.getTarget();
	        ItemStack itemstack = event.getEntityPlayer().getHeldItem(event.getHand());
			if(null != itemstack && BaseControl.itemZiqi == itemstack.getItem()) {
				if(!villager.world.isRemote) {
					if(villager.getRNG().nextInt(10) == 0) {
						EntityDisciple entityDisciple = new EntityDisciple(villager.world, 1, EnumGender.FEMALE.getType());
			            entityDisciple.setLocationAndAngles(villager.posX, villager.posY, villager.posZ, 0.0F, 0.0F);
			            entityDisciple.setHomePos(villager.getPosition());
			            entityDisciple.updataSwordDamageAndArmorValue();
			            entityDisciple.world.spawnEntity(entityDisciple);
			            entityDisciple.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
			            villager.setDead();
					}
					itemstack.shrink(1);
				}else if(villager.world.isRemote){
			        for (int i = 0; i < 5; ++i){
			            double d0 = villager.getRNG().nextGaussian() * 0.02D;
			            double d1 = villager.getRNG().nextGaussian() * 0.02D;
			            double d2 = villager.getRNG().nextGaussian() * 0.02D;
			            villager.world.spawnParticle(EnumParticleTypes.HEART, villager.posX + (double)(villager.getRNG().nextFloat() * villager.width * 2.0F) - (double)villager.width, villager.posY + 1.0D + (double)(villager.getRNG().nextFloat() * villager.height), villager.posZ + (double)(villager.getRNG().nextFloat() * villager.width * 2.0F) - (double)villager.width, d0, d1, d2);
			        }
				}
			}
		}
	}
	
}
