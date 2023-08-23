package com.github.guninigor75.social_media.mapper;

import com.github.guninigor75.social_media.dto.activity.CreatePost;
import com.github.guninigor75.social_media.dto.activity.PostDto;
import com.github.guninigor75.social_media.entity.activity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper {

    Post createPostToPost(CreatePost createPost);

    @Mapping(target = "createdAt", expression = "java(post.getCreatedAt().toEpochMilli())")
    @Mapping(target = "updatedAt", expression = "java(post.getUpdatedAt().toEpochMilli())")
    PostDto postToPostDto(Post post);

    List<PostDto> postsToPostsDto(List<Post> posts);
}
