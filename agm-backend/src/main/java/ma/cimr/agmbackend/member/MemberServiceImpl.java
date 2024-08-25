package ma.cimr.agmbackend.member;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.assembly.Assembly;
import ma.cimr.agmbackend.assembly.AssemblyRepository;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final AssemblyRepository assemblyRepository;
	private final MemberMapper memberMapper;

	@Override
	public List<MemberResponse> getEligibleMembers() {
		// Récupérer l'assemblée en cours (celle qui n'est pas fermée)
		Assembly currentAssembly = assemblyRepository.findByClosed(false)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.CURRENT_ASSEMBLY_NOT_FOUND));

		LocalDate agDate = currentAssembly.getDay();
		LocalDate firstDayOfConvocationMonth = agDate.withDayOfMonth(1).minusMonths(1);
		LocalDate contributionDeadline = firstDayOfConvocationMonth.minusMonths(6);

		return memberRepository.findAll().stream()
				.filter(member -> isEligible(member, contributionDeadline))
				.map(memberMapper::toResponse)
				.collect(Collectors.toList());
	}

	private boolean isEligible(Member member, LocalDate contributionDeadline) {
		if (member.getType().equals("1")) {
			return isType1Eligible(member, contributionDeadline);
		} else {
			return isType2To4Eligible(member, contributionDeadline);
		}
	}

	private boolean isType1Eligible(Member member, LocalDate contributionDeadline) {
		// Logique pour vérifier si les contributions sont à jour
		// Et si le nombre d'affiliés est supérieur à 50
		return member.getDtrYear() >= contributionDeadline.getYear() &&
				member.getWorkforce() > 50;
	}

	private boolean isType2To4Eligible(Member member, LocalDate contributionDeadline) {
		// Logique pour vérifier les versements forfaitaires
		// Et si le nombre d'affiliés est supérieur à 50
		return member.getDtrYear() >= contributionDeadline.getYear() &&
				member.getWorkforce() > 50;
	}

	@Override
	public MemberResponse updateMember(String memberNumber, MemberEditRequest memberEditRequest) {
		Member member = memberRepository.findByMemberNumber(memberNumber)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.MEMBER_NOT_FOUND));

		member.setAddress1(memberEditRequest.getAddress1());
		member.setAddress2(memberEditRequest.getAddress2());
		member.setCity(memberEditRequest.getCity());
		member.setPhone1(memberEditRequest.getPhone1());
		member.setPhone2(memberEditRequest.getPhone2());

		Member updatedMember = memberRepository.save(member);
		return memberMapper.toResponse(updatedMember);
	}

	@Override
	public MemberEligibilityResponse searchMemberEligibility(String memberNumber) {
		Member member = memberRepository.findByMemberNumber(memberNumber)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.MEMBER_NOT_FOUND));

		// Récupérer l'assemblée en cours (celle qui n'est pas fermée)
		Assembly currentAssembly = assemblyRepository.findByClosed(false)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.CURRENT_ASSEMBLY_NOT_FOUND));

		LocalDate agDate = currentAssembly.getDay();
		LocalDate firstDayOfConvocationMonth = agDate.withDayOfMonth(1).minusMonths(1);
		LocalDate contributionDeadline = firstDayOfConvocationMonth.minusMonths(6);

		String eligibilityStatus;
		if (isEligible(member, contributionDeadline)) {
			eligibilityStatus = "éligible";
		} else if (isEligibleByGrouping(member, contributionDeadline)) {
			eligibilityStatus = "éligible par regroupement";
		} else {
			eligibilityStatus = "non éligible";
		}

		MemberResponse memberResponse = memberMapper.toResponse(member);

		return new MemberEligibilityResponse(member.getMemberNumber(), member.getCompanyName(), eligibilityStatus,
				memberResponse);
	}

	private boolean isEligibleByGrouping(Member member, LocalDate contributionDeadline) {
		// Vérifier si le nombre d'affiliés est inférieur à 50
		if (member.getWorkforce() < 50) {
			// Vérifier si les contributions sont à jour
			boolean contributionCheck = member.getDtrYear() >= contributionDeadline.getYear();

			return contributionCheck;
		}

		// Si le membre a un effectif de 50 ou plus, il n'est pas éligible par
		// regroupement
		return false;
	}

}