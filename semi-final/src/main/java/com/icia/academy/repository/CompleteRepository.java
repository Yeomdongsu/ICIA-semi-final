package com.icia.academy.repository;

import com.icia.academy.entity.Complete;
import com.icia.academy.entity.Course;
import com.icia.academy.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CompleteRepository extends CrudRepository<Complete, Long> {
    Page<Complete> findByCpnumGreaterThan(long cpnum, Pageable pageable);
    List<Complete> findByCpcname(Course course);

//    List<Complete> findByCpmid(Course cname);
}
