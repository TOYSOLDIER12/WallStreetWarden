package ma.xproce.getrich.service.mappers;

import ma.xproce.getrich.dao.entities.Member;
import ma.xproce.getrich.service.dto.MemberDto;
import ma.xproce.getrich.service.dto.MemberDtoADD;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    ModelMapper memberMapper = new ModelMapper();
    public MemberDto MemberToMemberDTO(Member creator){
        return memberMapper.map(creator, MemberDto.class);
    }

    public Member MemberDTOToMember(MemberDto creatorDTO){
        return memberMapper.map(creatorDTO, Member.class);
    }

    public Member MemberDTOADDToMember(MemberDtoADD creatorDTOADD){
        return memberMapper.map(creatorDTOADD, Member.class);
    }
    public MemberDtoADD MemberToMemberDTOADD(Member member) {
        return memberMapper.map(member, MemberDtoADD.class);
    }
}
