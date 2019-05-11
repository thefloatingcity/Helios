package com.outlook.tehbrian.tfcplugin.piano;

import org.bukkit.Sound;

public enum PianoSound {
    BASEDRUM(Sound.BLOCK_NOTE_BASEDRUM),
    BASS(Sound.BLOCK_NOTE_BASS),
    BELL(Sound.BLOCK_NOTE_BELL),
    CHIME(Sound.BLOCK_NOTE_CHIME),
    FLUTE(Sound.BLOCK_NOTE_FLUTE),
    GUITAR(Sound.BLOCK_NOTE_GUITAR),
    HARP(Sound.BLOCK_NOTE_HARP),
    HAT(Sound.BLOCK_NOTE_HAT),
    PLING(Sound.BLOCK_NOTE_PLING),
    SNARE(Sound.BLOCK_NOTE_SNARE),
    XYLOPHONE(Sound.BLOCK_NOTE_XYLOPHONE);

    private final Sound sound;

    PianoSound(Sound sound) {
        this.sound = sound;
    }

    public Sound toSound() {
        return sound;
    }
}
