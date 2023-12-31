package com.danram.server.repository;

import com.danram.server.domain.member.MemberName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberNameRepository extends JpaRepository<MemberName, Long> {
}
