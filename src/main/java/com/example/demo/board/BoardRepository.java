package com.example.demo.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    @Modifying
    @Query("delete from Board b where b.user.id = :userId")
    void deleteByUserId(@Param("userId") Integer userId);
}
