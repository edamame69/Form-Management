package com.topcv.form.repository;

import com.topcv.form.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {

    List<Field> findByFormIdOrderByFieldOrderAsc(Long formId);
}