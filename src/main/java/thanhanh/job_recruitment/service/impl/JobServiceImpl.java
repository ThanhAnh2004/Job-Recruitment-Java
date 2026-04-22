package thanhanh.job_recruitment.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import thanhanh.job_recruitment.domain.Company;
import thanhanh.job_recruitment.domain.Job;
import thanhanh.job_recruitment.domain.Skill;
import thanhanh.job_recruitment.dto.request.Job.CreateJobRequest;
import thanhanh.job_recruitment.dto.request.Job.UpdateJobRequest;
import thanhanh.job_recruitment.dto.response.ApiResponse.Meta;
import thanhanh.job_recruitment.dto.response.ApiResponse.ResultPagination;
import thanhanh.job_recruitment.dto.response.Job.JobResponse;
import thanhanh.job_recruitment.repository.CompanyRepository;
import thanhanh.job_recruitment.repository.JobRepository;
import thanhanh.job_recruitment.repository.SkillRepository;
import thanhanh.job_recruitment.service.JobService;
import thanhanh.job_recruitment.util.exception.IdInvalidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;


    @Override
    public JobResponse createJob(CreateJobRequest request) {

        List<Skill> listSkills = new ArrayList<>();
      // Check skills
        if (request.getSkills() != null) {
            List<Long> listSkillId = request.getSkills()
                    .stream().map(x -> x.getId()).toList();

            listSkills = this.skillRepository.findByIdIn(listSkillId);
        }

        // Check company
        Company company = new Company();
        if(request.getCompany() != null) {
            Optional<Company> companyOptional = this.companyRepository.findById(request.getCompany().getId());
            if (companyOptional.isPresent()) {
                company = companyOptional.get();
            }

        }

        Job newJob = Job.builder()
                .name(request.getName())
                .location(request.getLocation())
                .salary(request.getSalary())
                .quantity(request.getQuantity())
                .level(request.getLevel())
                .active(request.isActive())
                .description(request.getDescription())
                .company(company)
                .skills(listSkills)
                .build();

        this.jobRepository.save(newJob);

        return this.mapperJobToJobResponse(newJob);
    }

    @Override
    public JobResponse updateJob(UpdateJobRequest request) {
        Job currentJob = this.jobRepository.findById(request.getId()).get();

        currentJob.setName(request.getName());
        currentJob.setLocation(request.getLocation());
        currentJob.setSalary(request.getSalary());
        currentJob.setQuantity(request.getQuantity());
        currentJob.setLevel(request.getLevel());
        currentJob.setActive(request.isActive());
        currentJob.setDescription(request.getDescription());
        currentJob.setStartDate(request.getStartDate());
        currentJob.setEndDate(request.getEndDate());
        currentJob.setCompany(request.getCompany());
        currentJob.setSkills(request.getSkills());

        this.jobRepository.save(currentJob);

        return this.mapperJobToJobResponse(currentJob);

    }

    @Override
    public boolean checkExistsById(long id) {
        return this.jobRepository.existsById(id);
    }

    @Override
    public JobResponse fetchJobById(long id) {
       Job  currentJob = this.jobRepository.findById(id).get();

        return this.mapperJobToJobResponse(currentJob);
    }

    @Override
    public ResultPagination fetchAllJob(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);

        List<JobResponse> pageJobResponse = pageJob.getContent().stream().map(job ->
                this.mapperJobToJobResponse(job)).toList();

        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(pageJob.getTotalPages())
                .total(pageJob.getTotalElements())
                .build();

        return ResultPagination.builder()
                .meta(meta)
                .result(pageJobResponse)
                .build();
    }

    @Override
    public void deleteJobById(long id) {
        this.jobRepository.deleteById(id);
    }

    private JobResponse mapperJobToJobResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .name(job.getName())
                .location(job.getLocation())
                .salary(job.getSalary())
                .quantity(job.getQuantity())
                .level(job.getLevel())
                .active(job.isActive())
                .description(job.getDescription())
                .startDate(job.getStartDate())
                .endDate(job.getEndDate())
                .companies(job.getCompany())
                .skills(job.getSkills())
                .createdAt(job.getCreatedAt())
                .createBy(job.getCreatedBy())
                .updatedAt(job.getUpdatedAt())
                .updatedBy(job.getUpdatedBy())
                .build();
    }
}
