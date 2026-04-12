package thanhanh.job_recruitment.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Meta {
    int page;
    int pageSize;
    int pages;
    long total;
}
