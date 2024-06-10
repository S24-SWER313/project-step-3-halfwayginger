package edu.bethlehem.interaction_service.Interaction;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/interactions")
public class InteractionController {

  private final InteractionService service;

  @GetMapping("/{interactionId}")
  public ResponseEntity<EntityModel<Interaction>> one(@PathVariable Long interactionId) {
    return ResponseEntity.ok(service.findInteractionById(interactionId));
  }

  @GetMapping()
  public ResponseEntity<CollectionModel<EntityModel<Interaction>>> all() {
    return ResponseEntity.ok(service.findAllInteractions());
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateInteractionPartially(@PathVariable(value = "id") Long interactionId,
      @RequestBody InteractionRequestDTO newInteraction) {
    return ResponseEntity.ok().body(service.updateInteraction(interactionId, newInteraction));
  }

  @PostMapping("/opinion/{opinionId}")
  public ResponseEntity<?> addOpinionInteraction(
      @PathVariable(value = "opinionId") Long opinionId,
      @RequestBody InteractionRequestDTO interaction, @RequestHeader(name = "Authorization") String jwtToken) {

    return ResponseEntity.ok().body(service.addOpinionInteraction(opinionId, interaction, jwtToken));
  }

  @PostMapping("/journal/{journalId}")
  public ResponseEntity<?> addJournalInteraction(
      @PathVariable(value = "journalId") Long journalId,
      @RequestBody InteractionRequestDTO interaction, @RequestHeader(name = "Authorization") String jwtToken) {
    return ResponseEntity.ok(service.addPostInteraction(journalId, interaction, jwtToken));
  }

}
