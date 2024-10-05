package dev.jb0s.blockgameenhanced.mixin.gui.screen;

import dev.jb0s.blockgameenhanced.BlockgameEnhanced;
import dev.jb0s.blockgameenhanced.gamefeature.mmovendor.MMOVendor;

import org.joml.Vector2i;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Mixin(GenericContainerScreen.class)
public class MixinGenericContainerScreen extends HandledScreen<GenericContainerScreenHandler> implements ScreenHandlerProvider<GenericContainerScreenHandler> {
    @Shadow @Final private int rows;

    private static final Identifier TEXTURE = new Identifier("textures/gui/container/generic_54.png");
    private static final Text LOOT_ALL_BUTTON = Text.translatable("menu.blockgame.container.plunder");

    public MixinGenericContainerScreen(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init(); // right. forgot about that

        // If this is not a regular chest we're interacting with, don't add the loot all button
        String titleStr = title.getString();
        boolean isValidContainer = titleStr.endsWith("Plunder") || titleStr.endsWith("Chest") || titleStr.endsWith("Shulker Box") || titleStr.endsWith("Barrel");
        if(!titleStr.isEmpty() && !isValidContainer) {
            return;
        }

        // If the user has disabled the Loot All button, cancel here
        if(!BlockgameEnhanced.getConfig().getAccessibilityConfig().enableLootAllButton) {
            return;
        }

        int originX = width / 2;
        int originY = height / 2;
        int btnWidth = 47;
        int btnHeight = 12;

        int x = originX + 34;
        int y = originY - (backgroundHeight / 2) + 4;
        addDrawableChild(ButtonWidget.builder(LOOT_ALL_BUTTON, (button) -> {
                MinecraftClient mc = MinecraftClient.getInstance();
                ClientPlayerEntity p = mc.player;

                for(int i = 0; i < 9 * rows; i++) {
                    mc.interactionManager.clickSlot(handler.syncId, i, 0, SlotActionType.QUICK_MOVE, p);
                }
            })
            .dimensions(x, y, btnWidth, btnHeight)
            .build()
        );
    }

    /**
     * Reimplementation from GenericContainerScreen
     * @param context n/a
     * @param delta n/a
     * @param mouseX n/a
     * @param mouseY n/a
     */
    @Override
    public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        Identifier tex = TEXTURE;
        String vendorName = getTitle().getString().replaceAll(" \\(\\d/\\d\\)", "");

        // Find the vendor currently being interacted with
        MMOVendor vendor = MMOVendor.getByName(vendorName);

        if (vendor != null) {
            tex = new Identifier("blockgame", String.format("textures/gui/container/%s.png", vendor.getUi()));
            tex = new Identifier("blockgame", String.format("textures/gui/vendor/%s.png", vendor.getUi()));
            if (MinecraftClient.getInstance().getResourceManager().getResource(tex).isEmpty())
                tex = new Identifier("blockgame", "textures/gui/vendor/generic.png");
            backgroundWidth = 256;
            titleX = -32;
        }
        else if (getTitle().getString().equals("Auction House")) {
            tex = new Identifier("blockgame", "textures/gui/container/auction_house.png");
            tex = new Identifier("blockgame", "textures/gui/vendor/auction_house.png");
            backgroundWidth = 256;
            titleX = 8;
        }

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, tex);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(tex, i, j, 0, 0, this.backgroundWidth, this.rows * 18 + 17);
        context.drawTexture(tex, i, j + this.rows * 18 + 17, 0, 126, this.backgroundWidth, 96);

        if(vendor != null) {
            // Corners of the window where our Player Entities are rendered
            Vector2i topLeft = new Vector2i(this.x + 176, this.y + 146);
            Vector2i bottomRight = new Vector2i(topLeft.x + 33, topLeft.y + 63);

            int entityScale = 40;
            float verticalOffset = 0.4f;
            InventoryScreen.drawEntity(context,
                    topLeft.x, topLeft.y,
                    bottomRight.x, bottomRight.y,
                    entityScale, verticalOffset,
                    mouseX, mouseY, client.player);

            PlayerEntity vendorEntity = vendor.getVendorEntity();
            if(vendorEntity != null) {
                topLeft.x = this.x - 32;
                topLeft.y = this.y + 52;
                bottomRight.x = topLeft.x + 33;
                bottomRight.y = topLeft.y + 68;
                InventoryScreen.drawEntity(context,
                        topLeft.x, topLeft.y,
                        bottomRight.x, bottomRight.y,
                        entityScale, verticalOffset,
                        mouseX, mouseY, vendorEntity);
            }
        }
    }
}
