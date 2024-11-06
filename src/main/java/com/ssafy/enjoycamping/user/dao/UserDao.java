package com.ssafy.enjoycamping.user.dao;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.enjoycamping.user.entity.User;

@Mapper
public interface UserDao {
	@Transactional(readOnly = true)
	List<User> selectAll();
	@Transactional(readOnly = true)
	Optional<User> selectById(int id);
	@Transactional(readOnly = true)
	Optional<User> selectActiveById(int id);
	@Transactional(readOnly = true)
	Optional<User> selectByEmail(String email);
	@Transactional(readOnly = true)
	Optional<User> selectActiveByEmail(String email);
	@Transactional
	void insert(User user);
	@Transactional
	void update(User user);
}
