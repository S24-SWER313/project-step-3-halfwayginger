package edu.bethlehem.post_service.Post;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter(AccessLevel.PUBLIC)
public class PostRequestDTO {
    private String content;
    private Long userId;
}
