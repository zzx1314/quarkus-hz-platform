package org.hzai.drones.media.service;

import java.util.List;

import org.hzai.drones.media.entity.DronesMedia;
import org.hzai.drones.media.entity.dto.DronesMediaDto;
import org.hzai.drones.media.entity.dto.DronesMediaQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.R;
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