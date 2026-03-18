    package com.running_platform.mapper;

    import com.running_platform.dto.response.PostResponse;
    import com.running_platform.entity.Post.PostImage;
    import com.running_platform.entity.Post.Posts;
    import org.mapstruct.Mapper;
    import org.mapstruct.Mapping;

    import java.util.List;

    @Mapper(componentModel = "spring")
    public interface PostMapper {

        @Mapping(source = "user.username", target = "username")
        PostResponse toDto(Posts post);

        default List<String> map(List<PostImage> images) {
            if (images == null) return null;
            return images.stream()
                    .map(PostImage::getImageUrl)
                    .toList();
        }
    }
