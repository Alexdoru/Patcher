/*
 * Copyright © 2020 by Sk1er LLC
 *
 * All rights reserved.
 *
 * Sk1er LLC
 * 444 S Fulton Ave
 * Mount Vernon, NY
 * sk1er.club
 */

package club.sk1er.patcher.screen.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DebugPerformanceRenderer {

    private boolean frameRender = false;
    private long updated = 0;
    private String mode = "???";
    private final DecimalFormat format = new DecimalFormat("#.00");
    private final List<Long> frames = new ArrayList<>();
    private final String[] renderStrings = new String[5];
    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void renderTickEvent(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (frameRender) {
            frames.add(System.currentTimeMillis());
            frames.removeIf(time -> (System.currentTimeMillis() - time) > 60000);

            if (System.currentTimeMillis() - updated > TimeUnit.SECONDS.toMillis(1)) {
                updated = System.currentTimeMillis();
                renderStrings[0] = "Mode: " + mode;
                int[] intervals = {1, 10, 30, 60};
                int e = 0;
                for (int interval : intervals) {
                    int amt = 0;

                    for (long frame : frames) {
                        if (System.currentTimeMillis() - frame < TimeUnit.SECONDS.toMillis(interval)) {
                            amt++;
                        }
                    }

                    renderStrings[++e] = "Avg on " + interval + "s: " + format.format(amt / ((float) interval));
                }
            }

            final ScaledResolution scaledResolution = new ScaledResolution(mc);
            int y = 40;

            for (String render : renderStrings) {
                mc.fontRendererObj.drawString(render, scaledResolution.getScaledWidth() - 5 - mc.fontRendererObj.getStringWidth(render), y, new Color(1, 165, 82).getRGB(), true);
                y += 10;
            }
        }
    }

    public void toggleFPS() {
        this.frameRender = !this.frameRender;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
