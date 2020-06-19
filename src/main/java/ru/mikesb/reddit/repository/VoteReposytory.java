package ru.mikesb.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mikesb.reddit.model.Vote;

@Repository
public interface VoteReposytory extends JpaRepository<Vote,Long> {
}
