package thanhanh.job_recruitment.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import thanhanh.job_recruitment.domain.Resume;
import thanhanh.job_recruitment.dto.response.ApiResponse.ResultPagination;
import thanhanh.job_recruitment.dto.response.Resume.CreateResumeResponse;
import thanhanh.job_recruitment.dto.response.Resume.FetchResumeResponse;
import thanhanh.job_recruitment.dto.response.Resume.UpdateResumeResponse;

public interface ResumeService {
    boolean checkResumeExistByUserAndJob(Resume resume);
    CreateResumeResponse create(Resume resume);
    UpdateResumeResponse update(Resume resume);
    FetchResumeResponse fetchById(long id);
    void deleteById(long id);
    boolean checkExistById(long id);
    ResultPagination fetchAllResume(Specification<Resume> spec, Pageable pageable);
}
