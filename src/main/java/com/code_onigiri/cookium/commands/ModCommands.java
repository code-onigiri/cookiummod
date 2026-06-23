package com.code_onigiri.cookium.commands;

import com.code_onigiri.cookium.CookiumMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = CookiumMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModCommands {
    
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("cookium")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("info")
                        .executes(context -> {
                            context.getSource().sendSuccess(() -> 
                                    Component.literal("Cookium Mod v" + CookiumMod.MODID), true);
                            return 1;
                        })
                )
                .then(Commands.literal("teleport")
                        .then(Commands.argument("player", net.minecraft.commands.arguments.EntityArgument.player())
                                .executes(context -> {
                                    ServerPlayer player = net.minecraft.commands.arguments.EntityArgument.getPlayer(context, "player");
                                    player.teleportTo(context.getSource().getLevel(), 
                                            player.getX(), player.getY() + 10, player.getZ());
                                    context.getSource().sendSuccess(() -> 
                                            Component.literal("Teleported " + player.getName().getString() + " up 10 blocks!"), true);
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("spawn")
                        .then(Commands.argument("count", IntegerArgumentType.integer(1, 100))
                                .executes(context -> {
                                    int count = IntegerArgumentType.getInteger(context, "count");
                                    context.getSource().sendSuccess(() -> 
                                            Component.literal("Spawning " + count + " cookium sparks!"), true);
                                    return count;
                                })
                        )
                )
                .then(Commands.literal("setcolor")
                        .then(Commands.argument("r", FloatArgumentType.floatArg(0, 1))
                                .then(Commands.argument("g", FloatArgumentType.floatArg(0, 1))
                                        .then(Commands.argument("b", FloatArgumentType.floatArg(0, 1))
                                                .executes(context -> {
                                                    float r = FloatArgumentType.getFloat(context, "r");
                                                    float g = FloatArgumentType.getFloat(context, "g");
                                                    float b = FloatArgumentType.getFloat(context, "b");
                                                    context.getSource().sendSuccess(() -> 
                                                            Component.literal("Set particle color to RGB(" + r + "," + g + "," + b + ")"), true);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
        );

        dispatcher.register(Commands.literal("heal")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("player", net.minecraft.commands.arguments.EntityArgument.player())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1, 100))
                                .executes(context -> {
                                    ServerPlayer player = net.minecraft.commands.arguments.EntityArgument.getPlayer(context, "player");
                                    int amount = IntegerArgumentType.getInteger(context, "amount");
                                    player.heal(amount);
                                    context.getSource().sendSuccess(() -> 
                                            Component.literal("Healed " + player.getName().getString() + " for " + amount + " HP!"), true);
                                    return 1;
                                })
                        )
                )
        );
    }
}
