package edu.bethlehem.interaction_service.Interaction;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import edu.bethlehem.post_service.Post.Post;
import edu.bethlehem.post_service.Post.PostNotFoundException;
import edu.bethlehem.post_service.Post.PostRepository;
import edu.bethlehem.post_service.Proxies.JwtServiceProxy;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InteractionService {

        private final InteractionRepository interactionRepository;
        private final PostRepository postRepository;
        private final InteractionModelAssembler assembler;
        @Autowired
        private JwtServiceProxy jwtServiceProxy;
        Logger logger = LoggerFactory.getLogger(Interaction.class);

        public EntityModel<Interaction> findInteractionById(Long InteractionId) {
                logger.trace("Finding Interaction by ID");
                Interaction interaction = interactionRepository.findById(
                                InteractionId)
                                .orElseThrow(() -> new InteractionNotFoundException(InteractionId,
                                                HttpStatus.NOT_FOUND));

                return assembler.toModel(interaction);
        }

        public CollectionModel<EntityModel<Interaction>> findAllInteractions() {
                logger.trace("Finding All Interactions");
                List<EntityModel<Interaction>> interactions = interactionRepository
                                .findAll()
                                .stream()
                                .map(assembler::toModel)
                                .collect(Collectors.toList());
                return CollectionModel.of(interactions,
                                linkTo(methodOn(InteractionController.class).all()).withSelfRel());
        }

        public Interaction saveInteraction(Interaction interaction) {
                logger.trace("Saving Interaction");
                return interactionRepository.save(interaction);
        }

        public EntityModel<Interaction> createInteraction(InteractionType type, String jwt) {
                logger.trace("Creating Interaction");
                Long userId = jwtServiceProxy.extractUserId(jwt);
                Interaction interaction = new Interaction(type, userId);
                return assembler.toModel(saveInteraction(interaction));
        }

        public EntityModel<Interaction> updateInteraction(Long interactionId,
                        InteractionRequestDTO newInteractionRequestDTO) {
                logger.trace("Updating Interaction");
                return interactionRepository.findById(
                                interactionId)
                                .map(interaction -> {
                                        interaction.setType(newInteractionRequestDTO.getType());
                                        return assembler.toModel(interactionRepository.save(interaction));
                                })
                                .orElseThrow(() -> new InteractionNotFoundException(
                                                interactionId, HttpStatus.UNPROCESSABLE_ENTITY));
        }

        public EntityModel<Interaction> addOpinionInteraction(
                        Long opinionId,
                        InteractionRequestDTO interactionDTO,
                        String jwt) {
                logger.trace("Adding Opinion Interaction");
                Long userId = jwtServiceProxy.extractUserId(jwt);

                // Check if opinion exiests

                Interaction interaction = interactionRepository.findByInteractorUserIdAndOpinionId(userId, opinionId);
                if (interaction != null) {
                        interaction.setType(interactionDTO.getType());
                        return assembler.toModel(interactionRepository.save(interaction));

                }

                interaction = new Interaction(interactionDTO.getType(), userId);
                interaction.setOpinionId(opinionId);

                interaction = interactionRepository.save(interaction);

                return assembler.toModel(interaction);

        }

        public EntityModel<Interaction> addPostInteraction(
                        Long journalId,
                        InteractionRequestDTO interactionDTO,
                        String jwt) {
                logger.trace("Adding Journal Interaction");
                Long userId = jwtServiceProxy.extractUserId(jwt);

                Post post = postRepository.findById(
                                journalId)
                                .orElseThrow(() -> new PostNotFoundException(journalId, HttpStatus.NOT_FOUND));
                Interaction interaction = interactionRepository.findByInteractorUserIdAndPost(userId, post);
                if (interaction != null) {
                        interaction.setType(interactionDTO.getType());
                        return assembler.toModel(interactionRepository.save(interaction));
                }

                interaction = new Interaction(interactionDTO.getType(), userId);

                interaction.setPost(post);
                return assembler.toModel(interactionRepository.save(interaction));

        }

}
