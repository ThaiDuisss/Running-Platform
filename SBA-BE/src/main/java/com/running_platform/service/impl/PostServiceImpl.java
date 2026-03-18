package com.running_platform.service.impl;

import com.running_platform.dto.request.CreatePostRequest;
import com.running_platform.dto.response.PostResponse;
import com.running_platform.entity.Post.PostImage;
import com.running_platform.entity.Post.Posts;
import com.running_platform.entity.RunActivities.RunActivity;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.PostStatus;
import com.running_platform.enums.UploadFolder;
import com.running_platform.mapper.PostMapper;
import com.running_platform.repository.PostImageRepository;
import com.running_platform.repository.PostRepository;
import com.running_platform.repository.RunActivityRepository;
import com.running_platform.repository.UserRepository;
import com.running_platform.service.CloudinaryService;
import com.running_platform.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postsRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final PostImageRepository postImageRepository;
    @Override
    @Transactional
    public PostResponse createPost(CreatePostRequest request, Authentication authentication) {

        String username = authentication.getName();

        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Posts post = new Posts();
        post.setContent(request.getContent());
        post.setUser(user);
        post.setStatus(PostStatus.PENDING);

        if (request.getImages() != null) {
            for (String base64 : request.getImages()) {

                byte[] imageBytes = Base64.getDecoder().decode(base64);
                String url = cloudinaryService.uploadBytes(imageBytes, null);

                PostImage img = new PostImage();
                img.setImageUrl(url);

                post.addImage(img);
            }
        }

        postsRepository.save(post);

        return postMapper.toDto(post);
    }

    @Override
    public Page<PostResponse> getPostsByUser(Long userId, Pageable pageable) {
        Page<Posts> posts = postsRepository.findByUser_Id(userId, pageable);
        return posts.map(postMapper::toDto);
    }

    @Override
    public List<PostResponse> findAllApprovePost() {
        List<Posts> posts = postsRepository.findApprovedPosts();

        return posts.stream()
                .map(postMapper::toDto)
                .toList();
    }
}
