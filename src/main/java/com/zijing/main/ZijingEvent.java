package com.zijing.main;

import org.lwjgl.input.Keyboard;

import com.zijing.ZijingMod;
import com.zijing.items.staff.ItemZilingZhu;
import com.zijing.items.tool.ItemArmorZijingBody;
import com.zijing.items.tool.ItemArmorZijingBoots;
import com.zijing.items.tool.ItemArmorZijingHelmet;
import com.zijing.items.tool.ItemArmorZijingLegs;
import com.zijing.main.gui.GuiUpgrade;
import com.zijing.main.playerdata.ShepherdCapability;
import com.zijing.main.playerdata.ShepherdProvider;
import com.zijing.util.PlayerUtil;
import com.zijing.util.StringUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

public class ZijingEvent {
	
	@SubscribeEvent
	public void entityFall(LivingFallEvent event) {
		if (!event.getEntity().world.isRemote && event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.getEntity();
			ItemStack mainHandStack = player.getHeldItemMainhand();
			ItemStack offhandStack =  player.getHeldItemOffhand();
			if(event.getDistance() > 3 && ((null != mainHandStack && mainHandStack.getItem() == BaseControl.itemZilingZhu) || (null != offhandStack && offhandStack.getItem() == BaseControl.itemZilingZhu)) && ShepherdProvider.hasCapabilityFromPlayer(player)) {
				ShepherdCapability shepherdCapability = ShepherdProvider.getCapabilityFromPlayer(player);
				if(shepherdCapability.getMagic() >= ItemZilingZhu.MagicSkill5) {
					event.setDistance(0);
					shepherdCapability.setMagic(shepherdCapability.getMagic() - ItemZilingZhu.MagicSkill5);
					ShepherdProvider.updateChangeToClient(player);
				}else {
					player.sendMessage(StringUtil.MagicIsNotEnough(ItemZilingZhu.MagicSkill5));
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
	public void entityAttack(LivingAttackEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (!entity.world.isRemote && entity instanceof EntityPlayer && event.getAmount() > 0) {
			EntityPlayer player = (EntityPlayer)entity;
			Iterable<ItemStack> armorList = player.getArmorInventoryList();
			for(ItemStack stack: armorList) {
				if(null != stack && stack.getItem() == BaseControl.itemArmorZijingHelmet && null == player.getActivePotionEffect(MobEffects.WATER_BREATHING)) {
					player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, ItemArmorZijingHelmet.effectTick, 0));
				}
				if(null != stack && stack.getItem() == BaseControl.itemArmorZijingBody && null == player.getActivePotionEffect(MobEffects.REGENERATION)) {
					player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, ItemArmorZijingBody.effectTick, 0));
				}
				if(null != stack && stack.getItem() == BaseControl.itemArmorZijingLegs && null == player.getActivePotionEffect(MobEffects.RESISTANCE)) {
					player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, ItemArmorZijingLegs.effectTick, 0));
				}
				if(null != stack && stack.getItem() == BaseControl.itemArmorZijingBoots && null == player.getActivePotionEffect(MobEffects.SPEED)) {
					player.addPotionEffect(new PotionEffect(MobEffects.SPEED, ItemArmorZijingBoots.effectTick, 0));
				}
			}
		}
	}

	@SubscribeEvent
	public void playerClone(PlayerEvent.Clone event){
		if(event.isWasDeath() && !event.getEntity().world.isRemote){
			EntityPlayer newPlayer = event.getEntityPlayer();
			EntityPlayer oldPlayer = event.getOriginal();
			if(ShepherdProvider.hasCapabilityFromPlayer(newPlayer) && ShepherdProvider.hasCapabilityFromPlayer(oldPlayer)) {
				ShepherdCapability newCapb = ShepherdProvider.getCapabilityFromPlayer(newPlayer);
				ShepherdCapability oldCapb = ShepherdProvider.getCapabilityFromPlayer(event.getOriginal());
				newCapb.readNBT(null, oldCapb.writeNBT(null));
				newCapb.setBlood(1);
				newCapb.setMagic(0);
    			PlayerUtil.setAllValueToPlayer(newPlayer, newCapb);
				ShepherdProvider.updateChangeToClient(newPlayer);
			}
		}
	}
	
    @SubscribeEvent
    public void entityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer && !event.getEntity().world.isRemote) {
        	EntityPlayer player = (EntityPlayer) event.getEntity();
        	if(ShepherdProvider.hasCapabilityFromPlayer(player)){
    			ShepherdCapability newCapb = ShepherdProvider.getCapabilityFromPlayer(player);
    			PlayerUtil.setAllValueToPlayer(player, newCapb);
                ShepherdProvider.updateChangeToClient(player);
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
		if (!FMLClientHandler.instance().isGUIOpen(GuiUpgrade.MyGuiContainer.class)) {
			if (Keyboard.isKeyDown(key1.getKeyCode())) {
				EntityPlayer player = Minecraft.getMinecraft().player;
				player.openGui(ZijingMod.instance, GuiUpgrade.GUIID, player.world, (int) player.posX, (int) (player.posY + 1.62D), (int) player.posZ);
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
}
