package org.huazhi.drones.media.service;

import java.util.List;

import org.huazhi.drones.media.entity.DronesMedia;
import org.huazhi.drones.media.entity.dto.DronesMediaDto;
import org.huazhi.drones.media.entity.dto.DronesMediaQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.R;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public interface DronesMediaService {
   List<DronesMedia> listEntitys();

   List<DronesMedia> listEntitysByDto(DronesMediaQueryDto sysOrgDto);

   DronesMedia listOne(DronesMediaQueryDto dto);

   PageResult<DronesMedia> listPage(DronesMediaQueryDto dto, PageRequest pageRequest);

   Boolean register(DronesMedia entity);

   void replaceById(DronesMedia entity);

   void replaceByDto(DronesMediaDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);

   R<Object> uploadFile(FileUpload file, DronesMediaDto modelDto);
}