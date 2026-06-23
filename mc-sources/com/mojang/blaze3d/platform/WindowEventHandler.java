package com.mojang.blaze3d.platform;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface WindowEventHandler {
    void setWindowActive(final boolean windowActive);

    void resizeGui();

    void cursorEntered();
}
