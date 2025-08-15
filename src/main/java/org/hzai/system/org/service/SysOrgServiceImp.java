package org.hzai.system.org.service;

import java.util.List;

import org.hzai.system.org.entity.SysOrg;
import org.hzai.system.org.entity.dto.SysOrgQueryDto;
import org.hzai.system.org.repository.SysOrgRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SysOrgServiceImp implements SysOrgService {
    @Inject
    SysOrgRepository sysOrgRepository;
    @Override
    public List<SysOrg> listOrgs() {
        return sysOrgRepository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<SysOrg> listOrgsByDto(SysOrgQueryDto sysOrgDto) {
        return sysOrgRepository.selectOrgList(sysOrgDto);
    }

    @Override
    public PageResult<SysOrg> listOrgsPage(SysOrgQueryDto dto, PageRequest pageRequest) {
        return sysOrgRepository.selectOrgPage(dto, pageRequest);
    }

    @Override
    public Boolean registerOrg(SysOrg sysOrg) {
        sysOrgRepository.persist(sysOrg);
        return true;
    }

}
