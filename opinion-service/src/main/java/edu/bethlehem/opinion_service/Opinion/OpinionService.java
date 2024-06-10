package edu.bethlehem.opinion_service.Opinion;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import edu.bethlehem.opinion_service.Error.GeneralException;
import edu.bethlehem.opinion_service.Proxies.JwtServiceProxy;
import edu.bethlehem.opinion_service.Proxies.PostServiceProxy;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@Data
@RequiredArgsConstructor
public class OpinionService {

        private final OpinionRepository opinionRepository;
        private final JwtServiceProxy jwtServiceProxy;
        private final OpinionModelAssembler assembler;
        private final PostServiceProxy postServiceProxy;

        public Opinion convertOpinionDtoToOpinionEntity(OpinionDTO opinionDTO, Long userId) {

                return Opinion.builder()
                                .content(opinionDTO.getContent())
                                .postId(opinionDTO.getPostId())
                                .userId(userId)
                                .build();

        }

        public EntityModel<Opinion> getOneOpinion(Long opinionId) {
                Opinion opinion = opinionRepository.findById(opinionId)
                                .orElseThrow(() -> new OpinionNotFoundException(opinionId, HttpStatus.NOT_FOUND));
                return assembler.toModel(opinion);
        }

        public Boolean exists(Long opinionId) {
                Opinion opinion = opinionRepository.findById(opinionId)
                                .orElse(null);
                if (opinion == null)
                        return false;
                return true;
        }

        public List<EntityModel<Opinion>> getAllOpinions() {
                return opinionRepository.findAll().stream().map(assembler::toModel)
                                .collect(Collectors.toList());
        }

        public EntityModel<Opinion> postOpinion(OpinionDTO newOpinionDTO, String jwt) {
                Long userId = jwtServiceProxy.extractUserId(jwt);
                // postServiceProxy.existes(newOpinionDTO.getPostId());
                if (postServiceProxy.existes(newOpinionDTO.getPostId()) == false)
                        throw new GeneralException("Post with Id: " + newOpinionDTO.getPostId() + " Is Not Found",
                                        HttpStatus.NOT_FOUND);
                // check if post exists
                Opinion newOpinion = convertOpinionDtoToOpinionEntity(newOpinionDTO, userId);
                return assembler.toModel(opinionRepository.save(newOpinion));
        }

        public EntityModel<Opinion> updateOpinionPartially(Long opinionId, OpinionPatchDTO opinionPatchDTO,
                        String jwt) {

                Opinion opinion = opinionRepository.findById(opinionId)
                                .orElseThrow(() -> new OpinionNotFoundException(opinionId, HttpStatus.NOT_FOUND));

                if (jwtServiceProxy.extractUserId(jwt) != opinion.getUserId())
                        throw new OpinionNotFoundException(opinionId, HttpStatus.FORBIDDEN);

                if (opinionPatchDTO.getContent() != null)
                        opinion.setContent(opinionPatchDTO.getContent());

                return assembler.toModel(opinionRepository.save(opinion));

        }

        public void deleteOpinion(Long id, String jwt) {

                if (jwtServiceProxy.extractUserId(jwt) != opinionRepository.findById(id).get().getUserId())
                        throw new OpinionNotFoundException(id, HttpStatus.FORBIDDEN);

                opinionRepository.deleteById(id);

        }

}
