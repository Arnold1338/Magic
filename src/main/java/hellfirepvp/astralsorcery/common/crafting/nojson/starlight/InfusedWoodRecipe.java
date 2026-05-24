package hellfirepvp.astralsorcery.common.crafting.nojson.starlight;

import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.item.ItemEntity;
import hellfirepvp.astralsorcery.common.data.config.entry.CraftingConfig;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Collections;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.List;
import hellfirepvp.astralsorcery.AstralSorcery;

public class InfusedWoodRecipe extends LiquidStarlightRecipe
{
    public InfusedWoodRecipe() {
        super(AstralSorcery.key("infused_wood"));
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public List<Ingredient> getInputForRender() {
        return Collections.singletonList(Ingredient.func_199805_a((ITag)ItemTags.field_200038_h));
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public List<Ingredient> getOutputForRender() {
        return Collections.singletonList(Ingredient.func_199804_a(new ItemLike[] { (ItemLike)BlocksAS.INFUSED_WOOD }));
    }
    
    @Override
    public boolean doesStartRecipe(final ItemStack item) {
        return (boolean)CraftingConfig.CONFIG.liquidStarlightDropInfusedWood.get() && item.getItem().func_206844_a((ITag)ItemTags.field_200038_h);
    }
    
    @Override
    public boolean matches(final ItemEntity trigger, final World world, final BlockPos at) {
        return true;
    }
    
    @Override
    public void doServerCraftTick(final ItemEntity trigger, final World world, final BlockPos at) {
        if (this.getAndIncrementCraftingTick((Entity)trigger) > 5 && this.consumeItemEntityInBlock((IWorld)world, at, 1, stack -> !stack.isEmpty() && stack.getItem().func_206844_a((ITag)ItemTags.field_200038_h)) != null) {
            ItemUtils.dropItemNaturally(world, trigger.func_226277_ct_(), trigger.func_226278_cu_(), trigger.func_226281_cx_(), new ItemStack((ItemLike)BlocksAS.INFUSED_WOOD));
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void doClientEffectTick(final ItemEntity trigger, final World world, final BlockPos at) {
        for (int i = 0; i < 4; ++i) {
            final Vector3 pos = new Vector3((Vector3i)at).add(0.5, 0.5, 0.5);
            MiscUtils.applyRandomOffset(pos, InfusedWoodRecipe.rand, 0.5f);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).color(VFXColorFunction.constant(ColorsAS.DYE_BROWN)).alpha(VFXAlphaFunction.PYRAMID).setScaleMultiplier(0.1f + InfusedWoodRecipe.rand.nextFloat() * 0.1f).setMaxAge(30 + InfusedWoodRecipe.rand.nextInt(20));
        }
    }
}
