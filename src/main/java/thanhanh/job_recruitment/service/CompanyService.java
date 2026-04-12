package thanhanh.job_recruitment.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import thanhanh.job_recruitment.domain.Company;
import thanhanh.job_recruitment.dto.request.CompanyRequest;
import thanhanh.job_recruitment.dto.response.CompanyResponse;
import thanhanh.job_recruitment.dto.response.ResultPagination;

import java.util.List;

public interface CompanyService {
    CompanyResponse createCompany (CompanyRequest companyRequest);
    ResultPagination fetchAllCompany (Specification<Company> spec);
    CompanyResponse updateCompany (Company company);
    void deleteCompany (long id);
}
