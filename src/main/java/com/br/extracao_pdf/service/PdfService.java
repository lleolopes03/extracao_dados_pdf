package com.br.extracao_pdf.service;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Service
public class PdfService {
    public String extractTextFromPDF(MultipartFile file) throws IOException {
        Tika tika = new Tika();
        try {
            return tika.parseToString(file.getInputStream());
        } catch (TikaException e) {
            throw new IOException("Erro ao processar o PDF com Tika", e);
        } catch (Exception e) {
            throw new IOException("Erro inesperado ao processar o PDF", e);
        }
    }


}
