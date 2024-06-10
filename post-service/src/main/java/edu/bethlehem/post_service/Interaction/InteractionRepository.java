package edu.bethlehem.post_service.Interaction;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.bethlehem.post_service.Opinion.Opinion;
import edu.bethlehem.post_service.Post.Post;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    Interaction findByIdAndInteractorUserId(Long interactionId, Long interactioner);

    Interaction findByInteractorUserIdAndPost(Long user, Post post);

    Interaction findByInteractorUserIdAndOpinion(long userId, Opinion opinion);
}
