package thanhanh.job_recruitment.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import thanhanh.job_recruitment.domain.Company;
import thanhanh.job_recruitment.dto.request.CompanyRequest;
import thanhanh.job_recruitment.dto.response.CompanyResponse;
import thanhanh.job_recruitment.dto.response.Meta;
import thanhanh.job_recruitment.dto.response.ResultPagination;
import thanhanh.job_recruitment.repository.CompanyRepository;
import thanhanh.job_recruitment.service.CompanyService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;


    @Override
    public CompanyResponse createCompany(CompanyRequest companyRequest) {
        Company newCompany = Company.builder()
                .name(companyRequest.getName())
                .description(companyRequest.getDescription())
                .address(companyRequest.getAddress())
                .logo(companyRequest.getLogo())
                .build();

        this.companyRepository.save(newCompany);

        return this.mapperCompanyToCompanyResponse(newCompany);
    }

    @Override
    public ResultPagination fetchAllCompany(Specification<Company> spec) {
        List<Company> pageCompany = this.companyRepository.findAll(spec);
//
//        List<Company> allCompany = pageCompany.getContent();
//
       ResultPagination rs = new ResultPagination();
       Meta meta = new Meta();
//
//       meta.setPage(pageCompany.getNumber() + 1);
//       meta.setPages(pageCompany.getTotalPages());
//       meta.setPageSize(pageCompany.getSize());
//       meta.setTotal(pageCompany.getTotalElements());
//
       rs.setMeta(meta);
       rs.setResult(pageCompany);

       return rs;
    }

    @Override
    public CompanyResponse updateCompany(Company company) {
        Optional<Company> optionalCompany = this.companyRepository.findById(company.getId());

        if (optionalCompany.isPresent()) {
            Company currentCompany = optionalCompany.get();
            currentCompany.setName(company.getName());
            currentCompany.setAddress(company.getAddress());
            currentCompany.setDescription(company.getDescription());
            currentCompany.setLogo(company.getLogo());
            this.companyRepository.save(currentCompany);

            return mapperCompanyToCompanyResponse(currentCompany);
        }
        return null;
    }

    @Override
    public void deleteCompany(long id) {
        Optional<Company> companyOptional = this.companyRepository.findById(id);

        if (companyOptional.isPresent()) {
            this.companyRepository.delete(companyOptional.get());
        }
    }


    private CompanyResponse mapperCompanyToCompanyResponse (Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .address(company.getAddress())
                .description(company.getDescription())
                .logo(company.getLogo())
                .createBy(company.getCreatedBy())
                .createdAt(company.getCreatedAt())
                .updatedBy(company.getUpdatedBy())
                .updatedAt(company.getUpdatedAt())
                .build();
    }
}
