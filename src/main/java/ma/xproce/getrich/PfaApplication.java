package ma.xproce.getrich;

import ma.xproce.getrich.dao.entities.Enterprise;
import ma.xproce.getrich.dao.entities.Stock;
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


	Member member = new Member((long)1,"admin", "admin", "lord", "me.jpg", 1, null);
	Enterprise enterprise = new Enterprise((long) 1,"tesla", (Stock)null, null);
	enterprise.addUser(member);
	member.addEnterprise(enterprise);
	enterpriseManager.addEnterprise(enterprise);
	memberManager.addMember(member);
	}



	}
