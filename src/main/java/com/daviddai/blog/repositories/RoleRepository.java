package com.daviddai.blog.repositories;

import java.util.Optional;

import com.daviddai.blog.model.entities.Role;
import com.daviddai.blog.model.enums.ERole;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByName(ERole role);
}
