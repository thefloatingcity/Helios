package com.outlook.tehbrian.tfcplugin;

import co.aikar.commands.*;
import co.aikar.locales.MessageKeyProvider;
import com.outlook.tehbrian.tfcplugin.commands.*;
import com.outlook.tehbrian.tfcplugin.events.AntiBuildEvents;
import com.outlook.tehbrian.tfcplugin.events.BuildingEvents;
import com.outlook.tehbrian.tfcplugin.events.MiscEvents;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class Main extends JavaPlugin {

    private static Main instance = null;
    private static Permission vaultPerms = null;
    private static Chat vaultChat = null;

    public Main() {
        instance = this;
    }

    static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new AntiBuildEvents(), this);
        getServer().getPluginManager().registerEvents(new BuildingEvents(this), this);
        getServer().getPluginManager().registerEvents(new MiscEvents(this), this);

        setupCommandManager();

        if (!setupVault()) {
            getLogger().severe("No Vault dependency found! Disabling plugin..");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("I hope to see you again soon!");
    }

    private void setupCommandManager() {
        // ACF
        PaperCommandManager manager = new PaperCommandManager(this);

        // ACF Completions
        manager.getCommandCompletions().registerAsyncCompletion("pianosounds", c -> ACFUtil.enumNames(Piano.PianoSounds.values()));

        manager.registerCommand(new ActionCommand(this));
        manager.registerCommand(new CoreCommand(this));
        manager.registerCommand(new EmoteCommand(this));
        manager.registerCommand(new GamemodeCommand(this));
        manager.registerCommand(new PianoCommand(this));
        manager.registerCommand(new RulesCommand(this));
        manager.registerCommand(new UtilCommand(this));

        manager.enableUnstableAPI("help");

        // ACF Custom Messages
        Map<MessageKeyProvider, String> messages = new HashMap<>();
        messages.put(MessageKeys.COULD_NOT_FIND_PLAYER, "test");
        messages.put(MinecraftMessageKeys.YOU_MUST_BE_HOLDING_ITEM, "some other message");
        messages.put(MessageKeys.ERROR_PREFIX, "TEST:");
        manager.getLocales().addMessages(manager.getLocales().getDefaultLocale(), messages);

        // ACF Conditions
        manager.getCommandConditions().addCondition(Integer.class, "limits", (context, executionContext, value) -> {
            if (value == null) {
                return;
            }
            if (context.hasConfig("min") && context.getConfigValue("min", 0) > value) {
                throw new ConditionFailedException("Minimum value must be " + context.getConfigValue("min", 0));
            }
            if (context.hasConfig("max") && context.getConfigValue("max", 3) < value) {
                throw new ConditionFailedException("Maximum value must be " + context.getConfigValue("max", 3));
            }
        });
    }

    private boolean setupVault() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Permission> prsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (prsp == null) {
            return false;
        }
        vaultPerms = prsp.getProvider();
        RegisteredServiceProvider<Chat> crsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (crsp == null) {
            return false;
        }
        vaultChat = crsp.getProvider();
        return (vaultPerms != null && vaultChat != null);
    }

    public Permission getVaultPerms() {
        return vaultPerms;
    }

    public Chat getVaultChat() {
        return vaultChat;
    }
}