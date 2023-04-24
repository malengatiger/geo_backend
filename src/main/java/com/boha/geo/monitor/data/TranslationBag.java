package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("translationBags")
public class TranslationBag {
    private String stringToTranslate;
    private String source;
    private String target;
    private String format;
    private String translatedText;
    private String key;
}
