package com.samsungproject.SamsungProject.repo;

import com.samsungproject.SamsungProject.models.Company;
import com.samsungproject.SamsungProject.models.User;
import com.samsungproject.SamsungProject.models.Worker;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface WorkerRepository extends CrudRepository<Worker, Long> {
    Set<Worker> findWorkersByCompanyIsNull();
    Worker findByCompanyAndUser(Company company, User user);
}
