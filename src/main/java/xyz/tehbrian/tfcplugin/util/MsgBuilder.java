package xyz.tehbrian.tfcplugin.util;

import java.util.Arrays;
import java.util.List;

public class MsgBuilder {

    private String msgKey;
    private String msgString;
    private String prefixKey;
    private String prefixString;
    private List<String> formats;

    public MsgBuilder def(final String msgKey) {
        this.msgKey = msgKey;
        this.prefixKey = "prefixes.server.prefix";
        return this;
    }

    public MsgBuilder msgKey(final String msgKey) {
        this.msgKey = msgKey;
        return this;
    }

    public MsgBuilder msgString(final String msgString) {
        this.msgString = msgString;
        return this;
    }

    public MsgBuilder prefixKey(final String prefixKey) {
        this.prefixKey = prefixKey;
        return this;
    }

    public MsgBuilder prefixString(final String prefixString) {
        this.prefixString = prefixString;
        return this;
    }

    public MsgBuilder formats(final String... formats) {
        this.formats = Arrays.asList(formats);
        return this;
    }

    public String build() {
        final StringBuilder sb = new StringBuilder();

        if (this.prefixKey != null) {
            sb.append(this.prefixKey).append(" ");
        } else if (this.prefixString != null) {
            sb.append(this.prefixString).append(" ");
        }

        if (this.msgKey != null) {
            sb.append(this.msgKey);
        } else if (this.msgString != null) {
            sb.append(this.msgString);
        }


        if (this.formats != null) {
            sb.append(" [").append(String.join(", ", formats)).append("]");
        }

        return sb.toString();
    }

}
