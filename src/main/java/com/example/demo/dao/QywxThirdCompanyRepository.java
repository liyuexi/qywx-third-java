package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QywxThirdCompanyRepository extends JpaRepository<QywxThirdCompany,Integer> {

    Optional<QywxThirdCompany> findTopByOrderByIdDesc();

}
