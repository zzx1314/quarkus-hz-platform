package org.hzai.system.sysuser.service;

import java.util.List;

import org.hzai.system.sysuser.entity.SysUser;
import org.hzai.system.sysuser.entity.dto.SysUserQueryDto;
import org.hzai.system.sysuser.repository.SysUserRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SysUserServiceImp implements SysUserService {
    @Inject
    SysUserRepository sysUserRepository;
    @Override
    public List<SysUser> listUsers() {
        return sysUserRepository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }
    @Override
    public boolean authenticate(String username, String password) {
        return "admin".equals(username) && "123456".equals(password);
    }
    @Override
    public List<SysUser> listUsersByDto(SysUserQueryDto sysUserDto) {
        return sysUserRepository.selectUserList(sysUserDto);
    }
    @Override
    public PageResult<SysUser> listUserPage(SysUserQueryDto dto, PageRequest pageRequest) {
        return sysUserRepository.selectUserPage(dto, pageRequest);
    }
    @Override
    public Boolean registerUser(SysUser sysUser) {
         sysUserRepository.persist(sysUser);
         return true;
    }
}
