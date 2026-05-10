package com.topcv.form.repository;

import com.topcv.form.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByFormIdOrderBySubmittedAtDesc(Long formId);

    List<Submission> findAllByOrderBySubmittedAtDesc();
}