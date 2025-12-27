package org.huazhi.ai.scoringmodel;

import java.util.Map;

import lombok.Data;

@Data
public class TextSegmentDto {
    private String text;

    private Map<String, Object> metadata;
}
