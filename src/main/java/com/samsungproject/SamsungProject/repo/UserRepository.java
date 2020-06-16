package com.samsungproject.SamsungProject.repo;

import com.samsungproject.SamsungProject.models.Company;
import com.samsungproject.SamsungProject.models.Invite;
import com.samsungproject.SamsungProject.models.User;
import com.samsungproject.SamsungProject.models.Worker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT user_table FROM User user_table " +
            "JOIN Worker worker_table ON worker_table.user = user_table AND worker_table.company IS NULL " +
            "WHERE worker_table NOT IN (SELECT invite_table.worker FROM Invite invite_table WHERE invite_table.company = ?1)")
    Set<User> findAllByWorkerIsNullAndInviteCompany(Company company);

    @Query("SELECT user_table FROM User user_table " +
            "JOIN Director director_table ON director_table.user = user_table " +
            "JOIN Company company_table ON company_table.director = director_table " +
            "JOIN Invite invite_table ON invite_table.company = company_table AND invite_table.worker = ?1 AND invite_table.status = ?2")
    Set<User> findUserByWorkerAndInviteStatus(Worker worker, String status);


    @Query("SELECT user_table FROM User user_table " +
            "JOIN Worker worker_table " +
            "ON worker_table.company = ?1 AND worker_table.user = user_table")
    Set<User> findUsersByCompany(Company company);

}
