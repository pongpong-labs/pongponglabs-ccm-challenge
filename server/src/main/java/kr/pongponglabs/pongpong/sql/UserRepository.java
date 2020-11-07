package kr.pongponglabs.pongpong.sql;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findByFileName(String fileName);
}