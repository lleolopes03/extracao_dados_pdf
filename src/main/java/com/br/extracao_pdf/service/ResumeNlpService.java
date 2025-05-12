package com.br.extracao_pdf.service;

import com.br.extracao_pdf.model.ResumeData;
import com.br.extracao_pdf.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service

public class ResumeNlpService {
    private final ResumeRepository resumeRepository;
    private final NLPProcessor nlpProcessor;

    public ResumeNlpService(ResumeRepository resumeRepository, NLPProcessor nlpProcessor) {
        this.resumeRepository = resumeRepository;
        this.nlpProcessor = nlpProcessor;
    }

    public ResumeData processText(String text) {
        ResumeData data = new ResumeData();

        data.setEmail(extractPattern(text, "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b"));
        data.setPhone(extractPattern(text, "\\+?\\d{2,3}\\s?\\d{2}\\s?\\d{4,5}-?\\d{4}"));

        data.setName(extractNameFromText(text)); // Agora pega o nome diretamente pelo texto

        data.setEducation(nlpProcessor.extractEntity(text, "EDUCATION"));
        data.setExperience(nlpProcessor.extractEntity(text, "WORK"));

        if (data.getEducation().equalsIgnoreCase("Entidade identificada: EDUCATION")) {
            data.setEducation(extractEducationFromText(text)); // Fallback manual para Educação
        }
        if (data.getExperience().equalsIgnoreCase("Entidade identificada: WORK")) {
            data.setExperience(extractExperienceFromText(text)); // Fallback manual para Experiência
        }

        return resumeRepository.save(data);
    }

    private String extractPattern(String text, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        return m.find() ? m.group() : "Não encontrado";
    }

    private String extractNameFromText(String text) {
        String[] linhas = text.split("\n"); // Divide o texto por linhas

        for (int i = 0; i < linhas.length; i++) {
            String linha = linhas[i].trim();

            // Verifica se a linha contém apenas nome e sobrenome corretamente capitalizados
            if (linha.matches("^[A-Z][a-z]+\\s[A-Z][a-z]+$")) {
                return linha; // Retorna a linha que parece um nome
            }

            // Se houver "Desenvolvedor de software", tenta pegar a linha anterior
            if (linha.contains("Desenvolvedor de software") && i > 0) {
                String nomePossivel = linhas[i - 1].trim();
                if (nomePossivel.matches("^[A-Z][a-z]+\\s[A-Z][a-z]+$")) {
                    return nomePossivel; // Se a linha anterior parecer um nome, retorna
                }
            }
        }
        return "Nome não identificado";

    }
    private String extractEducationFromText(String text) {
        String[] linhas = text.split("\n");

        for (String linha : linhas) {
            if (linha.toLowerCase().contains("formação") || linha.toLowerCase().contains("educação") || linha.toLowerCase().contains("universidade") || linha.toLowerCase().contains("graduação")) {
                return linha.trim(); // Retorna a linha onde está a formação acadêmica
            }
        }
        return "Não identificado";
    }
    private String extractExperienceFromText(String text) {
        String[] linhas = text.split("\n");

        for (String linha : linhas) {
            if (linha.toLowerCase().contains("experiência") || linha.toLowerCase().contains("cargo") || linha.toLowerCase().contains("empresa") || linha.toLowerCase().contains("trabalho") || linha.toLowerCase().contains("projetos")) {
                return linha.trim(); // Retorna a linha que indica experiência profissional
            }
        }
        return "Não identificado";
    }


}
