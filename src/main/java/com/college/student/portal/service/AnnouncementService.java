package com.college.student.portal.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.college.student.portal.dto.AnnouncementDTO;
import com.college.student.portal.dto.AnnouncementResponseDTO;
import com.college.student.portal.entity.Admin;
import com.college.student.portal.entity.Announcement;
import com.college.student.portal.enums.TargetAudience;
import com.college.student.portal.repository.AdminRepository;
import com.college.student.portal.repository.AnnouncementRepository;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final AdminRepository adminRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository, AdminRepository adminRepository) {
        this.announcementRepository = announcementRepository;
        this.adminRepository = adminRepository;
    }

    public ResponseEntity<Map<String, Object>> createAnnouncement(AnnouncementDTO dto, int adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found!"));

        Announcement a = new Announcement();
        a.setTitle(dto.getTitle());
        a.setDescription(dto.getDescription());
        a.setCreatedBy(admin);
        a.setTargetAudience(dto.getTargetAudience());
        a.setExpiresAt(dto.getExpiresAt());
        announcementRepository.save(a);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Announcement created!"));
    }

    public List<AnnouncementResponseDTO> getAnnouncements(TargetAudience target) {
        return announcementRepository.findByTargetAudience(target).stream()
                .map(a -> new AnnouncementResponseDTO(
                        a.getId(), a.getTitle(), a.getDescription(),
                        a.getTargetAudience().name(), a.getCreatedBy().getName(),
                        a.getCreatedAt().toLocalDate(), a.getExpiresAt()))
                .toList();
    }

    public List<AnnouncementResponseDTO> getAllAnnouncements() {
        return announcementRepository.findAll().stream()
                .map(a -> new AnnouncementResponseDTO(
                        a.getId(), a.getTitle(), a.getDescription(),
                        a.getTargetAudience().name(), a.getCreatedBy().getName(),
                        a.getCreatedAt().toLocalDate(), a.getExpiresAt()))
                .toList();
    }
}