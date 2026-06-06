package net.itzq.mira.modules.ai.client.openai.audio.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * Whisper枚举类
 */
public class WhisperEnum {
    @Getter
    @AllArgsConstructor
    public enum ResponseFormat implements Serializable {
        JSON("json"),
        TEXT("text"),
        SRT("srt"),
        VERBOSE_JSON("verbose_json"),
        VTT("vtt"),
        ;
        private final String value;
    }
}
