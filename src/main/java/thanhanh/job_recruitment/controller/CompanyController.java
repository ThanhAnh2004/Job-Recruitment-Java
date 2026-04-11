package thanhanh.job_recruitment.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thanhanh.job_recruitment.domain.Company;
import thanhanh.job_recruitment.dto.request.CompanyRequest;
import thanhanh.job_recruitment.dto.response.CompanyResponse;
import thanhanh.job_recruitment.service.CompanyService;

import java.util.List;

@RestController
@RequestMapping("/companies")
@AllArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/create")
    public ResponseEntity<CompanyResponse> createCompany (@Valid @RequestBody CompanyRequest companyRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.createCompany(companyRequest));
    }

    @GetMapping()
    public ResponseEntity<List<CompanyResponse>> fetchAllCompany ()  {
        return ResponseEntity.ok().body(this.companyService.fetchAllCompany());
    }

    @PutMapping("")
    ResponseEntity<CompanyResponse> updateCompany(@RequestBody Company company) {
        return ResponseEntity.ok().body(this.companyService.updateCompany(company));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCompany (@PathVariable("id") long id) {
        this.companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
