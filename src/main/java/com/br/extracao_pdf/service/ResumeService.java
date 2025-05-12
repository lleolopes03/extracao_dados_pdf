package com.br.extracao_pdf.service;

import com.br.extracao_pdf.model.ResumeData;
import com.br.extracao_pdf.repository.ResumeRepository;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public ResumeData saveResumeData(ResumeData resumeData) {
        Optional<ResumeData> existingResume = resumeRepository.findByEmail(resumeData.getEmail());
        if (existingResume.isPresent()) {
            return existingResume.get();
        }
        return resumeRepository.save(resumeData);
    }


    public List<ResumeData> findByName(String name) {
        return resumeRepository.findByNameContaining(name);
    }



}
