package com.github.guninigor75.social_media;

import com.github.guninigor75.social_media.dto.activity.PageDto;
import com.github.guninigor75.social_media.entity.activity.Post;
import com.github.guninigor75.social_media.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class SocialMediaApplicationTests {

	@Autowired
	private PostRepository postRepository;
	@Test
	void contextLoads() {
		PageDto pageDto = new PageDto();
		Pageable pageable = new PageDto().getPageable(pageDto);
		Page<Post> all = postRepository.findByFriend(1L, pageable);
		System.out.println(all.getContent());

	}

}
