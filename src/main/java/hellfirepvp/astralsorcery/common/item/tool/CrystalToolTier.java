package hellfirepvp.astralsorcery.common.item.tool;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.IItemTier;

public class CrystalToolTier implements IItemTier
{
    private static final CrystalToolTier INSTANCE;
    
    private CrystalToolTier() {
    }
    
    public static CrystalToolTier getInstance() {
        return CrystalToolTier.INSTANCE;
    }
    
    public int func_200926_a() {
        return 16192;
    }
    
    public float func_200928_b() {
        return 4.5f;
    }
    
    public float func_200929_c() {
        return 3.5f;
    }
    
    public int func_200925_d() {
        return 3;
    }
    
    public int func_200927_e() {
        return 24;
    }
    
    public Ingredient func_200924_f() {
        return Ingredient.field_193370_a;
    }
    
    static {
        INSTANCE = new CrystalToolTier();
    }
}
