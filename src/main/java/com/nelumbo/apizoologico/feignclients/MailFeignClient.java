package com.nelumbo.apizoologico.feignclients;

import com.nelumbo.apizoologico.feignclients.dto.MailDtoReq;
import com.nelumbo.apizoologico.services.dto.res.MessageDtoRes;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "correos", url="http://localhost:8081/mails")
public interface MailFeignClient {
    @PostMapping("/send")
    MessageDtoRes sendMail(@Valid @RequestBody MailDtoReq mailDtoReq);
}