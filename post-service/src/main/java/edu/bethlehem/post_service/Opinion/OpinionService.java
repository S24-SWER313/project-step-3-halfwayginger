package edu.bethlehem.post_service.Opinion;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import edu.bethlehem.post_service.Post.Post;
import edu.bethlehem.post_service.Post.PostNotFoundException;
import edu.bethlehem.post_service.Post.PostRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@Data
@RequiredArgsConstructor
public class OpinionService {

        private final PostRepository postRepository;
        private final OpinionRepository opinionRepository;

        private final OpinionModelAssembler assembler;

        public Opinion convertOpinionDtoToOpinionEntity(OpinionDTO opinionDTO, Long userId) {

                return Opinion.builder()
                                .content(opinionDTO.getContent())
                                .post(postRepository.findById(opinionDTO.getPostId())
                                                .orElseThrow(() -> new PostNotFoundException(
                                                                opinionDTO.getPostId())))
                                .userId(userId)
                                .build();

        }

        public EntityModel<Opinion> getOneOpinion(Long opinionId) {
                Opinion opinion = opinionRepository.findById(opinionId)
                                .orElseThrow(() -> new OpinionNotFoundException(opinionId, HttpStatus.NOT_FOUND));
                return assembler.toModel(opinion);
        }

        public List<EntityModel<Opinion>> getAllOpinions() {
                return opinionRepository.findAll().stream().map(assembler::toModel)
                                .collect(Collectors.toList());
        }

        public EntityModel<Opinion> postOpinion(OpinionDTO newOpinionDTO, Long userId) {
                Opinion newOpinion = convertOpinionDtoToOpinionEntity(newOpinionDTO, userId);
                return assembler.toModel(opinionRepository.save(newOpinion));
        }

        public EntityModel<Opinion> updateOpinionPartially(Long opinionId, OpinionPatchDTO opinionPatchDTO) {
                Opinion opinion = opinionRepository.findById(opinionId)
                                .orElseThrow(() -> new OpinionNotFoundException(opinionId, HttpStatus.NOT_FOUND));

                if (opinionPatchDTO.getContent() != null)
                        opinion.setContent(opinionPatchDTO.getContent());

                return assembler.toModel(opinionRepository.save(opinion));

        }

        public void deleteOpinion(Long id) {

                opinionRepository.deleteById(id);

        }

}
