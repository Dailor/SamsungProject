package com.samsungproject.SamsungProject.repo;

import com.samsungproject.SamsungProject.models.Company;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, Long> {
    Company findByName(String name);
}
