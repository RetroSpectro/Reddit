package ru.mikesb.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mikesb.reddit.model.Subreddit;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit,Long> {
}
