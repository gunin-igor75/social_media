package com.github.guninigor75.social_media.mappers;

import com.github.guninigor75.social_media.dto.activity.CreatePost;
import com.github.guninigor75.social_media.dto.activity.PostDto;
import com.github.guninigor75.social_media.entity.activity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper {

    Post createPostToPost(CreatePost createPost);

    PostDto postToPostDto(Post post);

    List<PostDto> postsToPostsDto(List<Post> posts);
}
