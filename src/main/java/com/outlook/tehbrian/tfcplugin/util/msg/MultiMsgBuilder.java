package com.outlook.tehbrian.tfcplugin.util.msg;

import com.outlook.tehbrian.tfcplugin.TFCPlugin;
import com.outlook.tehbrian.tfcplugin.util.MiscUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MultiMsgBuilder {

    private String msgListKey;
    private List<String> msgKeys;
    private List<String> msgStrings;
    private String multiStartKey;
    private String multiStartString;
    private String multiKey;
    private String multiString;
    private List<Object[]> replacements;

    public MultiMsgBuilder() {
    }

    public MultiMsgBuilder def(String msgListKey) {
        this.msgListKey = msgListKey;
        this.multiStartKey = "infixes.server.multistart";
        this.multiKey = "infixes.server.multi";
        return this;
    }

    public MultiMsgBuilder msgListKey(String msgListKey) {
        this.msgListKey = msgListKey;
        return this;
    }

    public MultiMsgBuilder msgKeys(List<String> msgKeys) {
        this.msgKeys = msgKeys;
        return this;
    }

    public MultiMsgBuilder msgStrings(List<String> msgStrings) {
        this.msgStrings = msgStrings;
        return this;
    }

    public MultiMsgBuilder multiStartKey(String multiStartKey) {
        this.multiStartKey = multiStartKey;
        return this;
    }

    public MultiMsgBuilder multiStartString(String multiStartString) {
        this.multiStartString = multiStartString;
        return this;
    }

    public MultiMsgBuilder multiKey(String multiKey) {
        this.multiKey = multiKey;
        return this;
    }

    public MultiMsgBuilder multiString(String multiString) {
        this.multiString = multiString;
        return this;
    }

    public MultiMsgBuilder replace(List<Object[]> replacements) {
        this.replacements = replacements;
        return this;
    }

    public List<String> build() {
        FileConfiguration config = TFCPlugin.getInstance().getConfig();
        List<String> messages = new ArrayList<>();

        if (msgListKey != null) {
            messages = config.getStringList(msgListKey);
        } else if (msgKeys != null) {
            for (String msgKey : msgKeys) {
                messages.add(config.getString(msgKey));
            }
        } else if (msgStrings != null) {
            messages = msgStrings;
        }

        if (multiStartKey != null) {
            messages.set(0, config.getString(multiStartKey) + " " + messages.get(0));
        } else if (multiStartString != null) {
            messages.set(0, multiStartString + " " + messages.get(0));
        }

        if (multiKey != null) {
            for (int i = 1; i < messages.size() - 1; i++) {
                messages.set(i, config.getString(multiKey) + " " + messages.get(i));
            }
        } else if (multiString != null) {
            for (int i = 1; i < messages.size() - 1; i++) {
                messages.set(i, multiString + " " + messages.get(i));
            }
        }

        if (replacements != null) {
            for (int i = 0; i < replacements.size(); i++) {
                messages.set(i, String.format(messages.get(i), replacements.get(i)));
            }
        }

        messages.replaceAll(MiscUtils::color);
        return messages;
    }
}