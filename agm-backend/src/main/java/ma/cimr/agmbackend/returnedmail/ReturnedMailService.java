package ma.cimr.agmbackend.returnedmail;

import java.util.List;

public interface ReturnedMailService {
    ReturnedMailResponse createReturnedMail(ReturnedMailRequest request);

    List<ReturnedMailResponse> getReturnedMails();
}
