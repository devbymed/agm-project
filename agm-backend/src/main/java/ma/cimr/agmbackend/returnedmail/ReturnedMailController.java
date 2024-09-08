package ma.cimr.agmbackend.returnedmail;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.util.ApiResponse;
import ma.cimr.agmbackend.util.ApiResponseFormatter;

@RestController
@RequestMapping("/returned-mails")
@RequiredArgsConstructor
public class ReturnedMailController {

    private final ReturnedMailService returnedMailService;

    @PostMapping
    public ResponseEntity<ApiResponse> createReturnedMail(@RequestBody ReturnedMailRequest request) {
        ReturnedMailResponse returnedMail = returnedMailService.createReturnedMail(request);
        return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Retour de courrier ajouté avec succès",
                returnedMail);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllReturnedMails() {
        List<ReturnedMailResponse> returnedMails = returnedMailService.getReturnedMails();
        return ApiResponseFormatter.generateResponse(HttpStatus.OK, returnedMails);
    }
}
