package ma.cimr.agmbackend.returnedmail;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;
import ma.cimr.agmbackend.member.Member;
import ma.cimr.agmbackend.member.MemberRepository;
import ma.cimr.agmbackend.reason.ReasonRepository;

@Service
@RequiredArgsConstructor
public class ReturnedMailServiceImpl implements ReturnedMailService {

    private final ReturnedMailRepository returnedMailRepository;
    private final MemberRepository memberRepository;
    private final ReasonRepository reasonRepository;
    private final ReturnedMailMapper returnedMailMapper;

    @Override
    public ReturnedMailResponse createReturnedMail(ReturnedMailRequest request) {
        Member member = memberRepository.findByMemberNumber(request.getMemberNumber())
                .orElseThrow(() -> new ApiException(ApiExceptionCodes.MEMBER_NOT_FOUND));

        ReturnedMail returnedMail = ReturnedMail.builder()
                .member(member)
                .returnDate(request.getReturnDate())
                .reason(reasonRepository.findById(request.getReasonId())
                        .orElseThrow(() -> new ApiException(ApiExceptionCodes.REASON_NOT_FOUND)))
                .build();

        returnedMailRepository.save(returnedMail);

        return returnedMailMapper.toResponse(returnedMail);
    }

    @Override
    public List<ReturnedMailResponse> getReturnedMails() {
        return returnedMailRepository.findAll().stream()
                .map(returnedMailMapper::toResponse)
                .collect(Collectors.toList());
    }
}
