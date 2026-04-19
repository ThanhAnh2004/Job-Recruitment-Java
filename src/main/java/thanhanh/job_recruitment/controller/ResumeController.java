package thanhanh.job_recruitment.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thanhanh.job_recruitment.domain.Resume;
import thanhanh.job_recruitment.dto.response.ApiResponse.ResultPagination;
import thanhanh.job_recruitment.dto.response.Resume.CreateResumeResponse;
import thanhanh.job_recruitment.dto.response.Resume.FetchResumeResponse;
import thanhanh.job_recruitment.dto.response.Resume.UpdateResumeResponse;
import thanhanh.job_recruitment.service.ResumeService;
import thanhanh.job_recruitment.service.UserService;
import thanhanh.job_recruitment.util.annotation.ApiMessage;
import thanhanh.job_recruitment.util.exception.IdInvalidException;


@RestController
@RequestMapping("/api/v1/resumes")
@AllArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;
    private final UserService userService;

    @PostMapping("/resumes")
    @ApiMessage("Create a resume")
    public ResponseEntity<CreateResumeResponse> create(
            @Valid @RequestBody Resume resume
    ) throws IdInvalidException {
        // check id exists
        boolean isIdExist = this.resumeService.checkResumeExistByUserAndJob(resume);
        if (!isIdExist) {
            throw new IdInvalidException("User id/Job id not exists");
        }

        // create new resume
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.resumeService.create(resume));
    }

    @PutMapping("/resumes")
    @ApiMessage("Update a resume")
    public ResponseEntity<UpdateResumeResponse> update(@RequestBody Resume resume)
            throws IdInvalidException {
        // check resume exist by id
        boolean checkExists = this.resumeService.checkExistById(resume.getId());
        if (!checkExists) {
            throw new IdInvalidException("Mot found resume with id " + resume.getId());
        }

        return ResponseEntity.ok().body(this.resumeService.update(resume));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a resume by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id)
            throws IdInvalidException {
        // check resume exist by id
        boolean checkExists = this.resumeService.checkExistById(id);
        if (!checkExists) {
            throw new IdInvalidException("Mot found resume with id " + id);
        }

        this.resumeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Fetch a resume by id")
    public ResponseEntity<FetchResumeResponse> fetchById(
            @PathVariable("id") long id) throws IdInvalidException {
        // check resume exist by id
        boolean checkExists = this.resumeService.checkExistById(id);
        if (!checkExists) {
            throw new IdInvalidException("Mot found resume with id " + id);
        }

        return ResponseEntity
                .ok()
                .body(this.resumeService.fetchById(id));
    }

    @GetMapping
    @ApiMessage("Fetch all resume with paginate")
    public ResponseEntity<ResultPagination> fetchAll(
            @Filter Specification<Resume> spec,
            Pageable pageable
    ) {
        return ResponseEntity.ok().body(this.resumeService.fetchAllResume(spec, pageable));
    }
}
