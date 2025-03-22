package com.TLU.SoundVerse.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TLU.SoundVerse.dto.request.SignContractDto;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.dto.response.ContractResponse;
import com.TLU.SoundVerse.entity.Contract;
import com.TLU.SoundVerse.service.ContractService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@RestController
@RequestMapping("contract")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContractController {
  ContractService contractService;

  @GetMapping
  ApiResponse<ContractResponse> signContract(HttpServletRequest request) {
    @SuppressWarnings("unchecked")
    Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
    Integer id = Integer.parseInt(String.valueOf(user.get("id")));
    ContractResponse contract = contractService.getContract(id);

    ApiResponse<ContractResponse> apiResponse = new ApiResponse<ContractResponse>();
    apiResponse.setStatus("success");
    apiResponse.setMessage("Sign contract successfilly");
    apiResponse.setData(contract);
    return apiResponse;
  }

  @PostMapping("/sign-contract")
  ApiResponse<Contract> signContract(HttpServletRequest request, @RequestBody SignContractDto dto) throws IOException {
    @SuppressWarnings("unchecked")
    Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
    Integer id = Integer.parseInt(String.valueOf(user.get("id")));
    Contract isSigned = contractService.create(id, dto);

    ApiResponse<Contract> apiResponse = new ApiResponse<Contract>();
    apiResponse.setStatus("success");
    apiResponse.setMessage("Sign contract successfilly");
    apiResponse.setData(isSigned);
    return apiResponse;
  }
}
