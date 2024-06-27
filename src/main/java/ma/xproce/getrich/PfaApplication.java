package ma.xproce.getrich;

import ma.xproce.getrich.service.EnterpriseManager;
import ma.xproce.getrich.service.MemberManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PfaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PfaApplication.class, args);
	}
	@Autowired
	MemberManager memberManager;
	@Autowired
	EnterpriseManager enterpriseManager;

	public void run(String... args) throws Exception {

		//enterpriseManager.populateEnterprise();

	}



	}
