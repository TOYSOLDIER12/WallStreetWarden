package ma.xproce.getrich.service;

import ma.xproce.getrich.dao.entities.Enterprise;

import java.util.List;
import java.util.Optional;

public interface EnterpriseManager {
    public Enterprise addEnterprise(Enterprise enterprise);
    public Enterprise updateEnterprise(Enterprise enterprise);
    public boolean deleteEnterprise(Enterprise enterprise);
    public List<Enterprise> getAllEnterprises();
    public Optional<Enterprise> getEnterpriseById(long id);

}
