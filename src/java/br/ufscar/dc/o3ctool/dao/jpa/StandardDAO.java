package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.Standard;
import java.util.List;

/**
 *
 * @author Michael
 */

public interface StandardDAO {
//    public void save(Standard sta);
//    public boolean alreadyExistingStandard(Long projid, Standard sta);
//    public List<Standard> getAllStandards(Long projid);
//    public Standard getByName(Long projid, String name);
//    public void remove(Standard sta);
//    public void removeAll(Long projid);
    public void save(Standard sta);
    public boolean alreadyExistingStandard(Standard sta);
    public List<Standard> getAllStandards();
    public Standard getByName(String name);
    public String getByIdStandard(Long idStandard);
    public void remove(Standard sta);
    public void removeAll();
}
