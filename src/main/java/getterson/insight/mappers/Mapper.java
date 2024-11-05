package getterson.insight.mappers;

import java.util.ArrayList;
import java.util.List;

public interface Mapper <E, D>{
    E toEntity(D dto) throws Exception;

    D toDTO(E entity);

    default List<D> toDTO(List<E> entityList){
        List<D> DTOList = new ArrayList<>();
        for(E entity : entityList){
            DTOList.add(toDTO(entity));
        }
        return DTOList;
    }
}

