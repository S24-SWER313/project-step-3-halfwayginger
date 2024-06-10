package edu.bethlehem.post_service.Post;

import java.lang.reflect.Method;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import edu.bethlehem.post_service.Proxies.JwtServiceProxy;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Data
public class PostService {

    private final PostRepository postRepository;
    private final PostModelAssembler assembler;
    @Autowired
    private JwtServiceProxy jwtServiceProxy;

    public Post convertPostDtoToPostEntity(Long userId, PostRequestDTO postRequestDTO) {
        return Post.builder()
                .content(postRequestDTO.getContent())
                .userId(userId)
                .build();
    }

    public EntityModel<Post> findPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        ;

        return assembler.toModel(post);
    }

    public CollectionModel<EntityModel<Post>> findAllPosts() {
        return CollectionModel.of(postRepository
                .findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList()));
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public EntityModel<Post> createPost(String jwt, PostRequestDTO newPostRequestDTO) {
        Long userId = jwtServiceProxy.extractUserId(jwt);
        Post newPost = convertPostDtoToPostEntity(userId, newPostRequestDTO);
        newPost = savePost(newPost);
        return assembler.toModel(newPost);
    }

    public EntityModel<Post> updatePostPartially(Long postId,
            PostRequestPatchDTO newPostRequestDTO, String jwtToken) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId, HttpStatus.NOT_FOUND));

        if (post.getUserId() != jwtServiceProxy.extractUserId(jwtToken)) {
            throw new PostNotFoundException(postId, HttpStatus.UNAUTHORIZED);
        }
        try {
            for (Method method : PostRequestPatchDTO.class.getMethods()) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    Object value = method.invoke(newPostRequestDTO);
                    if (value != null) {
                        String propertyName = method.getName().substring(3); // remove "get"
                        if (propertyName.equals("Class")) // Class is a reserved keyword in Java
                            continue;
                        Method setter = Post.class.getMethod("set" + propertyName, method.getReturnType());
                        setter.invoke(post, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assembler.toModel(postRepository.save(post));

    }

    public void deletePost(Long postId, String jwtToken) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId, HttpStatus.UNPROCESSABLE_ENTITY));
        if (post.getUserId() != jwtServiceProxy.extractUserId(jwtToken)) {
            throw new PostNotFoundException(postId, HttpStatus.UNAUTHORIZED);
        }
        postRepository.delete(post);
    }

}
