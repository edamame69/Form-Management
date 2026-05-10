package com.topcv.form.repository;

import com.topcv.form.entity.Form;
import com.topcv.form.enums.FormStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    List<Form> findByStatusOrderByDisplayOrderAsc(FormStatus status);
}