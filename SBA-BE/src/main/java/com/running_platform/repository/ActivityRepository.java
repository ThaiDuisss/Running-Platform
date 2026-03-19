package com.running_platform.repository;


import com.running_platform.entity.RunActivities.RunActivity;
import com.running_platform.entity.UserAuth.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ActivityRepository extends JpaRepository<RunActivity, Long>, PagingAndSortingRepository<RunActivity, Long> {


    Page<RunActivity> findByUser_Id(Long userId, Pageable pageable);

}

