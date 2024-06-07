package edu.bethlehem.post_service.Opinion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class OpinionPatchDTO {

    private String content;

    private Long journalId;

}
