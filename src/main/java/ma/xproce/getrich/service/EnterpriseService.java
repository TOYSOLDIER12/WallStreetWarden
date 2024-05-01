package ma.xproce.getrich.service;

import ma.xproce.getrich.dao.entities.Enterprise;
import ma.xproce.getrich.dao.repositories.EnterpriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnterpriseService implements EnterpriseManager {
    @Autowired
    EnterpriseRepository enterpriseRepository;
    @Override
    public Enterprise addEnterprise(Enterprise enterprise) {
        return enterpriseRepository.save(enterprise);
    }

    @Override
    public Enterprise updateEnterprise(Enterprise enterprise) {
        Optional<Enterprise> expectedEntreprise = enterpriseRepository.findById(enterprise.getId());
        if (expectedEntreprise.isEmpty()){
            System.out.println("no enterprise with that id mate " + enterprise.getId());
            return null;
        }
        Enterprise UpdatedEnterprise = expectedEntreprise.get();
        UpdatedEnterprise.setName(enterprise.getName());
        UpdatedEnterprise.setStock(enterprise.getStock());
        UpdatedEnterprise.setMembers(enterprise.getMembers());
        return enterpriseRepository.save(UpdatedEnterprise);
    }

    @Override
    public boolean deleteEnterprise(Enterprise enterprise) {
        enterpriseRepository.delete(enterprise);
        return !enterpriseRepository.existsById(enterprise.getId());
    }

    @Override
    public List<Enterprise> getAllEnterprises() {
        return enterpriseRepository.findAll();
    }

    @Override
    public Optional<Enterprise> getEnterpriseById(long id) {
        return enterpriseRepository.findById(id);
    }
}
