package com.samsungproject.SamsungProject.repo;

import com.samsungproject.SamsungProject.models.Company;
import com.samsungproject.SamsungProject.models.Work;
import com.samsungproject.SamsungProject.models.Worker;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Set;

public interface WorkRepository extends CrudRepository<Work, Long> {
    Set<Work> findAllByWorkerAndCompanyAndStatus(Worker worker, Company company, String status);
    Set<Work> findAllByCompany(Company company, Sort sort);

    @Modifying
    @Transactional
    @Query("DELETE FROM Work work WHERE work.id = ?1 AND work.company = ?2")
    void deleteByIdAndCompany(Long id, Company company);
}
