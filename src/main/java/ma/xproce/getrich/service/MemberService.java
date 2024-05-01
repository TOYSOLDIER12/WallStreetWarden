package ma.xproce.getrich.service;

import ma.xproce.getrich.dao.entities.Member;
import ma.xproce.getrich.dao.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
@Service
public class MemberService implements MemberManager {
    @Autowired
    MemberRepository memberRepository;
    @Override
    public Member addMember(Member member) {
        if(member.getUsername().isEmpty() || member.getPassword().isEmpty()) {
            System.out.println("no username no pass? thou shall not pass");
            return null;
        }
        return  memberRepository.save(member);
    }

    @Override
    public boolean deleteMember(Member member) {
        Optional<Member> expectedUser = memberRepository.findById(member.getId());
        if (expectedUser.isEmpty()){
            System.out.println("no user with that id mate " + member.getId());
        return false;
            }
        memberRepository.delete(expectedUser.get());
        return !memberRepository.existsById(member.getId());
    }

    @Override
    public Member updateMember(Member member) {
        Optional<Member> expectedUser = memberRepository.findById(member.getId());
        if (expectedUser.isEmpty()){
            System.out.println("no user with that id mate " + member.getId());
            return null;
        }
        Member updatedMember = expectedUser.get();
        updatedMember.setName(member.getName());
        updatedMember.setUsername(member.getUsername());
        updatedMember.setPassword(member.getPassword());
        updatedMember.setProfile(member.getProfile());
        updatedMember.setNumb_Of_Interactions(member.getNumb_Of_Interactions());
        return memberRepository.save(updatedMember);
    }

    @Override
    public Member getById(long id) {
        if(memberRepository.getById(id) == null) {
            System.out.println("no user with that id "+id);
            return null;
        }
        return memberRepository.getById(id);
    }

    @Override
public Collection<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public boolean checkLogin(String username, String password) {
        Optional<Member> existingUser = memberRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            Member member = existingUser.get();
            if (member.getPassword().equals(password)) {
                // Password matches, login successful
                return true;
            } else {
                System.out.println("Password not matchy matchy");
                return false;
            }
        } else {
            System.out.println("who are you mr " +username+" ?");
            return false;
        }
    }


    @Override
    public Optional<Member> findByUsername(String username) {
        Optional<Member> existingUser = memberRepository.findByUsername(username);
        if(existingUser.isPresent()){
            return existingUser;
        }
        return null;
    }

}
