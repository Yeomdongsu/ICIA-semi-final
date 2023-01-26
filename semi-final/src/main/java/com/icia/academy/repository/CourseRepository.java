package com.icia.academy.repository;

import com.icia.academy.entity.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, String> {
    Course findByCname(String cname);
}
