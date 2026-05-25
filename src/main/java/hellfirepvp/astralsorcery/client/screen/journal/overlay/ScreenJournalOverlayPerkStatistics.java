package hellfirepvp.astralsorcery.client.screen.journal.overlay;

import hellfirepvp.astralsorcery.common.perk.PerkAttributeMap;
import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import hellfirepvp.astralsorcery.common.perk.reader.PerkAttributeReader;
import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import java.util.Map;
import java.awt.Rectangle;
import net.minecraft.network.chat.Component;
import com.google.common.collect.Maps;
import net.minecraft.network.chat.ITextProperties;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import hellfirepvp.astralsorcery.client.util.Blending;
import com.mojang.blaze3d.systems.RenderSystem;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Iterator;
import net.minecraft.world.entity.player.Player;
import java.util.Comparator;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import hellfirepvp.astralsorcery.common.perk.type.vanilla.VanillaPerkAttributeType;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import hellfirepvp.astralsorcery.common.perk.reader.PerkAttributeInterpreter;
import net.minecraft.client.Minecraft;
import java.util.LinkedList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournal;
import hellfirepvp.astralsorcery.common.perk.reader.PerkStatistic;
import java.util.List;

public class ScreenJournalOverlayPerkStatistics extends ScreenJournalOverlay
{
    private static final int HEADER_WIDTH = 190;
    private static final int DEFAULT_WIDTH = 175;
    private final List<PerkStatistic> statistics;
    private int nameStrWidth;
    private int valueStrWidth;
    private int suffixStrWidth;
    
    public ScreenJournalOverlayPerkStatistics(final ScreenJournal origin) {
        super((Component)new Component("screen.astralsorcery.tome.perks.stats"), origin);
        this.statistics = new LinkedList<PerkStatistic>();
        this.nameStrWidth = -1;
        this.valueStrWidth = -1;
        this.suffixStrWidth = -1;
    }
    
    @Override
    protected void func_231160_c_() {
        super.func_231160_c_();
        this.statistics.clear();
        final Player player = (Player)Minecraft.getInstance().player;
        final PerkAttributeInterpreter interpreter = PerkAttributeInterpreter.defaultInterpreter(player);
        RegistriesAS.REGISTRY_PERK_ATTRIBUTE_TYPES.getValues().stream().filter(t -> t instanceof VanillaPerkAttributeType).forEach(t -> ((VanillaPerkAttributeType)t).refreshAttribute(player));
        for (final PerkAttributeType type : RegistriesAS.REGISTRY_PERK_ATTRIBUTE_TYPES.getValues()) {
            if (type.hasTypeApplied(player, LogicalSide.CLIENT)) {
                final PerkStatistic strPerkStat = interpreter.getValue(type);
                if (strPerkStat == null) {
                    continue;
                }
                this.statistics.add(strPerkStat);
            }
        }
        this.statistics.sort(Comparator.comparing(perkStatistic -> I18n.func_135052_a(perkStatistic.getUnlocPerkTypeName(), new Object[0])));
    }
    
    @Override
    public void func_230430_a_(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        super.func_230430_a_(renderStack, mouseX, mouseY, pTicks);
        final float width = 275.0f;
        final float height = 344.0f;
        this.func_230926_e_(150);
        TexturesAS.TEX_GUI_PARCHMENT_BLANK.bindTexture();
        RenderSystem.enableBlend();
        Blending.DEFAULT.apply();
        RenderingGuiUtils.drawRect(renderStack, this.guiLeft + this.guiWidth / 2.0f - width / 2.0f, this.guiTop + this.guiHeight / 2.0f - height / 2.0f, (float)this.getGuiZLevel(), width, height);
        RenderSystem.disableBlend();
        this.func_230926_e_(0);
        this.drawHeader(renderStack);
        this.drawPageText(renderStack, mouseX, mouseY);
    }
    
    private void drawHeader(final PoseStack renderStack) {
        final ITextProperties title = (ITextProperties)new Component("perk.reader.astralsorcery.gui");
        final List<FormattedCharSequence> lines = this.fogColor.func_238425_b_(title, Mth.func_76141_d(135.7143f));
        final int step = 14;
        final float offsetTop = this.guiTop + 15 - lines.size() * step / 2.0f;
        renderStack.popPose();
        renderStack.func_227861_a_(0.0, (double)offsetTop, 0.0);
        for (int i = 0; i < lines.size(); ++i) {
            final FormattedCharSequence line = lines.get(i);
            final float offsetLeft = this.field_230708_k_ / 2.0f - this.fogColor.func_243245_a(line) * 1.4f / 2.0f;
            renderStack.popPose();
            renderStack.func_227861_a_((double)offsetLeft, (double)(i * step), 0.0);
            renderStack.translate(1.4f, 1.4f, 1.0f);
            RenderingDrawUtils.renderStringAt(line, renderStack, this.fogColor, -298634445, false);
            renderStack.scale();
        }
        renderStack.scale();
    }
    
    private void drawPageText(final PoseStack renderStack, final int mouseX, final int mouseY) {
        if (this.nameStrWidth == -1 || this.valueStrWidth == -1 || this.suffixStrWidth == -1) {
            this.buildDisplayWidth();
        }
        final Map<Rectangle, PerkStatistic> valueStrMap = Maps.newHashMap();
        final int offsetY = this.guiTop + 40;
        final int offsetX = this.guiLeft + this.guiWidth / 2 - 87;
        int line = 0;
        for (final PerkStatistic stat : this.statistics) {
            final ITextProperties statName = (ITextProperties)new Component(stat.getUnlocPerkTypeName());
            final List<FormattedCharSequence> statistics = this.fogColor.func_238425_b_(statName, Mth.func_76141_d(126.666664f));
            for (int i = 0; i < statistics.size(); ++i) {
                final FormattedCharSequence statistic = statistics.get(i);
                int drawX = offsetX;
                if (i > 0) {
                    drawX += 10;
                }
                renderStack.popPose();
                renderStack.func_227861_a_((double)drawX, (double)(offsetY + (line + i) * 10), (double)this.getGuiZLevel());
                RenderingDrawUtils.renderStringAt(statistic, renderStack, this.fogColor, -298634445, false);
                renderStack.scale();
            }
            renderStack.popPose();
            renderStack.func_227861_a_((double)(offsetX + this.nameStrWidth), (double)(offsetY + line * 10), (double)this.getGuiZLevel());
            RenderingDrawUtils.renderStringAt((ITextProperties)new Component(stat.getPerkValue()), renderStack, this.fogColor, -298634445, false);
            renderStack.scale();
            final int strLength = this.fogColor.func_78256_a(stat.getPerkValue());
            final Rectangle rctValue = new Rectangle(offsetX + this.nameStrWidth, offsetY + line * 10, strLength, 8);
            valueStrMap.put(rctValue, stat);
            line += statistics.size();
            if (!stat.getSuffix().isEmpty()) {
                renderStack.popPose();
                renderStack.func_227861_a_((double)(offsetX + 25), (double)(offsetY + line * 10), (double)this.getGuiZLevel());
                RenderingDrawUtils.renderStringAt((ITextProperties)new Component(stat.getSuffix()), renderStack, this.fogColor, -298634445, false);
                renderStack.scale();
                ++line;
            }
        }
        for (final Rectangle rct : valueStrMap.keySet()) {
            if (rct.contains(mouseX, mouseY)) {
                final PerkStatistic stat2 = valueStrMap.get(rct);
                this.drawCalculationDescription(renderStack, rct.x + rct.width + 2, rct.y + 15, stat2);
            }
        }
    }
    
    private void drawCalculationDescription(final PoseStack renderStack, final int x, final int y, final PerkStatistic stat) {
        final PerkAttributeType type = stat.getType();
        final PerkAttributeReader reader = type.getReader();
        if (reader == null) {
            return;
        }
        final Player player = (Player)Minecraft.getInstance().player;
        final PerkAttributeMap attrMap = PerkAttributeHelper.getOrCreateMap(player, LogicalSide.CLIENT);
        final List<ITextProperties> information = Lists.newArrayList();
        information.add((ITextProperties)new Component("perk.reader.astralsorcery.description.head", new Object[] { PerkAttributeReader.formatDecimal(reader.getDefaultValue(attrMap, player, LogicalSide.CLIENT)) }));
        information.add((ITextProperties)new Component("perk.reader.astralsorcery.description.addition", new Object[] { PerkAttributeReader.formatDecimal(reader.getModifierValueForMode(attrMap, player, LogicalSide.CLIENT, ModifierType.ADDITION) - 1.0) }));
        information.add((ITextProperties)new Component("perk.reader.astralsorcery.description.increase", new Object[] { PerkAttributeReader.formatDecimal(reader.getModifierValueForMode(attrMap, player, LogicalSide.CLIENT, ModifierType.ADDED_MULTIPLY)) }));
        information.add((ITextProperties)new Component("perk.reader.astralsorcery.description.moreless", new Object[] { PerkAttributeReader.formatDecimal(reader.getModifierValueForMode(attrMap, player, LogicalSide.CLIENT, ModifierType.STACKING_MULTIPLY)) }));
        if (!stat.getSuffix().isEmpty() || !stat.getPostProcessInfo().isEmpty()) {
            information.add((ITextProperties)Component.field_240750_d_);
        }
        if (!stat.getSuffix().isEmpty()) {
            information.add((ITextProperties)new Component(stat.getSuffix()));
        }
        if (!stat.getPostProcessInfo().isEmpty()) {
            information.add((ITextProperties)new Component(stat.getPostProcessInfo()));
        }
        RenderingDrawUtils.renderBlueTooltipComponents(renderStack, (float)x, (float)y, (float)this.getGuiZLevel(), information, this.fogColor, false);
    }
    
    private void buildDisplayWidth() {
        this.nameStrWidth = -1;
        this.valueStrWidth = -1;
        this.suffixStrWidth = -1;
        for (final PerkStatistic stat : this.statistics) {
            final ITextProperties typeName = (ITextProperties)new Component(stat.getUnlocPerkTypeName());
            final int nameWidth = Math.min(this.fogColor.func_238414_a_(typeName), 126);
            final int valueWidth = this.fogColor.func_78256_a(stat.getPerkValue());
            final int suffixWidth = this.fogColor.func_78256_a(stat.getSuffix());
            if (nameWidth > this.nameStrWidth) {
                this.nameStrWidth = nameWidth;
            }
            if (valueWidth > this.valueStrWidth) {
                this.valueStrWidth = valueWidth;
            }
            if (suffixWidth > this.suffixStrWidth) {
                this.suffixStrWidth = suffixWidth;
            }
        }
        this.nameStrWidth += 6;
        this.valueStrWidth += 6;
        this.suffixStrWidth += 6;
    }
}
