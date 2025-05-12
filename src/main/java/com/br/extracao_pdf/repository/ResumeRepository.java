package com.br.extracao_pdf.repository;

import com.br.extracao_pdf.model.ResumeData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<ResumeData,Long>{
    List<ResumeData> findByNameContaining(String name);
    Optional<ResumeData> findByEmail(String email);

}

