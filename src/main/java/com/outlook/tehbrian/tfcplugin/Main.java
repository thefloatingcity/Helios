package com.outlook.tehbrian.tfcplugin;

import co.aikar.commands.ACFUtil;
import co.aikar.commands.PaperCommandManager;
import com.outlook.tehbrian.tfcplugin.commands.*;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance = null;
    private static Permission vaultPerms = null;
    private static Chat vaultChat = null;

    public Main() {
        instance = this;
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        PaperCommandManager manager = new PaperCommandManager(this);

        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new EventsHandler(this), this);

        if (!setupVaultPerms()) {
            getLogger().severe("No Vault dependency found! Disabling plugin..");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupVaultChat();

        manager.getCommandCompletions().registerAsyncCompletion("pianosounds", c -> ACFUtil.enumNames(Piano.PianoSounds.values()));

        manager.registerCommand(new ActionCommand(this));
        manager.registerCommand(new EmoteCommand(this));
        manager.registerCommand(new GamemodeCommand(this));
        manager.registerCommand(new PianoCommand(this));
        manager.registerCommand(new RulesCommand(this));
        manager.registerCommand(new UtilCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("I hope to see you again soon!");
    }

    private boolean setupVaultPerms() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        vaultPerms = rsp.getProvider();
        return vaultPerms != null;
    }

    public boolean setupVaultChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        vaultChat = rsp.getProvider();
        return vaultChat != null;
    }

    public Permission getVaultPerms() {
        return vaultPerms;
    }

    public Chat getVaultChat() {
        return vaultChat;
    }
}