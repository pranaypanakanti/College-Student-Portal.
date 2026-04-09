package com.college.student.portal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.college.student.portal.dto.AnnouncementDTO;
import com.college.student.portal.dto.AnnouncementResponseDTO;
import com.college.student.portal.enums.TargetAudience;
import com.college.student.portal.service.AnnouncementService;

import jakarta.validation.Valid;

@RestController
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @PostMapping("/api/admin/announcements/{adminId}")
    public ResponseEntity<Map<String, Object>> create(
            @PathVariable int adminId, @Valid @RequestBody AnnouncementDTO dto) {
        return announcementService.createAnnouncement(dto, adminId);
    }

    @GetMapping("/api/announcements")
    public List<AnnouncementResponseDTO> getAll() {
        return announcementService.getAllAnnouncements();
    }

    @GetMapping("/api/announcements/filter")
    public List<AnnouncementResponseDTO> getByTarget(@RequestParam TargetAudience target) {
        return announcementService.getAnnouncements(target);
    }
}