package edu.bethlehem.interaction_service.Interaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    Interaction findByIdAndInteractorUserId(Long interactionId, Long interactioner);

    Interaction findByInteractorUserIdAndPostId(Long user, Long postId);

    Interaction findByInteractorUserIdAndOpinionId(long userId, Long opinionId);
}
