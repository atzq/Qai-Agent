package net.itzq.mira.modules.ai.client.openai.audio.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * 音频枚举类
 */
public class AudioEnum {
    @Getter
    @AllArgsConstructor
    public enum Voice implements Serializable {
        ALLOY("alloy"),
        ECHO("echo"),
        FABLE("fable"),
        ONYX("onyx"),
        NOVA("nova"),
        SHIMMER("shimmer"),
        ;
        private final String value;
    }

    @Getter
    @AllArgsConstructor
    public enum ResponseFormat implements Serializable {
        MP3("mp3"),
        OPUS("opus"),
        AAC("aac"),
        FLAC("flac"),
        WAV("wav"),
        PCM("pcm"),
        ;
        private final String value;
    }
}
