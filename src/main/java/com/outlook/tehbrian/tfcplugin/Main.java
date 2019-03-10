package com.outlook.tehbrian.tfcplugin;

import co.aikar.commands.ACFUtil;
import co.aikar.commands.PaperCommandManager;
import com.outlook.tehbrian.tfcplugin.commands.*;
import me.lucko.luckperms.api.LuckPermsApi;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance = null;
    private static Chat vaultChat = null;
    private static LuckPermsApi luckPermsApi = null;

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

        if (!setupLuckPermsApi()) {
            getLogger().severe("No LuckPerms dependency found! Disabling plugin..");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!setupVaultChat()) {
            getLogger().severe("No Vault dependency found! Disabling plugin..");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

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

    public boolean setupLuckPermsApi() {
        if (getServer().getPluginManager().getPlugin("LuckPerms") == null) {
            return false;
        }
        RegisteredServiceProvider<LuckPermsApi> rsp = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
        if (rsp == null) {
            return false;
        }
        LuckPermsApi luckPermsApi = rsp.getProvider();
        return luckPermsApi != null;
    }

    public boolean setupVaultChat() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            return false;
        }
        vaultChat = rsp.getProvider();
        return vaultChat != null;
    }

    public LuckPermsApi getLuckPermsApi() {
        return luckPermsApi;
    }

    public Chat getVaultChat() {
        return vaultChat;
    }
}