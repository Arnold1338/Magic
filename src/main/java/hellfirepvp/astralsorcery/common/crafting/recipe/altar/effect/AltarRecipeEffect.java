package hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect;

import java.util.Objects;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.Random;


public abstract class AltarRecipeEffect extends ForgeRegistryEntry<AltarRecipeEffect>
{
    protected static final int INDEX_NOISE_PLANE_LAYER1 = 0;
    protected static final int INDEX_NOISE_PLANE_LAYER2 = 1;
    protected static final int INDEX_CRAFT_FLARE = 2;
    protected static final Random rand;
    private static final Vector3[] offsetPillarsT2;
    private static final Vector3[] offsetPillarsT3;
    
    protected static Vector3 getRandomPillarOffset(final AltarType type) {
        switch (type) {
            case ATTUNEMENT: {
                return AltarRecipeEffect.offsetPillarsT2[AltarRecipeEffect.rand.nextInt(AltarRecipeEffect.offsetPillarsT2.length)].clone();
            }
            case CONSTELLATION:
            case RADIANCE: {
                return AltarRecipeEffect.offsetPillarsT3[AltarRecipeEffect.rand.nextInt(AltarRecipeEffect.offsetPillarsT3.length)].clone();
            }
            default: {
                return new Vector3();
            }
        }
    }
    
    protected static Vector3 getPillarOffset(final AltarType type, final int index) {
        switch (type) {
            case ATTUNEMENT: {
                return AltarRecipeEffect.offsetPillarsT2[index % AltarRecipeEffect.offsetPillarsT2.length].clone();
            }
            case CONSTELLATION:
            case RADIANCE: {
                return AltarRecipeEffect.offsetPillarsT3[index % AltarRecipeEffect.offsetPillarsT3.length].clone();
            }
            default: {
                return new Vector3();
            }
        }
    }
    
    protected static int getPillarHeight(final AltarType type) {
        switch (type) {
            case ATTUNEMENT: {
                return 2;
            }
            case CONSTELLATION:
            case RADIANCE: {
                return 3;
            }
            default: {
                return 0;
            }
        }
    }
    
    protected static int getPillarAmount(final AltarType type) {
        if (type == AltarType.DISCOVERY) {
            return 0;
        }
        return 4;
    }
    
    protected static Vector3 getFocusRelayOffset(final AltarType type) {
        switch (type) {
            case RADIANCE: {
                return new Vector3(0.0, 4.5, 0.0);
            }
            default: {
                return new Vector3();
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    protected long getClientTick() {
        return ClientScheduler.getClientTick();
    }
    
    @OnlyIn(Dist.CLIENT)
    public abstract void onTick(final TileAltar p0, final ActiveSimpleAltarRecipe.CraftingState p1);
    
    @OnlyIn(Dist.CLIENT)
    public abstract void onTESR(final TileAltar p0, final ActiveSimpleAltarRecipe.CraftingState p1, final PoseStack p2, final MultiBufferSource p3, final float p4, final int p5);
    
    @OnlyIn(Dist.CLIENT)
    public abstract void onCraftingFinish(final TileAltar p0, final boolean p1);
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final AltarRecipeEffect that = (AltarRecipeEffect)o;
        return this.getRegistryName().equals((Object)that.getRegistryName());
    }
    
    public int hashCode() {
        return Objects.hash(this.getRegistryName());
    }
    
    static {
        rand = new Random();
        offsetPillarsT2 = new Vector3[] { new Vector3(2, 0, 2), new Vector3(-2, 0, 2), new Vector3(2, 0, -2), new Vector3(-2, 0, -2) };
        offsetPillarsT3 = new Vector3[] { new Vector3(3, 0, 3), new Vector3(-3, 0, 3), new Vector3(3, 0, -3), new Vector3(-3, 0, -3) };
    }
}
