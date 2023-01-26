package com.icia.academy.repository;

import com.icia.academy.entity.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, String> {
    Member findByMid(String mid);
}
