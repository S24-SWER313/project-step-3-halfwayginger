package edu.bethlehem.interaction_service.Interaction;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.bethlehem.post_service.Post.Post;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    Interaction findByIdAndInteractorUserId(Long interactionId, Long interactioner);

    Interaction findByInteractorUserIdAndPost(Long user, Post post);

    Interaction findByInteractorUserIdAndOpinionId(long userId, Long opinionId);
}
