package thanhanh.job_recruitment.service;

import thanhanh.job_recruitment.domain.Company;
import thanhanh.job_recruitment.dto.request.CompanyRequest;
import thanhanh.job_recruitment.dto.response.CompanyResponse;

import java.util.List;

public interface CompanyService {
    CompanyResponse createCompany (CompanyRequest companyRequest);
    List<CompanyResponse> fetchAllCompany ();
    CompanyResponse updateCompany (Company company);
    void deleteCompany (long id);
}
