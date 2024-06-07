package edu.bethlehem.post_service.Opinion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OpinionDTO {

    private String content;

    private Long postId;

}
