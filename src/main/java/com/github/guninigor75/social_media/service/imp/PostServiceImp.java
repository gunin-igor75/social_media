package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.dto.activity.CreatePost;
import com.github.guninigor75.social_media.entity.activity.Picture;
import com.github.guninigor75.social_media.entity.activity.Post;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.exception.ResourceNotFoundException;
import com.github.guninigor75.social_media.repository.PostRepository;
import com.github.guninigor75.social_media.security.SecurityUser;
import com.github.guninigor75.social_media.service.PictureService;
import com.github.guninigor75.social_media.service.PostService;
import com.github.guninigor75.social_media.service.UserService;
import com.github.guninigor75.social_media.service.props.PictureProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImp implements PostService {

    private final PostRepository postRepository;

    private final UserService userService;

    private final PictureService pictureService;

    private final PictureProperties pictureProperties;

    @Override
    public Post createPost(Post post, MultipartFile file, SecurityUser securityUser) {
        User user = userService.getProxyUser(securityUser.getId());
        Picture picture = new Picture();
        picture.setPost(post);
        Picture persistentPicture = pictureService.createPicture(picture, file);
        post.setUser(user);
        post.setImage(pictureProperties.getEndPoint() + persistentPicture.getFilePath());
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        Post post = getPost(id);
        Picture picture = pictureService.getPictureByIdPost(id);
        pictureService.deletePicture(picture);
        postRepository.delete(post);
    }

    @Override
    public Post getPost(Long id) {
        Optional<Post> postOrEmpty = postRepository.findById(id);
        if (postOrEmpty.isEmpty()) {
            String message = "Post with " + id + " is not in the database";
            log.debug(message);
            throw new ResourceNotFoundException(message);
        }
        return postOrEmpty.get();
    }

    @Override
    public Post updatePictureByPostId(Long id, MultipartFile file) {
        Picture picture = pictureService.getPictureByIdPost(id);
        pictureService.updatePicture(picture, file);
        return getPost(id);
    }

    @Override
    @Transactional
    public Post updatePost(CreatePost createPost) {
        Post post = getPost(createPost.getId());
        if (createPost.getTitle() != null) {
            post.setTitle(createPost.getTitle());
        }
        if (createPost.getContent() != null) {
            post.setContent(createPost.getContent());
        }
        return postRepository.save(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getPosts(Pageable pageable) {
        Page<Post> all = postRepository.findAll(pageable);
        return all.getContent();
    }
}