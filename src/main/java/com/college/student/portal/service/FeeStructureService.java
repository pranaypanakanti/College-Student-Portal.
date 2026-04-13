package com.college.student.portal.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.college.student.portal.dto.FeeStructureDTO;
import com.college.student.portal.entity.FeeStructure;
import com.college.student.portal.repository.FeeStructureRepository;

@Service
public class FeeStructureService {

    private final FeeStructureRepository feeStructureRepository;

    public FeeStructureService(FeeStructureRepository feeStructureRepository) {
        this.feeStructureRepository = feeStructureRepository;
    }

    public ResponseEntity<Map<String, Object>> createFeeStructure(FeeStructureDTO dto) {
        if (feeStructureRepository.findByBranchAndSemester(dto.getBranch(), dto.getSemester()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Fee structure already exists for this branch/semester!"));
        }

        FeeStructure fs = new FeeStructure();
        fs.setBranch(dto.getBranch());
        fs.setSemester(dto.getSemester());
        fs.setTuitionFee(dto.getTuitionFee());
        fs.setHostelFee(dto.getHostelFee());
        fs.setLibraryFee(dto.getLibraryFee());
        fs.setLabFee(dto.getLabFee());
        fs.setTotalFee(dto.getTuitionFee() + dto.getHostelFee() + dto.getLibraryFee() + dto.getLabFee());
        fs.setDueDate(dto.getDueDate());
        feeStructureRepository.save(fs);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Fee structure created!", "totalFee", fs.getTotalFee()));
    }

    public List<FeeStructure> getAllFeeStructures() {
        return feeStructureRepository.findAll();
    }
}