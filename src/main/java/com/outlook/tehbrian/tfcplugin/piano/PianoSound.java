package com.outlook.tehbrian.tfcplugin.piano;

import org.bukkit.Sound;

public enum PianoSound {
    BASEDRUM(Sound.BLOCK_NOTE_BLOCK_BASEDRUM),
    BASS(Sound.BLOCK_NOTE_BLOCK_BASS),
    BELL(Sound.BLOCK_NOTE_BLOCK_BELL),
    CHIME(Sound.BLOCK_NOTE_BLOCK_CHIME),
    FLUTE(Sound.BLOCK_NOTE_BLOCK_FLUTE),
    GUITAR(Sound.BLOCK_NOTE_BLOCK_GUITAR),
    HARP(Sound.BLOCK_NOTE_BLOCK_HARP),
    HAT(Sound.BLOCK_NOTE_BLOCK_HAT),
    PLING(Sound.BLOCK_NOTE_BLOCK_PLING),
    SNARE(Sound.BLOCK_NOTE_BLOCK_SNARE),
    XYLOPHONE(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE);

    private final Sound sound;

    PianoSound(Sound sound) {
        this.sound = sound;
    }

    public Sound toSound() {
        return sound;
    }
}
