package thanhanh.job_recruitment.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thanhanh.job_recruitment.service.EmailService;
import thanhanh.job_recruitment.service.SubscriberService;
import thanhanh.job_recruitment.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final SubscriberService subscriberService;


    @GetMapping
    @ApiMessage("Send simple email")
    public String sendSimpleEmail() {
        //return this.emailService.sendSimpleEmail();

//       this.emailService.sendEmailFromTemplateSync("thanhanh818757@gmail.com","test send email",
//              "job","thanhanh",null);

        this.subscriberService.sendSubscribersEmailJobs();

        return "ok";
    }
}
