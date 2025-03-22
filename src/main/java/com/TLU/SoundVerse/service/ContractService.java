package com.TLU.SoundVerse.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import com.TLU.SoundVerse.dto.request.SignContractDto;
import com.TLU.SoundVerse.dto.response.AlbumResponse;
import com.TLU.SoundVerse.dto.response.ContractResponse;
import com.TLU.SoundVerse.entity.Contract;
import com.TLU.SoundVerse.repository.ContractRepository;

import lombok.AccessLevel;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContractService {
  ContractRepository contractRepository;
  ArtistService artistService;
  TemplateEngine templateEngine;
  S3Service s3Service;

  public ContractResponse getContract(Integer userId) {
    Contract contract = contractRepository.findByUserId(userId);
    return toContractResponse(contract);
  }

  public Contract create(Integer userId, SignContractDto dto) throws IOException {
    Contract contract = new Contract();

    contract.setContractNumber(dto.getContractNumber());
    contract.setContractDate(dto.getContractDate());
    contract.setUsername(dto.getUsername());
    contract.setAddress(dto.getAddress());
    contract.setPhone(dto.getPhone());
    contract.setEmail(dto.getEmail());
    contract.setSignature(dto.getSignature());
    contract.setUserId(userId);
    artistService.signContract(userId);

    Map<String, Object> variables = new HashMap<>();
    variables.put("contractNumber", dto.getContractNumber());
    variables.put("contractDate", dto.getContractDate());
    variables.put("username", dto.getUsername());
    variables.put("address", dto.getAddress());
    variables.put("phone", dto.getPhone());
    variables.put("email", dto.getEmail());
    variables.put("signature", dto.getSignature());

    String htmlContent = generateHtmlFromTemplate(variables);
    byte[] pdfContent = generatePdfFromHtml(htmlContent);

    String fileUrl = s3Service.uploadFile(pdfContent, "contract_" + userId + ".pdf", userId);
    contract.setFilePath(fileUrl);

    return contractRepository.save(contract);
  }

  public String generateHtmlFromTemplate(Map<String, Object> variables) throws IOException {
    Context context = new Context();
    variables.forEach(context::setVariable);

    return templateEngine.process("contract_template", context);
  }

  public void generatePdfFromHtmlFile(String htmlFilePath, String pdfFilePath) throws IOException {
    File htmlFile = new File(htmlFilePath);
    File pdfFile = new File(pdfFilePath);

    try (OutputStream outputStream = new FileOutputStream(pdfFile)) {
      PdfRendererBuilder builder = new PdfRendererBuilder();
      builder.useFastMode();
      builder.withUri(htmlFile.toURI().toString()); // Sử dụng file đã lưu
      builder.toStream(outputStream);
      builder.run();
    }

    System.out.println("File PDF đã được tạo tại: " + pdfFile.getAbsolutePath());
  }

  public byte[] generatePdfFromHtml(String htmlContent) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PdfRendererBuilder builder = new PdfRendererBuilder();
    builder.useFastMode();
    builder.withHtmlContent(htmlContent, null);
    builder.toStream(outputStream);
    builder.run();
    return outputStream.toByteArray();
  }

  public ContractResponse toContractResponse(Contract contract) {
    return ContractResponse.builder()
      .contractNumber(contract.getContractNumber())
      .contractDate(contract.getContractDate())
      .username(contract.getUsername())
      .address(contract.getAddress())
      .phone(contract.getPhone())
      .email(contract.getEmail())
      .signature(contract.getSignature())
      .build();
  }
}
