package com.TLU.SoundVerse.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.dto.response.SearchResult;
import com.TLU.SoundVerse.service.SearchService;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

 @GetMapping
    public ResponseEntity<ApiResponse<SearchResult>> search(@RequestParam String keyword) {
        SearchResult result = searchService.search(keyword);
        
        return ResponseEntity.ok(
            ApiResponse.<SearchResult>builder()
                .code(200)
                .message("Search results retrieved successfully")
                .status("success")
                .data(result)
                .build()
        );
    }
}