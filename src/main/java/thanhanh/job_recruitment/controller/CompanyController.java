package thanhanh.job_recruitment.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thanhanh.job_recruitment.domain.Company;
import thanhanh.job_recruitment.dto.request.CompanyRequest;
import thanhanh.job_recruitment.dto.response.CompanyResponse;
import thanhanh.job_recruitment.dto.response.ResultPagination;
import thanhanh.job_recruitment.service.CompanyService;

import java.util.List;
import java.util.Optional;

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
    public  ResponseEntity<ResultPagination> fetchAllCompany (
            @Filter Specification<Company> spec
            // @RequestParam(value = "current") Optional<String> currentOptional,
            // @RequestParam(value = "pageSize") Optional<String> pageSizeOptional
            )  {
//        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
//        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
//
//        int current =  Integer.parseInt(sCurrent);
//        int pageSize = Integer.parseInt(sPageSize);
//        Pageable pageable = PageRequest.of(current -1, pageSize);
        return ResponseEntity.ok().body(this.companyService.fetchAllCompany(spec));
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
