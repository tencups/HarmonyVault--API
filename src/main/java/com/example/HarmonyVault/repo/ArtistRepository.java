package com.example.HarmonyVault.repo;

import com.example.HarmonyVault.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/*
 * ArtistRepository is a repository interface that enables easy and efficient CRUD operations on Artist entities, leveraging the power of Spring Data JPA.

 * Artist is the entity type
   String is type of identifier, id

    
 */
@Repository
public interface ArtistRepository extends JpaRepository<Artist, String> {

}


/*
 * Inherits 
save(Contact entity): Saves a given Contact entity.
findById(String id): Retrieves an entity by its ID.
findAll(): Returns all entities.
deleteById(String id): Deletes an entity by its ID.
existsById(String id): Checks if an entity with the given ID exists.
 * 
 */