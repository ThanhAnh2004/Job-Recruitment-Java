package thanhanh.job_recruitment.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import thanhanh.job_recruitment.domain.Skill;
import thanhanh.job_recruitment.domain.Subscriber;
import thanhanh.job_recruitment.repository.SkillRepository;
import thanhanh.job_recruitment.repository.SubscriberRepository;
import thanhanh.job_recruitment.service.SubscriberService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriberServiceImpl implements SubscriberService {

    SubscriberRepository subscriberRepository;
    SkillRepository skillRepository;

    @Override
    public boolean isExistsByEmail(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }

    @Override
    public Subscriber createSubscriber(Subscriber subscriber) {
        Subscriber newSubscriber = new Subscriber();

        // Check skill
        if (subscriber.getSkills() != null) {
            List<Long> listSkillId = subscriber.getSkills().stream().map(x -> x.getId()).toList();
            List<Skill> listSkills = this.skillRepository.findByIdIn(listSkillId);
            newSubscriber.setSkills(listSkills);
        }

        subscriber.setEmail(subscriber.getEmail());
        subscriber.setName(subscriber.getName());

        this.subscriberRepository.save(newSubscriber);

        return newSubscriber;
    }

    @Override
    public Subscriber updateSubscriber(Subscriber subsDB, Subscriber subsRequest) {
        // check skills
        if (subsRequest.getSkills() != null) {
            List<Long> reqSkills = subsRequest
                    .getSkills()
                    .stream()
                    .map(x -> x.getId())
                    .toList();

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            subsDB.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(subsDB);
    }

    @Override
    public Subscriber findById(long id) {
        Optional<Subscriber> subscriberOptional =
                this.subscriberRepository.findById(id);
        if (subscriberOptional.isPresent()) {
            return subscriberOptional.get();
        }
        return null;
    }


}
