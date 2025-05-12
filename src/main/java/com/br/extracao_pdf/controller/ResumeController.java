package com.br.extracao_pdf.controller;

import com.br.extracao_pdf.model.ResumeData;
import com.br.extracao_pdf.service.PdfService;
import com.br.extracao_pdf.service.ResumeNlpService;
import com.br.extracao_pdf.service.ResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/resume")

public class ResumeController {

    private final PdfService pdfService;
    private final ResumeNlpService resumeNlpService;
    private final ResumeService resumeService;

    public ResumeController(PdfService pdfService, ResumeNlpService resumeNlpService,ResumeService resumeService) {
        this.pdfService = pdfService;
        this.resumeNlpService = resumeNlpService;
        this.resumeService=resumeService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Arquivo vazio");
        }

        try {
            String extractedText = pdfService.extractTextFromPDF(file);
            ResumeData resumeData = resumeNlpService.processText(extractedText);
            return ResponseEntity.ok("Currículo processado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar arquivo");
        }
    }
    @GetMapping("/search")
    public ResponseEntity<List<ResumeData>> searchResumes(@RequestParam String name) {
        List<ResumeData> resumes = resumeService.findByName(name);
        return ResponseEntity.ok(resumes);
    }


}
