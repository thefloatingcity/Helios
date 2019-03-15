package com.outlook.tehbrian.tfcplugin;

import co.aikar.commands.ACFUtil;
import co.aikar.commands.PaperCommandManager;
import com.outlook.tehbrian.tfcplugin.commands.*;
import com.outlook.tehbrian.tfcplugin.events.*;
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

    static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new MiscEvents(this), this);
        getServer().getPluginManager().registerEvents(new AntiBuildEvents(this), this);

        if (!setupVault()) {
            getLogger().severe("No Vault dependency found! Disabling plugin..");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        PaperCommandManager manager = new PaperCommandManager(this);

        manager.registerCommand(new ActionCommand(this));
        manager.registerCommand(new CoreCommand(this));
        manager.registerCommand(new CustomHelpCommand(this));
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