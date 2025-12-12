package org.huazhi.system.sysrole.service;

import java.time.LocalDateTime;
import java.util.List;

import org.huazhi.system.sysrole.entity.SysRole;
import org.huazhi.system.sysrole.entity.dto.SysRoleQueryDto;
import org.huazhi.system.sysrole.repository.SysRoleRepository;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SysRoleServiceImp implements SysRoleService {
    @Inject
    SysRoleRepository sysRoleRepository;

    @Override
    public List<SysRole> listRoles() {
        return sysRoleRepository.list("isDeleted = ?1", Sort.by("createTime"), 0);
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
    public Long registerRole(SysRole sysRole) {
        sysRole.setCreateTime(LocalDateTime.now());
        sysRoleRepository.persist(sysRole);
        return sysRole.getId();
    }

    @Override
    public SysRole listRoleByDto(SysRoleQueryDto sysRoleQueryDto) {
        return sysRoleRepository.selectOne(sysRoleQueryDto);
    }

}
