package ru.mikesb.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mikesb.reddit.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
}
