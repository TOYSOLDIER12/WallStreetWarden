package ma.xproce.getrich;

import ma.xproce.getrich.dao.entities.Enterprise;
import ma.xproce.getrich.dao.entities.Member;
import ma.xproce.getrich.service.EnterpriseManager;
import ma.xproce.getrich.service.MemberManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PfaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PfaApplication.class, args);
	}
	@Autowired
	MemberManager memberManager;
	@Autowired
	EnterpriseManager enterpriseManager;
	public void run(String... args) throws Exception {

		Member member = new Member();
		Enterprise enterprise = new Enterprise();
		enterprise.setStock(null);
		enterprise.setName("tesla");
		enterprise.addUser(member);
		enterprise.addUser(member);
		member.setPassword("1234");
		member.setUsername("sidna");
		member.setProfile("test.jpg");
		member.setNumb_Of_Interactions(0);
		member.addEnterprise(enterprise);
		enterpriseManager.addEnterprise(enterprise);
		memberManager.addMember(member);

	}



	}
