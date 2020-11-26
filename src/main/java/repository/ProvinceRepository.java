package repository;

import model.Province;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProvinceRepository extends PagingAndSortingRepository<Province,Long> {
}
