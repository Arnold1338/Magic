package hellfirepvp.astralsorcery.common.crafting.nojson;

import hellfirepvp.astralsorcery.common.crafting.nojson.attunement.AttuneCrystalRecipe;
import hellfirepvp.astralsorcery.common.crafting.nojson.attunement.AttunePlayerRecipe;
import hellfirepvp.astralsorcery.common.crafting.nojson.attunement.AttunementRecipe;

public class AttunementCraftingRegistry extends CustomRecipeRegistry<AttunementRecipe<?>>
{
    public static final AttunementCraftingRegistry INSTANCE;
    
    @Override
    public void init() {
        ((CustomRecipeRegistry<AttunePlayerRecipe>)this).register(new AttunePlayerRecipe());
        ((CustomRecipeRegistry<AttuneCrystalRecipe>)this).register(new AttuneCrystalRecipe());
    }
    
    static {
        INSTANCE = new AttunementCraftingRegistry();
    }
}
