package com.samsungproject.SamsungProject.repo;

import com.samsungproject.SamsungProject.models.Photo;
import org.springframework.data.repository.CrudRepository;

public interface PhotoRepository extends CrudRepository<Photo, Long> {
}
