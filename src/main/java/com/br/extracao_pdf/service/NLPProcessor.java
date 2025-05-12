package com.br.extracao_pdf.service;

import org.springframework.stereotype.Service;

@Service
public class NLPProcessor {
    public String extractEntity(String text, String entityType) {
        // Aqui você pode integrar com spaCy, Stanford NLP, ou Hugging Face
        return "Entidade identificada: " + entityType;
    }

}
