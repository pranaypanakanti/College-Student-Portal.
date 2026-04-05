package com.college.student.portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.college.student.portal.entity.Announcement;
import com.college.student.portal.enums.TargetAudience;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {

    List<Announcement> findByTargetAudience(TargetAudience targetAudience);
}