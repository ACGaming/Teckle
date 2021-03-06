/*
 *    Copyright 2017 Benjamin K (darkevilmac)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.elytradev.teckle.common.item;

import com.elytradev.teckle.common.TeckleMod;
import com.elytradev.teckle.common.TeckleObjects;
import com.elytradev.teckle.common.tile.TileItemTube;
import com.elytradev.teckle.repack.concrete.resgen.EnumResourceType;
import com.elytradev.teckle.repack.concrete.resgen.IResourceHolder;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class ItemPaintbrush extends Item implements IResourceHolder {

    public ItemPaintbrush() {
        setMaxStackSize(1);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + EnumDyeColor.byDyeDamage(stack.getMetadata()).getUnlocalizedName();
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            subItems.add(new ItemStack(itemIn, 1, color.getDyeDamage()));
        }
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItem(hand);

        if (!player.canPlayerEdit(pos, facing, itemstack)) {
            return EnumActionResult.FAIL;
        }

        if (Objects.equals(worldIn.getBlockState(pos).getBlock(), TeckleObjects.blockItemTube)) {
            TileItemTube tubeItem = (TileItemTube) worldIn.getTileEntity(pos);

            if (tubeItem != null) {
                tubeItem.setColour(EnumDyeColor.byDyeDamage(getMetadata(itemstack)));
                worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 4);
            }
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public ResourceLocation getResource(EnumResourceType resourceType, int meta) {
        EnumDyeColor type = EnumDyeColor.byDyeDamage(meta);

        return new ResourceLocation(TeckleMod.RESOURCE_DOMAIN + "items/paintbrush_" + type.getName());
    }
}
