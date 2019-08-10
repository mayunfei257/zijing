package com.zijing;

import com.zijing.blocks.BlockGuhuaNiunaiKuai;
import com.zijing.blocks.BlockSuperNangua;
import com.zijing.blocks.BlockZijingKuai;
import com.zijing.blocks.BlockZilingCao;
import com.zijing.blocks.tool.BlockZhulingTai;
import com.zijing.blocks.tool.BlockZilingMieshaZhen;
import com.zijing.blocks.tool.BlockZilingZhaohuanZhen;
import com.zijing.data.message.ChuansongBookToServerMessage;
import com.zijing.data.message.ChuansongCardToServerMessage;
import com.zijing.data.message.ClientToServerMessage;
import com.zijing.data.message.OpenClientGUIMessage;
import com.zijing.data.message.OpenServerGUIMessage;
import com.zijing.data.message.ShepherdEntityToClientMessage;
import com.zijing.data.message.ShepherdToClientMessage;
import com.zijing.data.message.UpgradeToServerMessage;
import com.zijing.data.playerdata.ShepherdCapability;
import com.zijing.entity.EntityArrowBingDan;
import com.zijing.entity.EntityArrowFengyinDan;
import com.zijing.entity.EntityArrowHuoDan;
import com.zijing.entity.EntityArrowXukongDan;
import com.zijing.entity.EntityDisciple;
import com.zijing.entity.EntitySuperIronGolem;
import com.zijing.entity.EntitySuperSnowman;
import com.zijing.entity.TileEntityZhulingTai;
import com.zijing.entity.render.RenderDisciple;
import com.zijing.entity.render.RenderSuperIronGolem;
import com.zijing.entity.render.RenderSuperSnowman;
import com.zijing.items.ItemDanShenshu;
import com.zijing.items.ItemDanZiling;
import com.zijing.items.ItemGuhuaNiunai;
import com.zijing.items.ItemQiankunDai;
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
import com.zijing.items.staff.ItemStaffKongjian;
import com.zijing.items.staff.ItemStaffLieyan;
import com.zijing.items.staff.ItemStaffShijian;
import com.zijing.items.staff.ItemZilingZhu;
import com.zijing.items.tool.ItemArmorZijingBody;
import com.zijing.items.tool.ItemArmorZijingBoots;
import com.zijing.items.tool.ItemArmorZijingHelmet;
import com.zijing.items.tool.ItemArmorZijingLegs;
import com.zijing.items.tool.ItemToolZijingChan;
import com.zijing.items.tool.ItemToolZijingChu;
import com.zijing.items.tool.ItemToolZijingDun;
import com.zijing.items.tool.ItemToolZijingFu;
import com.zijing.items.tool.ItemToolZijingGao;
import com.zijing.items.tool.ItemToolZijingJian;
import com.zijing.util.ConstantUtil;
import com.zijing.waigua.ItemStaffBuilding;
import com.zijing.waigua.ItemWuxianBaoshi;
import com.zijing.waigua.world.BlockPortalLongjie;
import com.zijing.waigua.world.ItemTriggerLongshi;

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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.GameData;

public class BaseControl{
    private static int nextID = 0;
	public static SimpleNetworkWrapper netWorkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(ConstantUtil.MODID);
	public static DimensionType dtype;
	
	//TODO Instantiate mod item ---
	public static Block blockGuhuaNiunaiKuai;
	public static Block blockZilingCao;
	public static Block blockZijingKuai;
	public static Block blockZilingMieshaZhen;
	public static Block blockZilingZhaohuanZhen;
	public static Block blockSuperNangua;
	public static Block blockZhulingTai;
	
	//item
	public static Item itemGuhuaNiunai;
	public static Item itemZiqi;
	public static Item itemZijing;
	public static Item itemDanZiling;
	public static Item itemDanShenshu;
	public static ItemZilingZhu itemZilingZhu;
	public static Item itemQiankunDai;
	//card
	public static Item itemCardFengyin;
	public static Item itemCardChuansong;
	public static Item itemBookChuansong;
	//staff
	public static ItemStaffBingxue itemStaffBingxue;
	public static ItemStaffLieyan itemStaffLieyan;
	public static ItemStaffKongjian itemStaffKongjian;
	public static ItemStaffShijian itemStaffShijian;
	public static Item itemArrowBingDan;
	public static Item itemArrowHuoDan;
	public static Item itemArrowXukongDan;
	public static Item itemArrowFengyinDan;
	//tool
	public static Item itemToolZijingJian;
	public static Item itemToolZijingFu;
	public static Item itemToolZijingGao;
	public static Item itemToolZijingChan;
	public static Item itemToolZijingChu;
	public static Item itemToolZijingDun;
	public static Item itemArmorZijingHelmet;
	public static Item itemArmorZijingBody;
	public static Item itemArmorZijingLegs;
	public static Item itemArmorZijingBoots;
	//waigua
	public static Item itemStaffBuilding;
	public static Item itemWuxianBaoshi;
	public static BlockPortalLongjie blockPortalLongjie;
	public static ItemTriggerLongshi itemTriggerLongshi;

	public static void init(FMLPreInitializationEvent event){
//    	dtype = DimensionType.register("Longjie", "_Longjie", ZijingMod.config.getLONGJIE_DIMID(), DimensionLongjie.WorldProviderMod.class, false);
    	
		//TODO Instantiate mod item ---
		blockGuhuaNiunaiKuai = new BlockGuhuaNiunaiKuai();
		blockZilingCao = new BlockZilingCao();
		blockZijingKuai = new BlockZijingKuai();
		blockZilingMieshaZhen = new BlockZilingMieshaZhen();
		blockZilingZhaohuanZhen = new BlockZilingZhaohuanZhen();
		blockSuperNangua = new BlockSuperNangua();
		blockZhulingTai = new BlockZhulingTai(false);
		//item
		itemGuhuaNiunai = new ItemGuhuaNiunai();
		itemZiqi = new ItemZiqi();
		itemZijing = new ItemZijing();
		itemDanZiling = new ItemDanZiling();
		itemDanShenshu = new ItemDanShenshu();
		itemZilingZhu = new ItemZilingZhu();
		itemQiankunDai = new ItemQiankunDai();
		//card
		itemCardFengyin = new ItemCardFengyin();
		itemCardChuansong = new ItemCardChuansong();
		itemBookChuansong = new ItemBookChuansong();
		//staff
		itemStaffBingxue = new ItemStaffBingxue();
		itemStaffLieyan = new ItemStaffLieyan();
		itemStaffKongjian = new ItemStaffKongjian();
		itemStaffShijian = new ItemStaffShijian();
		itemArrowBingDan = new ItemArrowBingDan();
		itemArrowHuoDan = new ItemArrowHuoDan();
		itemArrowXukongDan = new ItemArrowXukongDan();
		itemArrowFengyinDan = new ItemArrowFengyinDan();
		//tool
		itemToolZijingJian = new ItemToolZijingJian();
		itemToolZijingFu = new ItemToolZijingFu();
		itemToolZijingGao = new ItemToolZijingGao();
		itemToolZijingChan = new ItemToolZijingChan();
		itemToolZijingChu = new ItemToolZijingChu();
		itemToolZijingDun = new ItemToolZijingDun();
		itemArmorZijingHelmet = new ItemArmorZijingHelmet();
		itemArmorZijingBody = new ItemArmorZijingBody();
		itemArmorZijingLegs = new ItemArmorZijingLegs();
		itemArmorZijingBoots = new ItemArmorZijingBoots();
		
		//waigua
		itemStaffBuilding = new ItemStaffBuilding();
		itemWuxianBaoshi = new ItemWuxianBaoshi();
		blockPortalLongjie = new BlockPortalLongjie();
		itemTriggerLongshi = new ItemTriggerLongshi();
	}
	
	public static void register(FMLPreInitializationEvent event){
		//TODO In this registration items and blocks ---
		CapabilityManager.INSTANCE.register(ShepherdCapability.class, ShepherdCapability.storage, ShepherdCapability.class);
    	netWorkWrapper.registerMessage(ChuansongCardToServerMessage.Handler.class, ChuansongCardToServerMessage.class, nextID++, Side.SERVER);
    	netWorkWrapper.registerMessage(ChuansongBookToServerMessage.Handler.class, ChuansongBookToServerMessage.class, nextID++, Side.SERVER);
    	netWorkWrapper.registerMessage(ShepherdToClientMessage.Handler.class, ShepherdToClientMessage.class, nextID++, Side.CLIENT);
    	netWorkWrapper.registerMessage(UpgradeToServerMessage.Handler.class, UpgradeToServerMessage.class, nextID++, Side.SERVER);
    	netWorkWrapper.registerMessage(OpenClientGUIMessage.Handler.class, OpenClientGUIMessage.class, nextID++, Side.CLIENT);
    	netWorkWrapper.registerMessage(ShepherdEntityToClientMessage.Handler.class, ShepherdEntityToClientMessage.class, nextID++, Side.CLIENT);
    	netWorkWrapper.registerMessage(OpenServerGUIMessage.Handler.class, OpenServerGUIMessage.class, nextID++, Side.SERVER);
    	netWorkWrapper.registerMessage(ClientToServerMessage.Handler.class, ClientToServerMessage.class, nextID++, Side.SERVER);
    	
    	try {
//    		DimensionManager.registerDimension(ZijingMod.config.getLONGJIE_DIMID(), dtype);
    	}catch(IllegalArgumentException e) {
    		throw new IllegalArgumentException(String.format("Failed to register dimension for id %d, One is already registered. Suggested change to %d .", ZijingMod.config.getLONGJIE_DIMID(), DimensionManager.getNextFreeDimId()));
    	}
//		ForgeRegistries.BLOCKS.register(blockGuhuaNiunaiKuai);
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
		GameData.register_impl(blockSuperNangua);
		GameData.register_impl(new ItemBlock(blockSuperNangua).setRegistryName(blockSuperNangua.getRegistryName()));
		GameData.register_impl(blockZhulingTai);
		GameData.register_impl(new ItemBlock(blockZhulingTai).setRegistryName(blockZhulingTai.getRegistryName()));

//		ForgeRegistries.ITEMS.register(itemGuhuaNiunai);
		//item
		GameData.register_impl(itemGuhuaNiunai);
		GameData.register_impl(itemZiqi);
		GameData.register_impl(itemZijing);
		GameData.register_impl(itemDanZiling);
		GameData.register_impl(itemDanShenshu);
		GameData.register_impl(itemZilingZhu);
		GameData.register_impl(itemQiankunDai);
		//card
		GameData.register_impl(itemCardFengyin);
		GameData.register_impl(itemCardChuansong);
		GameData.register_impl(itemBookChuansong);
		//staff
		GameData.register_impl(itemStaffBingxue);
		GameData.register_impl(itemStaffLieyan);
		GameData.register_impl(itemStaffKongjian);
		GameData.register_impl(itemStaffShijian);
		GameData.register_impl(itemArrowBingDan);
		GameData.register_impl(itemArrowHuoDan);
		GameData.register_impl(itemArrowXukongDan);
		GameData.register_impl(itemArrowFengyinDan);
		//tool
		GameData.register_impl(itemToolZijingJian);
		GameData.register_impl(itemToolZijingFu);
		GameData.register_impl(itemToolZijingGao);
		GameData.register_impl(itemToolZijingChan);
		GameData.register_impl(itemToolZijingChu);
		GameData.register_impl(itemToolZijingDun);
		GameData.register_impl(itemArmorZijingHelmet);
		GameData.register_impl(itemArmorZijingBody);
		GameData.register_impl(itemArmorZijingLegs);
		GameData.register_impl(itemArmorZijingBoots);

		//waigua
		GameData.register_impl(itemStaffBuilding);
		GameData.register_impl(itemWuxianBaoshi);
		GameData.register_impl(blockPortalLongjie);
		GameData.register_impl(new ItemBlock(blockPortalLongjie).setRegistryName(blockPortalLongjie.getRegistryName()));
		GameData.register_impl(itemTriggerLongshi);
		
		EntityRegistry.registerModEntity(new ResourceLocation(ConstantUtil.MODID + ":entityarrowbingdan"), EntityArrowBingDan.class, "entityArrowBingDan", 257, ZijingMod.instance, 64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(ConstantUtil.MODID + ":entityarrowhuodan"), EntityArrowHuoDan.class, "entityArrowHuoDan", 258, ZijingMod.instance, 64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(ConstantUtil.MODID + ":entityarrowxukongdan"), EntityArrowXukongDan.class, "entityArrowXukongDan", 259, ZijingMod.instance, 64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(ConstantUtil.MODID + ":entityarrowfengyindan"), EntityArrowFengyinDan.class, "entityArrowFengyinDan", 260, ZijingMod.instance, 64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(ConstantUtil.MODID + ":entitydisciple"), EntityDisciple.class, "entityDisciple", 261, ZijingMod.instance,64, 1, true, (204 << 16) + (0 << 8) + 204, (255 << 16) + (102 << 8) + 255);
		EntityRegistry.registerModEntity(new ResourceLocation(ConstantUtil.MODID + ":entitysuperirongolem"), EntitySuperIronGolem.class, "entitySuperIronGolem", 262, ZijingMod.instance,64, 1, true, (204 << 16) + (0 << 8) + 204, (255 << 16) + (102 << 8) + 255);
		EntityRegistry.registerModEntity(new ResourceLocation(ConstantUtil.MODID + ":entitysupersnowman"), EntitySuperSnowman.class, "entitySuperSnowman", 263, ZijingMod.instance,64, 1, true, (204 << 16) + (0 << 8) + 204, (255 << 16) + (102 << 8) + 255);
	}
    
    public static void resourceLoad(FMLPreInitializationEvent event){
    	//TODO Register Rendering ---
		bolckResourceLoad(blockGuhuaNiunaiKuai);
		bolckResourceLoad(blockZilingCao);
		bolckResourceLoad(blockZijingKuai);
		bolckResourceLoad(blockZilingMieshaZhen);
		bolckResourceLoad(blockZilingZhaohuanZhen);
		bolckResourceLoad(blockSuperNangua);
		bolckResourceLoad(blockZhulingTai);
		//item
		itemResourceLoad(itemGuhuaNiunai);
		itemResourceLoad(itemZiqi);
		itemResourceLoad(itemZijing);
		itemResourceLoad(itemDanZiling);
		itemResourceLoad(itemDanShenshu);
		itemResourceLoad(itemZilingZhu);
		itemResourceLoad(itemQiankunDai);
		//card
		itemResourceLoad(itemCardFengyin);
		itemResourceLoad(itemCardChuansong);
		itemResourceLoad(itemBookChuansong);
		//staff
		itemResourceLoad(itemStaffBingxue);
		itemResourceLoad(itemStaffLieyan);
		itemResourceLoad(itemStaffKongjian);
		itemResourceLoad(itemStaffShijian);
		itemResourceLoad(itemArrowBingDan);
		itemResourceLoad(itemArrowHuoDan);
		itemResourceLoad(itemArrowXukongDan);
		itemResourceLoad(itemArrowFengyinDan);
		//tool
		itemResourceLoad(itemToolZijingJian);
		itemResourceLoad(itemToolZijingFu);
		itemResourceLoad(itemToolZijingGao);
		itemResourceLoad(itemToolZijingChan);
		itemResourceLoad(itemToolZijingChu);
		itemResourceLoad(itemToolZijingDun);
		itemResourceLoad(itemArmorZijingHelmet);
		itemResourceLoad(itemArmorZijingBody);
		itemResourceLoad(itemArmorZijingLegs);
		itemResourceLoad(itemArmorZijingBoots);
		//waigua
		itemResourceLoad(itemStaffBuilding);
		itemResourceLoad(itemWuxianBaoshi);
		bolckResourceLoad(blockPortalLongjie);
		itemResourceLoad(itemTriggerLongshi);
    }
	
	public static void registerRecipe(FMLInitializationEvent event){
		//TODO Register synthetic methods and burn
		addShapelessRecipe(ConstantUtil.MODID + ":HC_itemGuhuaNiunai", ConstantUtil.MODID, new ItemStack(itemGuhuaNiunai, 1), Items.MILK_BUCKET);
		addShapelessRecipe(ConstantUtil.MODID + ":FJ_blockGuhuaNiunaiKuai", ConstantUtil.MODID, new ItemStack(itemGuhuaNiunai, 9), blockGuhuaNiunaiKuai);
		addShapelessRecipe(ConstantUtil.MODID + ":FJ_itemGuhuaNiunai", ConstantUtil.MODID, new ItemStack(Items.MILK_BUCKET, 1), itemGuhuaNiunai, Items.BUCKET);
		addRecipe(ConstantUtil.MODID + ":HC_blockGuhuaNiunaiKuai", ConstantUtil.MODID, new ItemStack(blockGuhuaNiunaiKuai, 1), itemGuhuaNiunai, itemGuhuaNiunai, itemGuhuaNiunai, itemGuhuaNiunai, itemGuhuaNiunai, itemGuhuaNiunai, itemGuhuaNiunai, itemGuhuaNiunai, itemGuhuaNiunai);
		
		addSmelting(blockZilingCao, new ItemStack(itemZiqi, 1), 0.5F);
		addShapelessRecipe(ConstantUtil.MODID + ":FJ_itemZijing", ConstantUtil.MODID, new ItemStack(itemZiqi, 9), itemZijing);
		addShapelessRecipe(ConstantUtil.MODID + ":FJ_blockZijingKuai", ConstantUtil.MODID, new ItemStack(itemZijing, 9), blockZijingKuai);
		addRecipe(ConstantUtil.MODID + ":HC_itemZijing", ConstantUtil.MODID, new ItemStack(itemZijing, 1), itemZiqi, itemZiqi, itemZiqi, itemZiqi, itemZiqi, itemZiqi, itemZiqi, itemZiqi, itemZiqi);
		addRecipe(ConstantUtil.MODID + ":HC_blockZijingKuai", ConstantUtil.MODID, new ItemStack(blockZijingKuai, 1), itemZijing, itemZijing, itemZijing, itemZijing, itemZijing, itemZijing, itemZijing, itemZijing, itemZijing);
		//block
		addRecipe(ConstantUtil.MODID + ":HC_blockSuperNangua1", ConstantUtil.MODID, new ItemStack(blockSuperNangua, 1), itemZijing, itemZijing, itemZijing, itemZijing, Blocks.PUMPKIN, itemZijing, itemZijing, itemZijing, itemZijing);
		addRecipe(ConstantUtil.MODID + ":HC_blockSuperNangua2", ConstantUtil.MODID, new ItemStack(blockSuperNangua, 1), itemZijing, itemZijing, itemZijing, itemZijing, Blocks.LIT_PUMPKIN, itemZijing, itemZijing, itemZijing, itemZijing);
		addRecipe(ConstantUtil.MODID + ":HC_blockZilingMieshaZhen", ConstantUtil.MODID, new ItemStack(blockZilingMieshaZhen, 1), itemZiqi, itemZiqi, itemZiqi, Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, itemToolZijingJian, Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, itemZijing, itemZijing, itemZijing);
		addRecipe(ConstantUtil.MODID + ":HC_blockZilingZhaohuanZhen", ConstantUtil.MODID, new ItemStack(blockZilingZhaohuanZhen, 1), itemZijing, Items.ENDER_PEARL, itemZijing, blockZijingKuai, Blocks.PUMPKIN, blockZijingKuai, blockZijingKuai, Blocks.IRON_BLOCK, blockZijingKuai);
		addRecipe(ConstantUtil.MODID + ":HC_blockZhulingTai", ConstantUtil.MODID, new ItemStack(blockZhulingTai, 1), itemZijing, Blocks.CAULDRON, itemZijing, blockZijingKuai, Blocks.CRAFTING_TABLE, blockZijingKuai, blockZijingKuai, Blocks.ANVIL, blockZijingKuai);
		//item and food
		addShapelessRecipe(ConstantUtil.MODID + ":HC_itemDanZiling1", ConstantUtil.MODID, new ItemStack(itemDanZiling, 2), Items.WHEAT, itemZiqi, Items.WHEAT);
		addRecipe(ConstantUtil.MODID + ":HC_itemDanShenshu1", ConstantUtil.MODID, new ItemStack(itemDanShenshu, 4), Items.WHEAT_SEEDS, Items.WHEAT_SEEDS, Items.WHEAT_SEEDS, Items.WHEAT_SEEDS, itemZiqi, Items.WHEAT_SEEDS, Items.WHEAT_SEEDS, Items.WHEAT_SEEDS, Items.WHEAT_SEEDS);
		addRecipe(ConstantUtil.MODID + ":HC_itemDanShenshu2", ConstantUtil.MODID, new ItemStack(itemDanShenshu, 4), Items.BEETROOT_SEEDS, Items.BEETROOT_SEEDS, Items.BEETROOT_SEEDS, Items.BEETROOT_SEEDS, itemZiqi, Items.BEETROOT_SEEDS, Items.BEETROOT_SEEDS, Items.BEETROOT_SEEDS, Items.BEETROOT_SEEDS);
		addRecipe(ConstantUtil.MODID + ":HC_itemDanShenshu3", ConstantUtil.MODID, new ItemStack(itemDanShenshu, 4), Items.MELON_SEEDS, Items.MELON_SEEDS, Items.MELON_SEEDS, Items.MELON_SEEDS, itemZiqi, Items.MELON_SEEDS, Items.MELON_SEEDS, Items.MELON_SEEDS, Items.MELON_SEEDS);
		addRecipe(ConstantUtil.MODID + ":HC_itemDanShenshu4", ConstantUtil.MODID, new ItemStack(itemDanShenshu, 4), Items.PUMPKIN_SEEDS, Items.PUMPKIN_SEEDS, Items.PUMPKIN_SEEDS, Items.PUMPKIN_SEEDS, itemZiqi, Items.PUMPKIN_SEEDS, Items.PUMPKIN_SEEDS, Items.PUMPKIN_SEEDS, Items.PUMPKIN_SEEDS);
		addRecipe(ConstantUtil.MODID + ":HC_itemQiankunDai", ConstantUtil.MODID, new ItemStack(itemQiankunDai, 1), Items.STRING, Items.LEATHER, Items.STRING, Items.LEATHER, itemZijing, Items.LEATHER, Items.STRING, Items.LEATHER, Items.STRING);
		//card
		addRecipe(ConstantUtil.MODID + ":HC_itemCardChuansong", ConstantUtil.MODID, new ItemStack(itemCardChuansong, 1), itemZiqi, Items.PAPER, itemZiqi, Items.PAPER, Items.ENDER_PEARL, Items.PAPER, itemZiqi, Items.PAPER, itemZiqi);
		addRecipe(ConstantUtil.MODID + ":HC_itemBookChuansong", ConstantUtil.MODID, new ItemStack(itemBookChuansong, 1), itemZiqi, Items.BOOK, itemZiqi, Items.BOOK, Items.ENDER_PEARL, Items.BOOK, itemZiqi, Items.BOOK, itemZiqi);
		//staff
		addRecipe(ConstantUtil.MODID + ":HC_itemZilingZhu", ConstantUtil.MODID, new ItemStack(itemZilingZhu, 1), Items.DIAMOND, itemZijing, Items.DIAMOND, itemZijing, Items.ENDER_PEARL, itemZijing, Items.DIAMOND, itemZijing, Items.DIAMOND);
		addRecipe(ConstantUtil.MODID + ":HC_itemStaffBingxue", ConstantUtil.MODID, new ItemStack(itemStaffBingxue, 1), null, Blocks.ICE, itemZilingZhu, null, Blocks.ICE, Blocks.ICE, Blocks.ICE, null, null);
		addRecipe(ConstantUtil.MODID + ":HC_itemStaffLieyan", ConstantUtil.MODID, new ItemStack(itemStaffLieyan, 1), null, Blocks.MAGMA, itemZilingZhu, null, Blocks.MAGMA, Blocks.MAGMA, Blocks.MAGMA, null, null);
		addRecipe(ConstantUtil.MODID + ":HC_itemStaffKongjian", ConstantUtil.MODID, new ItemStack(itemStaffKongjian, 1), null, Items.ENDER_EYE, itemZilingZhu, null, Items.ENDER_EYE, Items.ENDER_EYE, Items.ENDER_EYE, null, null);
		addRecipe(ConstantUtil.MODID + ":HC_itemStaffShikong", ConstantUtil.MODID, new ItemStack(itemStaffShijian, 1), null, Blocks.GOLD_BLOCK, itemZilingZhu, null, Blocks.GOLD_BLOCK, Blocks.GOLD_BLOCK, Blocks.GOLD_BLOCK, null, null);
		//tool
		addRecipe(ConstantUtil.MODID + ":HC_itemToolZijingJian", ConstantUtil.MODID, new ItemStack(itemToolZijingJian, 1), null, itemZijing, null, null, itemZijing, null, null, Items.STICK, null);
		addRecipe(ConstantUtil.MODID + ":HC_itemToolZijingFu", ConstantUtil.MODID, new ItemStack(itemToolZijingFu, 1), itemZijing, itemZijing, null, itemZijing, Items.STICK, null, null, Items.STICK, null);
		addRecipe(ConstantUtil.MODID + ":HC_itemToolZijingGao", ConstantUtil.MODID, new ItemStack(itemToolZijingGao, 1), itemZijing, itemZijing, itemZijing, null, Items.STICK, null, null, Items.STICK, null);
		addRecipe(ConstantUtil.MODID + ":HC_itemToolZijingChan", ConstantUtil.MODID, new ItemStack(itemToolZijingChan, 1), null, itemZijing, null, null, Items.STICK, null, null, Items.STICK, null);
		addRecipe(ConstantUtil.MODID + ":HC_itemToolZijingChu", ConstantUtil.MODID, new ItemStack(itemToolZijingChu, 1), itemZijing, itemZijing, null, null, Items.STICK, null, null, Items.STICK, null);
		addRecipe(ConstantUtil.MODID + ":HC_itemToolZijingDun", ConstantUtil.MODID, new ItemStack(itemToolZijingDun, 1), null, itemZiqi, null, itemZiqi, Items.SHIELD, itemZiqi, null, itemZiqi, null);
		addRecipe(ConstantUtil.MODID + ":HC_itemArmorZijingHelmet", ConstantUtil.MODID, new ItemStack(itemArmorZijingHelmet, 1), itemZijing, itemZijing, itemZijing, itemZijing, null, itemZijing, null, null, null);
		addRecipe(ConstantUtil.MODID + ":HC_itemArmorZijingBody", ConstantUtil.MODID, new ItemStack(itemArmorZijingBody, 1), itemZijing, null, itemZijing, itemZijing, itemZijing, itemZijing, itemZijing, itemZijing, itemZijing);
		addRecipe(ConstantUtil.MODID + ":HC_itemArmorZijingLegs", ConstantUtil.MODID, new ItemStack(itemArmorZijingLegs, 1), itemZijing, itemZijing, itemZijing, itemZijing, null, itemZijing, itemZijing, null, itemZijing);
		addRecipe(ConstantUtil.MODID + ":HC_itemArmorZijingBoots", ConstantUtil.MODID, new ItemStack(itemArmorZijingBoots, 1), null, null, null, itemZijing, null, itemZijing, itemZijing, null, itemZijing);
		
		addSmelting(Blocks.GRAVEL, new ItemStack(Items.FLINT, 1), 1);
		addShapelessRecipe(ConstantUtil.MODID + ":HC_GUNPOWDER1", "custom", new ItemStack(Items.GUNPOWDER, 3), new ItemStack(Items.FLINT, 1), new ItemStack(Items.DYE, 1, 15), new ItemStack(Items.COAL, 1));
		addShapelessRecipe(ConstantUtil.MODID + ":HC_GUNPOWDER2", "custom", new ItemStack(Items.GUNPOWDER, 3), new ItemStack(Items.FLINT, 1), new ItemStack(Items.DYE, 1, 15),new ItemStack(Items.COAL, 1, 1));
		addShapelessRecipe(ConstantUtil.MODID + ":FJ_CLAY", "custom", new ItemStack(Items.CLAY_BALL, 4), Blocks.CLAY);
		addShapelessRecipe(ConstantUtil.MODID + ":FJ_QUARTZ_BLOCK", "custom", new ItemStack(Items.QUARTZ, 4), Blocks.QUARTZ_BLOCK);
		addShapelessRecipe(ConstantUtil.MODID + ":FJ_WOOL", "custom", new ItemStack(Items.STRING, 4), Blocks.WOOL);
		addShapelessRecipe(ConstantUtil.MODID + ":FJ_GLOWSTONE", "custom", new ItemStack(Items.GLOWSTONE_DUST, 4), Blocks.GLOWSTONE);
		addShapelessRecipe(ConstantUtil.MODID + ":FJ_MELON_BLOCK", "custom", new ItemStack(Items.MELON, 9), Blocks.MELON_BLOCK);
		addShapelessRecipe(ConstantUtil.MODID + ":FJ_LEATHER", "custom", new ItemStack(Items.RABBIT_HIDE, 4), Items.LEATHER);
		addShapelessRecipe(ConstantUtil.MODID + ":FJ_NETHER_WART_BLOCK", "custom", new ItemStack(Items.NETHER_WART, 9), Blocks.NETHER_WART_BLOCK);
		addShapelessRecipe(ConstantUtil.MODID + ":FJ_SNOW_BLOCK", "custom", new ItemStack(Items.SNOWBALL, 4), Blocks.SNOW);
		addShapelessRecipe(ConstantUtil.MODID + ":FJ_MAGMA_BLOCK", "custom", new ItemStack(Items.MAGMA_CREAM, 4), Blocks.MAGMA);
		
	}
	
	public static void renderLoad(FMLInitializationEvent event){
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowBingDan.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), itemArrowBingDan, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowHuoDan.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), itemArrowHuoDan, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowXukongDan.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), itemArrowXukongDan, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityArrowFengyinDan.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), itemArrowFengyinDan, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityDisciple.class, new RenderDisciple(Minecraft.getMinecraft().getRenderManager(), true));
		RenderingRegistry.registerEntityRenderingHandler(EntitySuperIronGolem.class, new RenderSuperIronGolem(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntitySuperSnowman.class, new RenderSuperSnowman(Minecraft.getMinecraft().getRenderManager()));
	}

	public static void tileEntityAddMapping(FMLPostInitializationEvent event) {
		TileEntity.register(ConstantUtil.MODID + ":tileEntityZhulingTai", TileEntityZhulingTai.class);
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
  	  			ingredient[i] = Ingredient.fromStacks(new ItemStack(Item.getItemFromBlock((Block)items[i]), 1));
  			}else if(items[i] instanceof Item){
  	  			ingredient[i] = Ingredient.fromStacks(new ItemStack((Item)items[i], 1));
  			}else if(items[i] instanceof ItemStack){
  	  			ingredient[i] = Ingredient.fromStacks((ItemStack)items[i]);
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
			Character.valueOf('8'), null == item9 ? Items.AIR : item9
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
