/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import forestry.api.core.ForestryAPI;
import forestry.core.EventHandlerCore;
import forestry.core.config.Config;
import forestry.core.config.Constants;
import forestry.core.config.GameMode;
import forestry.core.errors.EnumErrorCode;
import forestry.core.errors.ErrorStateRegistry;
import forestry.core.gui.GuiHandler;
import forestry.core.multiblock.MultiblockEventHandler;
import forestry.core.network.PacketHandler;
import forestry.core.proxy.Proxies;
import forestry.core.worldgen.WorldGenerator;
import forestry.plugins.PluginManager;

/**
 * Forestry Minecraft Mod
 *
 * @author SirSengir
 */
@Mod(
		modid = Constants.MOD_ID,
		name = Constants.MOD_ID,
		version = Constants.VERSION,
		guiFactory = "forestry.core.config.ForestryGuiConfigFactory",
		dependencies = "required-after:Forge@[12.17.0.1922,);"
				+ "after:Buildcraft|Core@[7.2,);"
				+ "after:BuildCraft|Energy@[7.2,);"
				+ "after:ExtrabiomesXL;"
				+ "after:BiomesOPlenty;"
				+ "after:IC2@[2.3.235,);"
				+ "after:Natura@[2.2.0,);"
				+ "after:HardcoreEnderExpansion;"
				+ "after:basemetals;")
public class Forestry {

	@Mod.Instance(Constants.MOD_ID)
	public static Forestry instance;
	private File configFolder;

	public Forestry() {
		ForestryAPI.instance = this;
		ForestryAPI.forestryConstants = new Constants();
		ForestryAPI.errorStateRegistry = new ErrorStateRegistry();
		EnumErrorCode.init();
		FluidRegistry.enableUniversalBucket();
	}

	public static PacketHandler packetHandler;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		packetHandler = new PacketHandler();

		// Register event handler
		EventHandlerCore eventHandlerCore = new EventHandlerCore();
		MinecraftForge.EVENT_BUS.register(eventHandlerCore);
		MinecraftForge.EVENT_BUS.register(new MultiblockEventHandler());

		configFolder = new File(event.getModConfigurationDirectory(), Constants.RESOURCE_ID);
		Config.load();

		PluginManager.runSetup(event);

		ForestryAPI.activeMode = new GameMode(Config.gameMode);

		PluginManager.runPreInit();
		
		Proxies.render.registerModels();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Register gui handler
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		PluginManager.runInit();

		Proxies.render.registerItemAndBlockColors();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PluginManager.runPostInit();

		// Register world generator
		WorldGenerator worldGenerator = new WorldGenerator();
		GameRegistry.registerWorldGenerator(worldGenerator, 0);

		// Register tick handlers
		Proxies.common.registerTickHandlers(worldGenerator);

		// Handle IMC messages.
		PluginManager.processIMCMessages(FMLInterModComms.fetchRuntimeMessages(ForestryAPI.instance));
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		PluginManager.serverStarting(event.getServer());
	}

	public File getConfigFolder() {
		return configFolder;
	}

	@EventHandler
	public void processIMCMessages(IMCEvent event) {
		PluginManager.processIMCMessages(event.getMessages());
	}
}
