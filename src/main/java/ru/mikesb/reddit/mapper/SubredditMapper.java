package ru.mikesb.reddit.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {


    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    ru.mikesb.reddit.dto.SubredditDto mapSubredditToDto(ru.mikesb.reddit.model.Subreddit subreddit);

    default Integer mapPosts(List<ru.mikesb.reddit.model.Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    ru.mikesb.reddit.model.Subreddit mapDtoToSubreddit(ru.mikesb.reddit.dto.SubredditDto subredditDto);
}