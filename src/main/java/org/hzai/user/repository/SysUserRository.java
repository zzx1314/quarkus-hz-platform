package org.hzai.user.repository;

import org.hzai.user.entity.SysUser;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SysUserRository implements PanacheRepository<SysUser>{
}
