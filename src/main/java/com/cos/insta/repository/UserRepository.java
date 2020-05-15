  
package com.cos.insta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cos.insta.model.User;


public interface UserRepository extends JpaRepository<User, Integer>{
	
	User findByUsername(String username);

	User findByProviderAndProviderId(String string, String id);
	
	List<User> findByUsernameContaining(String username);
}