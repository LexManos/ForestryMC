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
package forestry.apiculture.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.apiculture.IArmorApiarist;
import forestry.api.core.IArmorNaturalist;
import forestry.api.core.IItemModelRegister;
import forestry.api.core.IModelManager;
import forestry.api.core.Tabs;
import forestry.apiculture.PluginApiculture;
import forestry.core.config.Constants;

public class ItemArmorApiarist extends ItemArmor implements IArmorApiarist, IArmorNaturalist, IItemModelRegister {

	public ItemArmorApiarist(EntityEquipmentSlot equipmentSlotIn) {
		super(ArmorMaterial.LEATHER, 0, equipmentSlotIn);
		this.setMaxDamage(100);
		setCreativeTab(Tabs.tabApiculture);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		if (stack != null && stack.getItem() == PluginApiculture.items.apiaristLegs) {
			return Constants.RESOURCE_ID + ":" + Constants.TEXTURE_APIARIST_ARMOR_SECONDARY;
		} else {
			return Constants.RESOURCE_ID + ":" + Constants.TEXTURE_APIARIST_ARMOR_PRIMARY;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel(Item item, IModelManager manager) {
		manager.registerItemModel(item, 0);
	}

	@Override
	public boolean hasColor(ItemStack itemstack) {
		return false;
	}

	@Override
	public boolean protectEntity(EntityLivingBase entity, ItemStack armor, String cause, boolean doProtect) {
		return true;
	}

	@Override
	public boolean canSeePollination(EntityPlayer player, ItemStack armor, boolean doSee) {
		return armorType == EntityEquipmentSlot.HEAD;
	}

}
