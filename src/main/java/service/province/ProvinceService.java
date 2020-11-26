package service.province;

import model.Province;
import org.springframework.beans.factory.annotation.Autowired;
import repository.ProvinceRepository;

public class ProvinceService implements IProvinceService {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public Iterable<Province> selectAll() {
        return provinceRepository.findAll();
    }

    @Override
    public Province findById(Long id) {
        return provinceRepository.findOne(id);
    }

    @Override
    public void save(Province province) {
        provinceRepository.save(province);
    }

    @Override
    public void update(Province province) {
        provinceRepository.save(province);
    }

    @Override
    public void remote(Long id) {
        provinceRepository.delete(id);
    }
}
