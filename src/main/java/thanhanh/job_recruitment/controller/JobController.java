package thanhanh.job_recruitment.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thanhanh.job_recruitment.dto.request.Job.CreateJobRequest;
import thanhanh.job_recruitment.dto.request.Job.UpdateJobRequest;
import thanhanh.job_recruitment.dto.response.Job.JobResponse;
import thanhanh.job_recruitment.service.JobService;
import thanhanh.job_recruitment.util.annotation.ApiMessage;
import thanhanh.job_recruitment.util.exception.IdInvalidException;

@RestController
@RequestMapping("/api/v1/jobs")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobController {
    JobService jobService;

    @PostMapping("/create")
    @ApiMessage("Create a new job")
    public ResponseEntity<JobResponse> createJob(@RequestBody CreateJobRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.createJob(request));
    }

    @PostMapping("/update")
    @ApiMessage(("Update a job"))
    public ResponseEntity<JobResponse> updateJob(@Valid @RequestBody UpdateJobRequest request) throws IdInvalidException {
        boolean checkExists = this.jobService.checkExistsById(request.getId());

        if (!checkExists) {
            throw new IdInvalidException("Not found job with id " + request.getId());
        }

        return ResponseEntity.ok().body(this.jobService.updateJob(request));
    }

    @GetMapping("/{id}")
    @ApiMessage("Fetch a job by id")
    public ResponseEntity<JobResponse> fetchById(@PathVariable("id") long id) throws IdInvalidException {
        boolean checkExists = this.jobService.checkExistsById(id);

        if (!checkExists) {
            throw new IdInvalidException("Not found job with id " + id);
        }

        return ResponseEntity.ok().body(this.jobService.fetchJobById(id));
    }

    @DeleteMapping("{id}")
    @ApiMessage("Delete a job by id")
    public ResponseEntity<JobResponse> deleteById(@PathVariable("id") long id) throws IdInvalidException {
        boolean checkExists = this.jobService.checkExistsById(id);

        if (!checkExists) {
            throw new IdInvalidException("Not found job with id " + id);
        }

        this.jobService.deleteJobById(id);

        return ResponseEntity.noContent().build();
    }

}
