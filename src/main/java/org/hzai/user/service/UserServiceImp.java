package org.hzai.user.service;

import java.util.List;

import org.hzai.user.entity.SysUser;
import org.hzai.user.entity.dto.SysUserDto;
import org.hzai.user.repository.SysUserRository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserServiceImp implements UserService {
    @Inject
    SysUserRository sysUserRepository;
    @Override
    public List<SysUser> listUsers() {
        return sysUserRepository.listAll(Sort.by("createTime"));
    }
    @Override
    public boolean authenticate(String username, String password) {
        return "admin".equals(username) && "123456".equals(password);
    }
    @Override
    public List<SysUser> listUsersByDto(SysUserDto sysUserDto) {
        return sysUserRepository.selectUserList(sysUserDto);
    }
    @Override
    public PageResult<SysUser> listUserPage(SysUserDto dto, PageRequest pageRequest) {
        return sysUserRepository.selectUserPage(dto, pageRequest);
    }

}
