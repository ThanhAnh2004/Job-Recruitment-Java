package thanhanh.job_recruitment.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thanhanh.job_recruitment.service.EmailService;
import thanhanh.job_recruitment.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    @ApiMessage("Send simple email")
    public String sendSimpleEmail() {
        //return this.emailService.sendSimpleEmail();

        this.emailService.sendEmailFromTemplateSync("thanhanhdz2004@gmail.com","test send email",
                "test","thanhanh",null);

        return "ok";
    }
}
