package ma.xproce.getrich.service;

import ma.xproce.getrich.dao.entities.Member;
import ma.xproce.getrich.service.dto.MemberDto;

import java.util.Collection;

public interface MemberManager {
    public Member addMember(Member member);
    public boolean deleteMember(Member member);
    public Member updateMember(Member member);
    public Member getById(long id);
    public Collection<Member> getAllMembers();
    public boolean checkLogin(String username, String password);
    public Member findByUsername(String username);
    public String getProfilePictureUrl(MemberDto memberDto);

}
