package ma.xproce.getrich.service;

import ma.xproce.getrich.dao.entities.Member;

import java.util.Collection;
import java.util.Optional;

public interface MemberManager {
    public Member addMember(Member member);
    public boolean deleteMember(Member member);
    public Member updateMember(Member member);
    public Member getById(long id);
    public Collection<Member> getAllMembers();
    public boolean checkLogin(String username, String password);
    public Optional<Member> findByUsername(String username);

}
