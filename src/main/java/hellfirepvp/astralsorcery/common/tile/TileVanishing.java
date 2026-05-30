package hellfirepvp.astralsorcery.common.tile;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.Iterator;
import java.util.List;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.item.armor.ItemMantle;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import net.minecraft.world.phys.AABB;
import hellfirepvp.astralsorcery.common.tile.base.TileEntityTick;

public class TileVanishing extends TileEntityTick
{
    private static final AABB SEARCH_BOX;
    
    public TileVanishing() {
        super(TileEntityTypesAS.VANISHING);
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (!this.getLevel().level() && this.getTicksExisted() % 5 == 0) {
            boolean removeBlock = true;
            final List<Player> players = this.getLevel().func_217357_a((Class)Player.class, TileVanishing.SEARCH_BOX.func_186670_a(this.getBlockState()));
            for (final Player player : players) {
                if (ItemMantle.getEffect((LivingEntity)player, ConstellationsAS.aevitas) != null) {
                    final double yDiff = player.getY() - this.getBlockState().getY();
                    if (player.func_233570_aj_() && yDiff >= 0.95 && yDiff <= 1.15) {
                        if (player.isCrouching()) {

                        }
                        removeBlock = false;
                    }
                    else {
                        if (!player.isCrouching() || yDiff < 0.95 || yDiff > 2.15) {

                        }
                        removeBlock = false;
                    }
                }
            }
            if (removeBlock) {
                this.getLevel().func_217377_a(this.getBlockState(), false);
            }
        }
        if (this.getLevel().level()) {
            this.tickClient();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void tickClient() {
        for (int i = 0; i < 3; ++i) {
            if (TileVanishing.rand.nextFloat() < 0.07f) {
                final Vector3 at = new Vector3((Vec3i)this.field_174879_c).add(0.5f, 0.5f, 0.5f).add(Vector3.random());
                final FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).setScaleMultiplier(0.15f + TileVanishing.rand.nextFloat() * 0.1f).alpha(VFXAlphaFunction.PYRAMID).setMaxAge(40 + TileVanishing.rand.nextInt(10));
                if (TileVanishing.rand.nextBoolean()) {
                    p.color(VFXColorFunction.WHITE);
                }
                else {
                    p.color(VFXColorFunction.constant(ColorsAS.RITUAL_CONSTELLATION_AEVITAS));
                }
            }
        }
    }
    
    static {
        SEARCH_BOX = new AABB(-4.0, 0.0, -4.0, 4.0, 3.0, 4.0);
    }
}
