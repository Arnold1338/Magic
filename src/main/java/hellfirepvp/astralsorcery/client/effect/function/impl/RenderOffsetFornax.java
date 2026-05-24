package hellfirepvp.astralsorcery.client.effect.function.impl;

import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import java.util.Random;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import hellfirepvp.astralsorcery.client.effect.function.VFXRenderOffsetFunction;

public class RenderOffsetFornax implements VFXRenderOffsetFunction<EntityVisualFX>
{
    @Nonnull
    @Override
    public Vector3 changeRenderPosition(@Nonnull final EntityVisualFX fx, final Vector3 interpolatedPos, final float pTicks) {
        final Vector3 currentMotion = fx.getMotion();
        final Vector3 perp = currentMotion.clone().perpendicular().normalize().multiply(0.05);
        final Random r = new Random(fx.getId());
        final int interv = (int)((r.nextInt() + ClientScheduler.getClientTick()) % 9L);
        final float part = interv + pTicks;
        final float perc = part / 10.0f;
        final float sinPart = Mth.func_76126_a(perc * 3.1415927f * 2.0f);
        return interpolatedPos.add(perp.rotate(r.nextFloat() * 360.0f, currentMotion).multiply(sinPart));
    }
}
