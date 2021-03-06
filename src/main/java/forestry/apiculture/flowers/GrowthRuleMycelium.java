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
package forestry.apiculture.flowers;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import forestry.api.genetics.IFlowerGrowthHelper;
import forestry.api.genetics.IFlowerGrowthRule;

public class GrowthRuleMycelium implements IFlowerGrowthRule {

	@Override
	public boolean growFlower(IFlowerGrowthHelper helper, String flowerType, World world, BlockPos pos) {
		if (!world.isAirBlock(pos)) {
			return false;
		}

		Block ground = world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())).getBlock();
		if (ground != Blocks.MYCELIUM) {
			return false;
		}

		return helper.plantRandomFlower(flowerType, world, pos);
	}

}
