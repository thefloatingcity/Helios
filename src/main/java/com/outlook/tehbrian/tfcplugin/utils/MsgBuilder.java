package com.outlook.tehbrian.tfcplugin.utils;

import com.outlook.tehbrian.tfcplugin.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class MsgBuilder {

    private String msgKey;
    private String msgString;
    private String prefixKey;
    private String prefixString;
    private Object[] replacements;

    public MsgBuilder() {

    }

    public MsgBuilder def(String msgKey) {
        this.msgKey = msgKey;
        this.prefixKey = "tfc_prefix";
        return this;
    }

    public MsgBuilder msg(String msgKey) {
        this.msgKey = msgKey;
        return this;
    }

    public MsgBuilder msgString(String msgString) {
        this.msgString = msgString;
        return this;
    }

    public MsgBuilder prefix(String prefixKey) {
        this.prefixKey = prefixKey;
        return this;
    }

    public MsgBuilder prefixString(String prefixString) {
        this.prefixString = prefixString;
        return this;
    }

    public MsgBuilder replace(Object... replacements) {
        this.replacements = replacements;
        return this;
    }

    public String build() {

        FileConfiguration config = Main.getInstance().getConfig();
        String message = "";

        if (msgKey != null) {
            message = config.getString(msgKey);
        } else if (msgString != null) {
            message = msgString;
        }

        if (prefixKey != null) {
            message = config.getString(prefixKey) + " " + message;
        } else if (prefixString != null) {
            message = prefixString + " " + message;
        }

        if (replacements != null) {
            message = String.format(message, replacements);
        }

        return MiscUtils.color(message);
    }
}