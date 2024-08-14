package ma.cimr.agmbackend.member;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.assembly.Assembly;
import ma.cimr.agmbackend.assembly.AssemblyRepository;
import ma.cimr.agmbackend.assembly.AssemblyService;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

	private final MemberRepository memberRepository;

	private final AssemblyRepository assemblyRepository;

	public List<Member> extractEligibleMembers() {
		Assembly currentAssembly = assemblyRepository.findByClosed(false)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.ASSEMBLY_NOT_FOUND));

		LocalDate assemblyDate = currentAssembly.getDay();
		LocalDate firstDayOfConvocationMonth = assemblyDate.withDayOfMonth(1).minusMonths(1);
		LocalDate contributionDueDate = firstDayOfConvocationMonth.minusMonths(6);

		int trimester = (contributionDueDate.getMonthValue() - 1) / 3 + 1;
		int year = contributionDueDate.getYear();

		List<Member> eligibleType1Members = memberRepository.findEligibleType1Members(
				trimester,
				year,
				50);

		List<Member> eligibleType2to4Members = memberRepository.findEligibleType2to4Members(
				trimester,
				year,
				50);

		List<Member> eligibleMembers = new ArrayList<>();
		eligibleMembers.addAll(eligibleType1Members);
		eligibleMembers.addAll(eligibleType2to4Members);

		return eligibleMembers;
	}
}