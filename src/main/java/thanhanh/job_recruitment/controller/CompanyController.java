package thanhanh.job_recruitment.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thanhanh.job_recruitment.domain.Company;
import thanhanh.job_recruitment.dto.request.Company.CompanyRequest;
import thanhanh.job_recruitment.dto.response.Company.CompanyResponse;
import thanhanh.job_recruitment.dto.response.ApiResponse.ResultPagination;
import thanhanh.job_recruitment.service.CompanyService;
import thanhanh.job_recruitment.util.annotation.ApiMessage;


@RestController
@RequestMapping("/api/v1/companies")
@AllArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/create")
    public ResponseEntity<CompanyResponse> createCompany (@Valid @RequestBody CompanyRequest companyRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.createCompany(companyRequest));
    }

    @GetMapping()
    @ApiMessage("Fetch all company")
    public  ResponseEntity<ResultPagination> fetchAllCompany (
            @Filter Specification<Company> spec,
            Pageable pageable
            )  {
        return ResponseEntity.ok().body(this.companyService.fetchAllCompany(spec, pageable));
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
