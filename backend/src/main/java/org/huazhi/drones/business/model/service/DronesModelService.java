package org.huazhi.drones.business.model.service;

import java.util.List;

import org.huazhi.drones.business.model.entity.DronesModel;
import org.huazhi.drones.business.model.entity.dto.DronesModelDto;
import org.huazhi.drones.business.model.entity.dto.DronesModelQueryDto;
import org.huazhi.drones.common.SelectOption;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.R;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public interface DronesModelService {
   List<DronesModel> listEntitys();

   List<DronesModel> listEntitysByDto(DronesModelQueryDto sysOrgDto);

   DronesModel listOne(DronesModelQueryDto dto);

   PageResult<DronesModel> listPage(DronesModelQueryDto dto, PageRequest pageRequest);

   Long register(DronesModel entity);

   void replaceById(DronesModel entity);

   void replaceByDto(DronesModelDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);

   R<Object> uploadFile(FileUpload file, DronesModelDto modelDto);

   List<SelectOption> getSelectOption();

   List<Long> getModelCount();

   List<Long> getModelCountBefore();

   long count();
}