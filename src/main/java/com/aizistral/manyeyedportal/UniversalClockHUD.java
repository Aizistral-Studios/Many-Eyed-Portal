package com.aizistral.manyeyedportal;

import com.aizistral.manyeyedportal.handlers.ConfigHandler;
import com.aizistral.manyeyedportal.handlers.PortalEventHandler;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(UniversalClockHUD.MODID)
public class UniversalClockHUD {
	public static final String MODID = "manyeyedportal";

	public UniversalClockHUD() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new PortalEventHandler());

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.getConfig(), "ManyEyedPortal.toml");
	}

	private void onLoadComplete(FMLLoadCompleteEvent event) {
		// NO-OP
	}

	private void setup(FMLCommonSetupEvent event) {
		// NO-OP
	}

	private void doClientStuff(FMLClientSetupEvent event) {
		// NO-OP
	}

	private void enqueueIMC(InterModEnqueueEvent event) {
		// NO-OP
	}

	private void processIMC(InterModProcessEvent event) {
		// NO-OP
	}

}
