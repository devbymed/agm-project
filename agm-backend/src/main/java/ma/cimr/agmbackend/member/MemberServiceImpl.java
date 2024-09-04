package ma.cimr.agmbackend.member;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTxbxContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.assembly.Assembly;
import ma.cimr.agmbackend.assembly.AssemblyRepository;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;
import ma.cimr.agmbackend.user.User;
import ma.cimr.agmbackend.user.UserRepository;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

	@Value("${file.uploads.output-path}")
	private String baseUploadPath;

	private final MemberRepository memberRepository;
	private final UserRepository userRepository;
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

	private boolean isEligible(Member member) {
		LocalDate agDate = LocalDate.now(); // ou récupérer la date de l'assemblée courante
		LocalDate firstDayOfConvocationMonth = agDate.withDayOfMonth(1).minusMonths(1);
		LocalDate contributionDeadline = firstDayOfConvocationMonth.minusMonths(6);
		return isEligible(member, contributionDeadline);
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
	public MemberResponse getMemberById(Long memberId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.MEMBER_NOT_FOUND));
		return memberMapper.toResponse(member);
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

	@Override
	public void assignMembersToAgent(List<String> memberNumbers, Long agentId) {
		User agent = userRepository.findById(agentId)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.USER_NOT_FOUND));

		// Récupérer les membres éligibles parmi les membres sélectionnés
		List<Member> eligibleMembers = memberRepository.findAllByMemberNumberIn(memberNumbers).stream()
				.filter(this::isEligible)
				.collect(Collectors.toList());

		if (eligibleMembers.isEmpty()) {
			throw new ApiException(ApiExceptionCodes.MEMBERS_NOT_ELIGIBLE);
		}

		eligibleMembers.forEach(member -> {
			if (member.getAgent() != null) {
				throw new ApiException(ApiExceptionCodes.MEMBER_ALREADY_ASSIGNED);
			}
			member.setAgent(agent);
			member.setStatus(MemberInvitationStatus.Affectée);
			member.setAssignmentDate(LocalDate.now());
		});
		memberRepository.saveAll(eligibleMembers);
	}

	@Override
	public void autoAssignMembers() {
		List<Member> unassignedEligibleMembers = memberRepository.findAllByAgentIsNull().stream()
				.filter(this::isEligible)
				.collect(Collectors.toList());

		List<User> agents = userRepository.findAllByProfileName("Agent de relance");

		if (agents.isEmpty()) {
			throw new ApiException(ApiExceptionCodes.NO_AGENTS_AVAILABLE);
		}

		if (unassignedEligibleMembers.isEmpty()) {
			throw new ApiException(ApiExceptionCodes.NO_ELIGIBLE_MEMBERS_TO_ASSIGN);
		}

		int agentIndex = 0;
		for (Member member : unassignedEligibleMembers) {
			User agent = agents.get(agentIndex);
			member.setAgent(agent);
			member.setStatus(MemberInvitationStatus.Affectée);
			member.setAssignmentDate(LocalDate.now());
			memberRepository.save(member);
			agentIndex = (agentIndex + 1) % agents.size();
		}
	}

	@Override
	public void changeAgentForMember(String memberNumber, Long newAgentId) {
		// Retrieve the member by memberNumber
		Member member = memberRepository.findByMemberNumber(memberNumber)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.MEMBER_NOT_FOUND));

		// Ensure the member has the status "Affectée"
		if (!MemberInvitationStatus.Affectée.equals(member.getStatus())) {
			throw new ApiException(ApiExceptionCodes.INVALID_MEMBER_STATUS); // Custom exception message
		}

		// Retrieve the new agent by their ID
		User newAgent = userRepository.findById(newAgentId)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.USER_NOT_FOUND));

		// Update the agent and save the changes
		member.setAgent(newAgent);
		member.setAssignmentDate(LocalDate.now()); // Update the assignment date
		memberRepository.save(member);
	}

	@Override
	public Map<String, String> generateDocumentsForMember(Long memberId) throws FileNotFoundException {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.MEMBER_NOT_FOUND));

		Assembly currentAssembly = assemblyRepository.findByClosed(false)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.CURRENT_ASSEMBLY_NOT_FOUND));

		String memberFolder = normalizePath(
				Paths.get(baseUploadPath, "assemblies", String.valueOf(currentAssembly.getId()),
						"members", member.getMemberNumber()).toString());

		new File(memberFolder).mkdirs();

		String invitationLetterPath = generateInvitationLetter(member, currentAssembly, memberFolder);
		String attendanceSheetPath = generateAttendanceSheet(member, currentAssembly, memberFolder);
		String proxyPath = generateProxy(member, currentAssembly, memberFolder);

		member.setStatus(MemberInvitationStatus.Editée);
		member.setInvitationLetterPath(invitationLetterPath);
		member.setAttendanceSheetPath(attendanceSheetPath);
		member.setProxyPath(proxyPath);
		member.setEditionDate(LocalDate.now());
		memberRepository.save(member);

		Map<String, String> generatedDocuments = new HashMap<>();
		generatedDocuments.put("invitationLetter", invitationLetterPath);
		generatedDocuments.put("attendanceSheet", attendanceSheetPath);
		generatedDocuments.put("proxy", proxyPath);

		return generatedDocuments;
	}

	private String normalizePath(String path) {
		return path.replace("\\", "/");
	}

	private String generateInvitationLetter(Member member, Assembly assembly, String memberFolder)
			throws FileNotFoundException {

		String templatePath = normalizePath(Paths.get(baseUploadPath, assembly.getInvitationLetter()).toString());

		String uniqueFilename = "invitation_letter_" + member.getMemberNumber() + "_" + UUID.randomUUID() + ".docx";
		String outputPath = normalizePath(Paths.get(memberFolder, uniqueFilename).toString());

		log.info("Chemin du modèle de convocation: " + templatePath);
		log.info("Chemin du document généré: " + outputPath);

		File templateFile = new File(templatePath);
		if (!templateFile.exists()) {
			log.error("Le fichier modèle n'existe pas : " + templateFile.getAbsolutePath());
			throw new FileNotFoundException("Le fichier modèle n'a pas été trouvé : " + templatePath);
		}

		try (FileInputStream fis = new FileInputStream(templateFile);
				XWPFDocument document = new XWPFDocument(fis)) {
			for (XWPFParagraph paragraph : document.getParagraphs()) {

				for (XWPFRun run : paragraph.getRuns()) {
					String text = run.getText(0);

					if (text != null) {
						text = text.replace("$Type", assembly.getType().name())
								.replace("$Jour",
										assembly.getDay().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
								.replace("$Heure", assembly.getTime().toString())
								.replace("$Adresse", assembly.getAddress())
								.replace("$N° Adhérent", member.getMemberNumber());
						run.setText(text, 0);
					}
				}
			}

			XmlCursor cursor = document.getDocument().newCursor();
			while (cursor.hasNextToken()) {
				cursor.toNextToken();
				XmlObject obj = cursor.getObject();
				if (obj instanceof CTTxbxContent) {
					CTTxbxContent ctTxbxContent = (CTTxbxContent) obj;
					for (CTP ctp : ctTxbxContent.getPList()) {
						for (CTText ctText : ctp.getRArray(0).getTArray()) {
							String text = ctText.getStringValue();
							if (text != null) {
								text = text.replace("$Raison sociale", member.getCompanyName())
										.replace("$Ville", member.getCity());
								ctText.setStringValue(text);
							}
						}
					}
				}
			}

			Files.createDirectories(Paths.get(outputPath).getParent()); // Ensure directories exist
			try (FileOutputStream fos = new FileOutputStream(outputPath)) {
				document.write(fos);
			}
		} catch (IOException e) {
			throw new RuntimeException("Error generating document", e);
		}

		return outputPath;
	}

	private String generateAttendanceSheet(Member member, Assembly assembly, String memberFolder)
			throws FileNotFoundException {
		String templatePath = normalizePath(Paths.get(baseUploadPath, assembly.getAttendanceSheet()).toString());

		String uniqueFilename = "attendance_sheet_" + member.getMemberNumber() + "_" + UUID.randomUUID() + ".docx";
		String outputPath = normalizePath(Paths.get(memberFolder, uniqueFilename).toString());

		log.info("Chemin du modèle de la feuille de présence: " + templatePath);
		log.info("Chemin du document généré: " + outputPath);

		File templateFile = new File(templatePath);
		if (!templateFile.exists()) {
			log.error("Le fichier modèle n'existe pas : " + templateFile.getAbsolutePath());
			throw new FileNotFoundException("Le fichier modèle n'a pas été trouvé : " + templatePath);
		}

		try (FileInputStream fis = new FileInputStream(templateFile);
				XWPFDocument document = new XWPFDocument(fis)) {

			for (XWPFParagraph paragraph : document.getParagraphs()) {
				for (XWPFRun run : paragraph.getRuns()) {
					String text = run.getText(0);
					if (text != null) {
						text = text.replace("$TYPE", assembly.getType().name())
								.replace("$JOUR", assembly.getDay().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
								.replace("$Raison sociale", member.getCompanyName())
								.replace("$Adresse1", member.getAddress1())
								.replace("$N°Adhérent", member.getMemberNumber())
								.replace("$Effectif", String.valueOf(member.getWorkforce()));
						run.setText(text, 0);
					}
				}
			}

			try (FileOutputStream fos = new FileOutputStream(outputPath)) {
				document.write(fos);
			}
		} catch (IOException e) {
			throw new RuntimeException("Error generating document", e);
		}

		return outputPath;
	}

	private String generateProxy(Member member, Assembly assembly, String memberFolder) throws FileNotFoundException {

		String templatePath = normalizePath(Paths.get(baseUploadPath, assembly.getProxy()).toString());

		String uniqueFilename = "proxy_" + member.getMemberNumber() + "_" + UUID.randomUUID() + ".docx";
		String outputPath = normalizePath(Paths.get(memberFolder, uniqueFilename).toString());

		log.info("Chemin du modèle de pouvoir: " + templatePath);
		log.info("Chemin du document généré: " + outputPath);

		File templateFile = new File(templatePath);
		if (!templateFile.exists()) {
			log.error("Le fichier modèle n'existe pas : " + templateFile.getAbsolutePath());
			throw new FileNotFoundException("Le fichier modèle n'a pas été trouvé : " + templatePath);
		}

		try (FileInputStream fis = new FileInputStream(templateFile);
				XWPFDocument document = new XWPFDocument(fis)) {

			for (XWPFParagraph paragraph : document.getParagraphs()) {
				for (XWPFRun run : paragraph.getRuns()) {
					String text = run.getText(0);
					if (text != null) {
						text = text.replace("$Type", assembly.getType().name())
								.replace("$Jour", assembly.getDay().format(DateTimeFormatter.ofPattern(
										"dd/MM/yyyy")))
								.replace("$Raison sociale", member.getCompanyName())
								.replace("$N°Adhérent", member.getMemberNumber())
								.replace("$Effectif", String.valueOf(member.getWorkforce()))
								.replace("$date", assembly.getDay().format(DateTimeFormatter.ofPattern(
										"dd/MM/yyyy")))
								.replace("$heure", assembly.getTime().toString())
								.replace("$Adresse", assembly.getAddress());
						run.setText(text, 0);
					}
				}
			}

			try (FileOutputStream fos = new FileOutputStream(outputPath)) {
				document.write(fos);
			}
		} catch (IOException e) {
			throw new RuntimeException("Error generating document", e);
		}

		return outputPath;
	}
}