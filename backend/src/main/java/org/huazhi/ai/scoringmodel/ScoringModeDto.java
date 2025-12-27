package org.huazhi.ai.scoringmodel;


import java.util.List;

import lombok.Data;

@Data
public class ScoringModeDto {
    private String question;

    private List<String> answers;

    private String answer;

    private List<TextSegmentDto> textSegments;


}
