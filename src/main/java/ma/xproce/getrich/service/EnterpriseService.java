package ma.xproce.getrich.service;

import ma.xproce.getrich.dao.entities.Enterprise;
import ma.xproce.getrich.dao.entities.Stock;
import ma.xproce.getrich.dao.repositories.EnterpriseRepository;
import ma.xproce.getrich.dao.repositories.StockRepository;
import ma.xproce.getrich.service.FileReader.FileReaderInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnterpriseService implements EnterpriseManager {
    @Autowired
    EnterpriseRepository enterpriseRepository;
    @Autowired
    FileReaderInt fileReaderInt;
    @Autowired
    private StockRepository stockRepository;

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


    @Override
    public Enterprise getEnterpriseByName(String name) {return enterpriseRepository.findByName(name);}

    @Override
    public void populateEnterprise(){
        if (enterpriseRepository.findAll().isEmpty()){
           List<String> datas = fileReaderInt.readFile("python/stocks.txt");
           for (String data : datas){
               Enterprise enterprise = new Enterprise();
               Stock stock = new Stock();
               enterprise.setName(data.split(":")[0]);
               stock.setTickName(data.split(": ")[1]);
               enterpriseRepository.save(enterprise);
               stock.setEnterprise(enterprise);
               stockRepository.save(stock);
           }
        }
    }
}
