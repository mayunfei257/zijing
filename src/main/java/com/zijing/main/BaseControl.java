package com.zijing.main;

import com.zijing.ZijingMod;
import com.zijing.blocks.BlockGuhuaNiunaiKuai;
import com.zijing.blocks.BlockZijingKuai;
import com.zijing.blocks.BlockZilingCao;
import com.zijing.blocks.tool.BlockMagicEnergySwitch;
import com.zijing.blocks.tool.BlockZilingMieshaZhen;
import com.zijing.blocks.tool.BlockZilingZhaohuanZhen;
import com.zijing.entity.EntityArrowBingDan;
import com.zijing.entity.EntityArrowFengyinDan;
import com.zijing.entity.EntityArrowHuoDan;
import com.zijing.entity.EntityArrowXukongDan;
import com.zijing.items.ItemDanShenshu;
import com.zijing.items.ItemDanZiling;
import com.zijing.items.ItemGuhuaNiunai;
import com.zijing.items.ItemZijing;
import com.zijing.items.ItemZiqi;
import com.zijing.items.card.ItemBookChuansong;
import com.zijing.items.card.ItemCardChuansong;
import com.zijing.items.card.ItemCardFengyin;
import com.zijing.items.staff.ItemArrowBingDan;
import com.zijing.items.staff.ItemArrowFengyinDan;
import com.zijing.items.staff.ItemArrowHuoDan;
import com.zijing.items.staff.ItemArrowXukongDan;
import com.zijing.items.staff.ItemStaffBingxue;
import com.zijing.items.staff.ItemStaffFengyin;
import com.zijing.items.staff.ItemStaffKongjian;
import com.zijing.items.staff.ItemStaffLieyan;
import com.zijing.items.staff.ItemStaffWaigua;
import com.zijing.items.staff.ItemZilingZhu;
import com.zijing.items.tool.ItemArmorZijingBody;
import com.zijing.items.tool.ItemArmorZijingBoots;
import com.zijing.items.tool.ItemArmorZijingHelmet;
import com.zijing.items.tool.ItemArmorZijingLegs;
import com.zijing.items.tool.ItemToolZijingChan;
import com.zijing.items.tool.ItemToolZijingChu;
import com.zijing.items.tool.ItemToolZijingFu;
import com.zijing.items.tool.ItemToolZijingGao;
import com.zijing.items.tool.ItemToolZijingJian;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.GameData;

public class BaseControl{
	//TODO Instantiate mod item ---
	public static Block blockGuhuaNiunaiKuai;
	public static Block blockZilingCao;
	public static Block blockZijingKuai;
	public static Block blockZilingMieshaZhen;
	public static Block blockZilingZhaohuanZhen;
	public static Block blockMagicEnergySwitch;
	
	//item
	public static Item itemGuhuaNiunai;
	public static Item itemZiqi;
	public static Item itemZijing;
	public static Item itemDanZiling;
	public static Item itemDanShenshu;
	public static Item itemZilingZhu;
	//card
	public static Item itemCardFengyin;
	public static Item itemCardChuansong;
	public static Item itemBookChuansong;
	//staff
	public static Item itemStaffBingxue;
	public static Item itemStaffLieyan;
	public static Item itemStaffKongjian;
	public static Item itemStaffFengyin;
	public static Item itemArrowBingDan;
	public static Item itemArrowHuoDan;
	public static Item itemArrowXukongDan;
	public static Item itemArrowFengyinDan;
	public static Item itemStaffWaigua;
	//tool
	public static Item itemToolZijingJian;
	public static Item itemToolZijingFu;
	public static Item itemToolZijingGao;
	public static Item itemToolZijingChan;
	public static Item itemToolZijingChu;
//	public static Item itemToolZijingDun;
	public static Item itemArmorZijingHelmet;
	public static Item itemArmorZijingBody;
	public static Item itemArmorZijingLegs;
	public static Item itemArmorZijingBoots;

	public static void init(FMLPreInitializationEvent event){
		//TODO Instantiate mod item ---
		blockGuhuaNiunaiKuai = new BlockGuhuaNiunaiKuai();
		blockZilingCao = new BlockZilingCao();
		blockZijingKuai = new BlockZijingKuai();
		blockZilingMieshaZhen = new BlockZilingMieshaZhen();
		blockZilingZhaohuanZhen = new BlockZilingZhaohuanZhen();
		blockMagicEnergySwitch = new BlockMagicEnergySwitch();
		//item
		itemGuhuaNiunai = new ItemGuhuaNiunai();
		itemZiqi = new ItemZiqi();
		itemZijing = new ItemZijing();
		itemDanZiling = new ItemDanZiling();
		itemDanShenshu = new ItemDanShenshu();
		itemZilingZhu = new ItemZilingZhu();
		//card
		itemCardFengyin = new ItemCardFengyin();
		itemCardChuansong = new ItemCardChuansong();
		itemBookChuansong = new ItemBookChuansong();
		//staff
		itemStaffBingxue = new ItemStaffBingxue();
		itemStaffLieyan = new ItemStaffLieyan();
		itemStaffKongjian = new ItemStaffKongjian();
		itemStaffFengyin = new ItemStaffFengyin();
		itemArrowBingDan = new ItemArrowBingDan();
		itemArrowHuoDan = new ItemArrowHuoDan();
		itemArrowXukongDan = new ItemArrowXukongDan();
		itemArrowFengyinDan = new ItemArrowFengyinDan();
		itemStaffWaigua = new ItemStaffWaigua();
		//tool
		itemToolZijingJian = new ItemToolZijingJian();
		itemToolZijingFu = new ItemToolZijingFu();
		itemToolZijingGao = new ItemToolZijingGao();
		itemToolZijingChan = new ItemToolZijingChan();
		itemToolZijingChu = new ItemToolZijingChu();
//		itemToolZijingDun = new ItemToolZijingDun();
		itemArmorZijingHelmet = new ItemArmorZijingHelmet();
		itemArmorZijingBody = new ItemArmorZijingBody();
		itemArmorZijingLegs = new ItemArmorZijingLegs();
		itemArmorZijingBoots = new ItemArmorZijingBoots();
		
	}
	
	public static void register(FMLPreInitializationEvent event){
		//TODO In this registration items and blocks ---
//		ForgeRegistries.BLOCKS.register(blockGuhuaNiunaiKuai);;
//		ForgeRegistries.ITEMS.register(new ItemBlock(blockGuhuaNiunaiKuai).setRegistryName(blockGuhuaNiunaiKuai.getRegistryName()));
		GameData.register_impl(blockGuhuaNiunaiKuai);
		GameData.register_impl(new ItemBlock(blockGuhuaNiunaiKuai).setRegistryName(blockGuhuaNiunaiKuai.getRegistryName()));
		GameData.register_impl(blockZilingCao);
		GameData.register_impl(new ItemBlock(blockZilingCao).setRegistryName(blockZilingCao.getRegistryName()));
		GameData.register_impl(blockZijingKuai);
		GameData.register_impl(new ItemBlock(blockZijingKuai).setRegistryName(blockZijingKuai.getRegistryName()));
		GameData.register_impl(blockZilingMieshaZhen);
		GameData.register_impl(new ItemBlock(blockZilingMieshaZhen).setRegistryName(blockZilingMieshaZhen.getRegistryName()));
		GameData.register_impl(blockZilingZhaohuanZhen);
		GameData.register_impl(new ItemBlock(blockZilingZhaohuanZhen).setRegistryName(blockZilingZhaohuanZhen.getRegistryName()));
		GameData.register_impl(blockMagicEnergySwitch);
		GameData.register_impl(new ItemBlock(blockMagicEnergySwitch).setRegistryName(blockMagicEnergySwitch.getRegistryName()));

//		ForgeRegistries.ITEMS.register(itemGuhuaNiunai);
		//item
		GameData.register_impl(itemGuhuaNiunai);
		GameData.register_impl(itemZiqi);
		GameData.register_impl(itemZijing);
		GameData.register_impl(itemDanZiling);
		GameData.register_impl(itemDanShenshu);
		GameData.register_impl(itemZilingZhu);
		//card
		GameData.register_impl(itemCardFengyin);
		GameData.register_impl(itemCardChuansong);
		GameData.register_impl(itemBookChuansong);
		//staff
		GameData.register_impl(itemStaffBingxue);
		GameData.register_impl(itemStaffLieyan);
		GameData.register_impl(itemStaffKongjian);
		GameData.register_impl(itemStaffFengyin);
		GameData.register_impl(itemArrowBingDan);
		GameData.register_impl(itemArrowHuoDan);
		GameData.register_impl(itemArrowXukongDan);
		GameData.register_impl(itemArrowFengyinDan);
		GameData.register_impl(itemStaffWaigua);
		//tool
		GameData.register_impl(itemToolZijingJian);
		GameData.register_impl(itemToolZijingFu);
		GameData.register_impl(itemToolZijingGao);
		GameData.register_impl(itemToolZijingChan);
		GameData.register_impl(itemToolZijingChu);
//		GameData.register_impl(itemToolZijingDun);
		GameData.register_impl(itemArmorZijingHelmet);
		GameData.register_impl(itemArmorZijingBody);
		GameData.register_impl(itemArmorZijingLegs);
		GameData.register_impl(itemArmorZijingBoots);
		
		
		EntityRegistry.registerModEntity(new ResourceLocation(ZijingMod.MODID + ":entityarrowbingdan"), EntityArrowBingDan.class, "entityArrowBingDan", 257, ZijingMod.instance, 64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(ZijingMod.MODID + ":entityarrowhuodan"), EntityArrowHuoDan.class, "entityArrowHuoDan", 258, ZijingMod.instance, 64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(ZijingMod.MODID + ":entityarrowxukongdan"), EntityArrowXukongDan.class, "entityArrowXukongDan", 259, ZijingMod.instance, 64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(ZijingMod.MODID + ":entityarrowfengyindan"), EntityArrowFengyinDan.class, "entityArrowFengyinDan", 260, ZijingMod.instance, 64, 1, true);
	}
    
    public static void resourceLoad(FMLPreInitializationEvent event){
    	//TODO Register Rendering ---
		bolckResourceLoad(blockGuhuaNiunaiKuai);
		bolckResourceLoad(blockZilingCao);
		bolckResourceLoad(blockZijingKuai);
		bolckResourceLoad(blockZilingMieshaZhen);
		bolckResourceLoad(blockZilingZhaohuanZhen);
		bolckResourceLoad(blockMagicEnergySwitch);
		//item
		itemResourceLoad(itemGuhuaNiunai);
		itemResourceLoad(itemZiqi);
		itemResourceLoad(itemZijing);
		itemResourceLoad(itemDanZiling);
		itemResourceLoad(itemDanShenshu);
		itemResourceLoad(itemZilingZhu);
		//card
		itemResourceLoad(itemCardFengyin);
		itemResourceLoad(itemCardChuansong);
		itemResourceLoad(itemBookChuansong);
		//staff
		itemResourceLoad(itemStaffBingxue);
		itemResourceLoad(itemStaffLieyan);
		itemResourceLoad(itemStaffKongjian);
		itemResourceLoad(itemStaffFengyin);
		itemResourceLoad(itemArrowBingDan);
		itemResourceLoad(itemArrowHuoDan);
		itemResourceLoad(itemArrowXukongDan);
		itemResourceLoad(itemArrowFengyinDan);
		itemResourceLoad(itemStaffWaigua);
		//tool
		itemResourceLoad(itemToolZijingJian);
		itemResourceLoad(itemToolZijingFu);
		itemResourceLoad(itemToolZijingGao);
		itemResourceLoad(itemToolZijingChan);
		itemResourceLoad(itemToolZijingChu);
//		itemResourceLoad(itemToolZijingDun);
		itemResourceLoad(itemArmorZijingHelmet);
		itemResourceLoad(itemArmorZijingBody);
		itemResourceLoad(itemArmorZijingLegs);
		itemResourceLoad(itemArmorZijingBoots);
    }
	
	public static void registerRecipe(FMLInitializationEvent event){
		//TODO Register synthetic methods and burn
		addShapelessRecipe(ZijingMod.MODID + ":HC_itemGuhuaNiunai", ZijingMod.MODID, new ItemStack(itemGuhuaNiunai, 1), Items.MILK_BUCKET);
		addShapelessRecipe(ZijingMod.MODID + ":FJ_blockGuhuaNiunaiKuai", ZijingMod.MODID, new ItemStack(itemGuhuaNiunai, 9), blockGuhuaNiunaiKuai);
		addShapelessRecipe(ZijingMod.MODID + ":FJ_itemGuhuaNiunai", ZijingMod.MODID, new ItemStack(Items.MILK_BUCKET, 1), itemGuhuaNiunai, Items.BUCKET);
		addRecipe(ZijingMod.MODID + ":HC_blockGuhuaNiunaiKuai", ZijingMod.MODID, new ItemStack(blockGuhuaNiunaiKuai, 1), itemGuhuaNiunai, itemGuhuaNiunai, itemGuhuaNiunai, itemGuhuaNiunai, itemGuhuaNiunai, itemGuhuaNiunai, itemGuhuaNiunai, itemGuhuaNiunai, itemGuhuaNiunai);
		
		addSmelting(blockZilingCao, new ItemStack(itemZiqi, 1), 1);
		addShapelessRecipe(ZijingMod.MODID + ":FJ_itemZijing", ZijingMod.MODID, new ItemStack(itemZiqi, 9), itemZijing);
		addShapelessRecipe(ZijingMod.MODID + ":FJ_blockZijingKuai", ZijingMod.MODID, new ItemStack(itemZijing, 9), blockZijingKuai);
		addRecipe(ZijingMod.MODID + ":HC_itemZijing", ZijingMod.MODID, new ItemStack(itemZijing, 1), itemZiqi, itemZiqi, itemZiqi, itemZiqi, itemZiqi, itemZiqi, itemZiqi, itemZiqi, itemZiqi);
		addRecipe(ZijingMod.MODID + ":HC_blockZijingKuai", ZijingMod.MODID, new ItemStack(blockZijingKuai, 1), itemZijing, itemZijing, itemZijing, itemZijing, itemZijing, itemZijing, itemZijing, itemZijing, itemZijing);
		//block
		addRecipe(ZijingMod.MODID + ":HC_blockZilingMieshaZhen", ZijingMod.MODID, new ItemStack(blockZilingMieshaZhen, 1), itemZiqi, itemZiqi, itemZiqi, Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, itemToolZijingJian, Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, itemZijing, itemZijing, itemZijing);
		addRecipe(ZijingMod.MODID + ":HC_blockZilingZhaohuanZhen", ZijingMod.MODID, new ItemStack(blockZilingZhaohuanZhen, 1), itemZijing, Items.ENDER_PEARL, itemZijing, blockZijingKuai, Blocks.PUMPKIN, blockZijingKuai, blockZijingKuai, Blocks.IRON_BLOCK, blockZijingKuai);
		//item and food
		addShapelessRecipe(ZijingMod.MODID + ":HC_itemDanZiling1", ZijingMod.MODID, new ItemStack(itemDanZiling, 2), Items.WHEAT, itemZiqi, Items.WHEAT);
		addShapelessRecipe(ZijingMod.MODID + ":HC_itemDanShenshu1", ZijingMod.MODID, new ItemStack(itemDanShenshu, 4), Items.WHEAT_SEEDS, Items.WHEAT_SEEDS, itemZiqi, Items.WHEAT_SEEDS, Items.WHEAT_SEEDS);
		addShapelessRecipe(ZijingMod.MODID + ":HC_itemDanShenshu2", ZijingMod.MODID, new ItemStack(itemDanShenshu, 4), Items.BEETROOT_SEEDS, Items.BEETROOT_SEEDS, itemZiqi, Items.BEETROOT_SEEDS, Items.BEETROOT_SEEDS);
		addShapelessRecipe(ZijingMod.MODID + ":HC_itemDanShenshu3", ZijingMod.MODID, new ItemStack(itemDanShenshu, 4), Items.MELON_SEEDS, Items.MELON_SEEDS, itemZiqi, Items.MELON_SEEDS, Items.MELON_SEEDS);
		addShapelessRecipe(ZijingMod.MODID + ":HC_itemDanShenshu4", ZijingMod.MODID, new ItemStack(itemDanShenshu, 4), Items.PUMPKIN_SEEDS, Items.PUMPKIN_SEEDS, itemZiqi, Items.PUMPKIN_SEEDS, Items.PUMPKIN_SEEDS);
		//card
		addRecipe(ZijingMod.MODID + ":HC_itemCardChuansong", ZijingMod.MODID, new ItemStack(itemCardChuansong, 1), itemZiqi, Items.PAPER, itemZiqi, Items.PAPER, Items.ENDER_PEARL, Items.PAPER, itemZiqi, Items.PAPER, itemZiqi);
		addRecipe(ZijingMod.MODID + ":HC_itemBookChuansong", ZijingMod.MODID, new ItemStack(itemBookChuansong, 1), itemZiqi, Items.BOOK, itemZiqi, Items.BOOK, Items.ENDER_PEARL, Items.BOOK, itemZiqi, Items.BOOK, itemZiqi);
		//staff
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger(ZijingMod.MODID + ":magicEnergy", ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY());
		nbt.setInteger(ZijingMod.MODID + ":maxMagicEnergy", ZijingMod.config.getSTAFF_MAX_MAGIC_ENERGY());
		ItemStack itemZilingZhuStack = new ItemStack(itemZilingZhu, 1);
		ItemStack itemStaffBingxueStack = new ItemStack(itemStaffBingxue, 1);
		ItemStack itemStaffLieyanStack = new ItemStack(itemStaffLieyan, 1);
		ItemStack itemStaffKongjianStack = new ItemStack(itemStaffKongjian, 1);
		ItemStack itemStaffFengyinStack = new ItemStack(itemStaffFengyin, 1);
		itemZilingZhuStack.setTagCompound(nbt.copy());
		itemStaffBingxueStack.setTagCompound(nbt.copy());
		itemStaffLieyanStack.setTagCompound(nbt.copy());
		itemStaffKongjianStack.setTagCompound(nbt.copy());
		itemStaffFengyinStack.setTagCompound(nbt.copy());
		addRecipe(ZijingMod.MODID + ":HC_itemZilingZhu", ZijingMod.MODID, itemZilingZhuStack, Items.DIAMOND, itemZijing, Items.DIAMOND, itemZijing, Items.ENDER_PEARL, itemZijing, Items.DIAMOND, itemZijing, Items.DIAMOND);
		addRecipe(ZijingMod.MODID + ":HC_itemStaffBingxue", ZijingMod.MODID, itemStaffBingxueStack, null, Blocks.ICE, itemZilingZhu, null, Blocks.ICE, Blocks.ICE, Blocks.ICE, null, null);
		addRecipe(ZijingMod.MODID + ":HC_itemStaffLieyan", ZijingMod.MODID, itemStaffLieyanStack, null, Blocks.MAGMA, itemZilingZhu, null, Blocks.MAGMA, Blocks.MAGMA, Blocks.MAGMA, null, null);
		addRecipe(ZijingMod.MODID + ":HC_itemStaffKongjian", ZijingMod.MODID, itemStaffKongjianStack, null, Items.ENDER_EYE, itemZilingZhu, null, Items.ENDER_EYE, Items.ENDER_EYE, Items.ENDER_EYE, null, null);
		addRecipe(ZijingMod.MODID + ":HC_itemStaffFengyin", ZijingMod.MODID, itemStaffFengyinStack, null, Blocks.GOLD_BLOCK, itemZilingZhu, null, Blocks.GOLD_BLOCK, Blocks.GOLD_BLOCK, Blocks.GOLD_BLOCK, null, null);
		//tool
		addRecipe(ZijingMod.MODID + ":HC_itemToolZijingJian", ZijingMod.MODID, new ItemStack(itemToolZijingJian, 1), null, itemZijing, null, null, itemZijing, null, null, Items.STICK, null);
		addRecipe(ZijingMod.MODID + ":HC_itemToolZijingFu", ZijingMod.MODID, new ItemStack(itemToolZijingFu, 1), itemZijing, itemZijing, null, itemZijing, Items.STICK, null, null, Items.STICK, null);
		addRecipe(ZijingMod.MODID + ":HC_itemToolZijingGao", ZijingMod.MODID, new ItemStack(itemToolZijingGao, 1), itemZijing, itemZijing, itemZijing, null, Items.STICK, null, null, Items.STICK, null);
		addRecipe(ZijingMod.MODID + ":HC_itemToolZijingChan", ZijingMod.MODID, new ItemStack(itemToolZijingChan, 1), null, itemZijing, null, null, Items.STICK, null, null, Items.STICK, null);
		addRecipe(ZijingMod.MODID + ":HC_itemToolZijingChu", ZijingMod.MODID, new ItemStack(itemToolZijingChu, 1), itemZijing, itemZijing, null, null, Items.STICK, null, null, Items.STICK, null);
//		addRecipe(new ItemStack(itemToolZijingDun, 1), null, itemZiqi, null, itemZiqi, Items.SHIELD, itemZiqi, null, itemZiqi, null);
		addRecipe(ZijingMod.MODID + ":HC_itemArmorZijingHelmet", ZijingMod.MODID, new ItemStack(itemArmorZijingHelmet, 1), itemZijing, itemZijing, itemZijing, itemZijing, null, itemZijing, null, null, null);
		addRecipe(ZijingMod.MODID + ":HC_itemArmorZijingBody", ZijingMod.MODID, new ItemStack(itemArmorZijingBody, 1), itemZijing, null, itemZijing, itemZijing, itemZijing, itemZijing, itemZijing, itemZijing, itemZijing);
		addRecipe(ZijingMod.MODID + ":HC_itemArmorZijingLegs", ZijingMod.MODID, new ItemStack(itemArmorZijingLegs, 1), itemZijing, itemZijing, itemZijing, itemZijing, null, itemZijing, itemZijing, null, itemZijing);
		addRecipe(ZijingMod.MODID + ":HC_itemArmorZijingBoots", ZijingMod.MODID, new ItemStack(itemArmorZijingBoots, 1), null, null, null, itemZijing, null, itemZijing, itemZijing, null, itemZijing);
		
		
		addSmelting(Blocks.GRAVEL, new ItemStack(Items.FLINT, 1), 1);
		addShapelessRecipe(ZijingMod.MODID + ":FJ_CLAY", ZijingMod.MODID, new ItemStack(Items.CLAY_BALL, 4),Blocks.CLAY);
		addShapelessRecipe(ZijingMod.MODID + ":FJ_QUARTZ_BLOCK", ZijingMod.MODID, new ItemStack(Items.QUARTZ, 4),Blocks.QUARTZ_BLOCK);
		addShapelessRecipe(ZijingMod.MODID + ":FJ_WOOL", ZijingMod.MODID, new ItemStack(Items.STRING, 4),Blocks.WOOL);
		addShapelessRecipe(ZijingMod.MODID + ":FJ_GLOWSTONE", ZijingMod.MODID, new ItemStack(Items.GLOWSTONE_DUST, 4),Blocks.GLOWSTONE);
		addShapelessRecipe(ZijingMod.MODID + ":FJ_MELON_BLOCK", ZijingMod.MODID, new ItemStack(Items.MELON, 9),Blocks.MELON_BLOCK);
		addShapelessRecipe(ZijingMod.MODID + ":FJ_LEATHER", ZijingMod.MODID, new ItemStack(Items.RABBIT_HIDE, 4),Items.LEATHER);
		addShapelessRecipe(ZijingMod.MODID + ":FJ_NETHER_WART_BLOCK", ZijingMod.MODID, new ItemStack(Items.NETHER_WART, 9),Blocks.NETHER_WART_BLOCK);
		
	}
	
	public static void renderLoad(FMLInitializationEvent event){
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowBingDan.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), itemArrowBingDan, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowHuoDan.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), itemArrowHuoDan, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowXukongDan.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), itemArrowXukongDan, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowFengyinDan.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), itemArrowFengyinDan, Minecraft.getMinecraft().getRenderItem()));
	}

	//*****************************************************************************************************************************************************//
	//Render items
  	private static void itemResourceLoad(Item item){
  		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName() , "inventory"));
  	}
  	//Render block
  	private static void bolckResourceLoad(Block block){
  		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName() , "inventory"));
  	}
  	//Unordered synthetic method
  	private static void addShapelessRecipe(String nameStr, String groupStr, ItemStack itemStack, Object... items){
  		Ingredient[] ingredient = new Ingredient[items.length];
  		for (int i = 0; i < items.length; ++i){
  			if(items[i] instanceof Block) {
  	  			ingredient[i] = Ingredient.fromStacks(new ItemStack(Item.getItemFromBlock((Block)items[i]),1));
  			}else {
  	  			ingredient[i] = Ingredient.fromStacks(new ItemStack((Item)items[i],1));
  			}
        }
  		GameRegistry.addShapelessRecipe(new ResourceLocation(nameStr), new ResourceLocation(groupStr), itemStack, ingredient);
  	}
	//Orderly synthesis method
	private static void addRecipe(String nameStr, String groupStr, ItemStack itemStack, Object item1,Object item2,Object item3,Object item4,Object item5,Object item6,Object item7,Object item8,Object item9){
		Object[] object = new Object[]{"012", "345", "678",
			Character.valueOf('0'), null == item1 ? Items.AIR : item1, Character.valueOf('1'), null == item2 ? Items.AIR : item2,
			Character.valueOf('2'), null == item3 ? Items.AIR : item3, Character.valueOf('3'), null == item4 ? Items.AIR : item4,
			Character.valueOf('4'), null == item5 ? Items.AIR : item5, Character.valueOf('5'), null == item6 ? Items.AIR : item6,
			Character.valueOf('6'), null == item7 ? Items.AIR : item7, Character.valueOf('7'), null == item8 ? Items.AIR : item8,
			Character.valueOf('8'), null == item9 ? Items.AIR : item9,
		};
		GameRegistry.addShapedRecipe(new ResourceLocation(nameStr), new ResourceLocation(groupStr), itemStack, object);
	}
	//Burned
	private static void addSmelting(Object item, ItemStack itemStack, float xp){
		if(item instanceof Item)
			GameRegistry.addSmelting((Item)item, itemStack, xp);
		else if(item instanceof Block)
			GameRegistry.addSmelting((Block)item, itemStack, xp);
	}
}
