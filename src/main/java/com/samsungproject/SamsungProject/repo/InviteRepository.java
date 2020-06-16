package com.samsungproject.SamsungProject.repo;

import com.samsungproject.SamsungProject.models.Company;
import com.samsungproject.SamsungProject.models.Invite;
import com.samsungproject.SamsungProject.models.User;
import com.samsungproject.SamsungProject.models.Worker;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Set;

public interface InviteRepository extends CrudRepository<Invite, Long> {
    Invite findByWorkerAndCompanyOrderByIdDesc(Worker worker, Company company);
    Set<Invite> findAllByWorkerAndStatus(Worker worker, String status);
}
