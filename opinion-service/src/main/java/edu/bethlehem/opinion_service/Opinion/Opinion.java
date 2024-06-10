package edu.bethlehem.opinion_service.Opinion;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.bethlehem.post_service.Post.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Opinion {
    @Id
    @GeneratedValue
    private Long id;
    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY) // Fetch Type Has been Changed from Lazy To Eager, Because When I request one
                                       // opinion there is an error, and this is how I solved it
    @JoinColumn(name = "post", updatable = false)

    @JsonIgnore
    private Post post;

    private Long userId;

}