package org.hzai.system.sysrole.service;

import java.util.List;

import org.hzai.system.sysrole.entity.SysRole;
import org.hzai.system.sysrole.entity.dto.SysRoleQueryDto;
import org.hzai.system.sysrole.repository.SysRoleRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SysRoleServiceImp implements SysRoleService {
    @Inject
    SysRoleRepository sysRoleRepository;

    @Override
    public List<SysRole> listRoles() {
        return sysRoleRepository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<SysRole> listRolesByDto(SysRoleQueryDto sysRoleDto) {
        return sysRoleRepository.selectRoleList(sysRoleDto);
    }

    @Override
    public PageResult<SysRole> listRolePage(SysRoleQueryDto dto, PageRequest pageRequest) {
        return sysRoleRepository.selectRolePage(dto, pageRequest);
    }

    @Override
    public Boolean registerRole(SysRole sysRole) {
         sysRoleRepository.persist(sysRole);
         return true;
    }

}
