package org.hzai.ai.scoringmodel;


import java.util.List;

import dev.langchain4j.data.segment.TextSegment;
import lombok.Data;

@Data
public class ScoringModeDto {
    private String question;

    private List<String> answers;

    private String answer;

    private List<TextSegment> textSegments;


}
