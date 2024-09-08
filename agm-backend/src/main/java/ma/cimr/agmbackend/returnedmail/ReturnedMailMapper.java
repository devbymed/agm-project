package ma.cimr.agmbackend.returnedmail;

import static org.mapstruct.ReportingPolicy.IGNORE;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ReturnedMailMapper {
    @Mapping(source = "member.memberNumber", target = "memberNumber")
    @Mapping(source = "returnDate", target = "returnDate")
    @Mapping(source = "reason.description", target = "reason")
    ReturnedMailResponse toResponse(ReturnedMail returnedMail);
}
