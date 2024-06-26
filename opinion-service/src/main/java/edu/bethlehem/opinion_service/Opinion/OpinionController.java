package edu.bethlehem.opinion_service.Opinion;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/opinions")
public class OpinionController {

  private final OpinionService opinionService;

  @GetMapping("/{opinionId}")
  public EntityModel<Opinion> one(@PathVariable Long opinionId) {

    return opinionService.getOneOpinion(opinionId);
  }

  @GetMapping("/exi/{opinionId}")
  public Boolean exists(@PathVariable Long opinionId) {

    return opinionService.exists(opinionId);
  }

  @GetMapping()
  CollectionModel<EntityModel<Opinion>> all() {

    return CollectionModel.of(opinionService.getAllOpinions(),
        linkTo(methodOn(OpinionController.class).all()).withSelfRel());
  }

  @PostMapping()
  ResponseEntity<?> newOpinion(@RequestBody OpinionDTO newOpinion,
      @RequestHeader(name = "Authorization") String jwtToken) {

    EntityModel<Opinion> entityModel = opinionService.postOpinion(newOpinion, jwtToken);

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateOpinionPartially(@PathVariable(value = "id") Long opinionId,
      @RequestBody OpinionPatchDTO newOpinion, @RequestHeader(name = "Authorization") String jwtToken) {

    EntityModel<Opinion> entityModel = opinionService.updateOpinionPartially(opinionId, newOpinion, jwtToken);

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOpinion(@PathVariable Long id,
      @RequestHeader(name = "Authorization") String jwtToken) {

    opinionService.deleteOpinion(id, jwtToken);
    return ResponseEntity.noContent().build();

  }

}
