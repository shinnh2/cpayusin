package com.jbaacount.visitor.controller;

import com.jbaacount.global.dto.SingleResponseDto;
import com.jbaacount.visitor.dto.VisitorResponseDto;
import com.jbaacount.visitor.service.VisitorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/visitor")
@RequiredArgsConstructor
public class VisitorController
{
    private final VisitorService visitorService;

    @GetMapping
    public ResponseEntity getVisitors()
    {
        VisitorResponseDto response = visitorService.getVisitorResponse();

        return ResponseEntity.ok(new SingleResponseDto<>(response));
    }
}
