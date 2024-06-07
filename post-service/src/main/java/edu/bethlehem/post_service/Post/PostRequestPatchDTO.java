package edu.bethlehem.post_service.Post;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Data
public class PostRequestPatchDTO {
    private String content;
    private Long userId;
}
