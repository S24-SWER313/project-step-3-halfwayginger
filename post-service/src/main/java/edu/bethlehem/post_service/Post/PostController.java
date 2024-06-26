package edu.bethlehem.post_service.Post;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
  private final PostService postService;

  @GetMapping("/{postId}")
  public ResponseEntity<EntityModel<Post>> one(@PathVariable long postId) {
    return ResponseEntity.ok(postService.findPostById(postId));
  }

  @GetMapping("/exi/{postId}")
  public Boolean existes(@PathVariable long postId) {
    return postService.existes(postId);
  }

  @GetMapping()
  public ResponseEntity<CollectionModel<EntityModel<Post>>> all() {
    CollectionModel<EntityModel<Post>> posts = postService.findAllPosts();

    return ResponseEntity.ok(CollectionModel.of(posts, linkTo(methodOn(PostController.class).all()).withSelfRel()));
  }

  @PostMapping()
  public ResponseEntity<EntityModel<Post>> createNewPost(Long userId,
      @RequestBody PostRequestDTO newPostRequestDTO, @RequestHeader(name = "Authorization") String jwtToken) {
    EntityModel<Post> entityModel = postService.createPost(jwtToken, newPostRequestDTO);
    return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updatePostPartially(@PathVariable(value = "id") Long postId,

      @RequestBody PostRequestPatchDTO newPostRequestDTO, @RequestHeader(name = "Authorization") String jwtToken) {

    EntityModel<Post> entityModel = postService.updatePostPartially(postId, newPostRequestDTO, jwtToken);
    return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  ResponseEntity<?> deletePost(@PathVariable Long id, @RequestHeader(name = "Authorization") String jwtToken) {
    postService.deletePost(id, jwtToken);

    return ResponseEntity.noContent().build();
  }

}
