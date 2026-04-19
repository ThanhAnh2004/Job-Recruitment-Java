package thanhanh.job_recruitment.service;

import thanhanh.job_recruitment.dto.request.Job.CreateJobRequest;
import thanhanh.job_recruitment.dto.request.Job.UpdateJobRequest;
import thanhanh.job_recruitment.dto.response.Job.JobResponse;

public interface JobService {
    JobResponse createJob (CreateJobRequest request);
    JobResponse updateJob (UpdateJobRequest request);
    boolean checkExistsById (long id);
    JobResponse fetchJobById (long id);
    void deleteJobById(long id);
}
