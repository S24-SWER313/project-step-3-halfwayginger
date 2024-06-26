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

import edu.bethlehem.interaction_service.Error.GeneralException;
import edu.bethlehem.interaction_service.Proxies.JwtServiceProxy;
import edu.bethlehem.interaction_service.Proxies.OpinionServiceProxy;
import edu.bethlehem.interaction_service.Proxies.PostServiceProxy;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InteractionService {

        private final InteractionRepository interactionRepository;
        private final InteractionModelAssembler assembler;
        private final PostServiceProxy postServiceProxy;
        private final OpinionServiceProxy opinionServiceProxy;
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

                if (opinionServiceProxy.existes(opinionId) == false)
                        throw new GeneralException("opinion with Id: " + opinionId + " Is Not Found",
                                        HttpStatus.NOT_FOUND);

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
                        Long postId,
                        InteractionRequestDTO interactionDTO,
                        String jwt) {
                logger.trace("Adding Journal Interaction");
                Long userId = jwtServiceProxy.extractUserId(jwt);
                if (postServiceProxy.existes(postId) == false)
                        throw new GeneralException("Post with Id: " + postId + " Is Not Found",
                                        HttpStatus.NOT_FOUND);
                Interaction interaction = interactionRepository.findByInteractorUserIdAndPostId(userId, postId);
                if (interaction != null) {
                        interaction.setType(interactionDTO.getType());
                        return assembler.toModel(interactionRepository.save(interaction));
                }

                interaction = new Interaction(interactionDTO.getType(), userId);

                interaction.setPostId(postId);
                return assembler.toModel(interactionRepository.save(interaction));

        }

}
