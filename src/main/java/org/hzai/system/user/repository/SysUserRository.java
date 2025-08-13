package org.hzai.system.user.repository;

import java.util.List;

import org.hzai.system.user.entity.SysUser;
import org.hzai.system.user.entity.dto.SysUserQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.QueryBuilder;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SysUserRository implements PanacheRepository<SysUser> {
    public List<SysUser> selectUserList(SysUserQueryDto sysUserDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .like("username", sysUserDto.getUsername())
                .like("phone", sysUserDto.getPhone());
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public PageResult<SysUser> selectUserPage(SysUserQueryDto dto, PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .like("username", dto.getUsername())
                .like("phone", dto.getPhone())
                .between("createTime", dto.getBeginTime(), dto.getEndTime())
                .orderBy("createTime desc");

        PanacheQuery<SysUser> query = find(qb.getQuery(), qb.getParams())
                .page(Page.of(pageRequest.getPageIndex(), pageRequest.getSize()));

        return new PageResult<>(
                query.list(),
                query.count(),
                pageRequest.getPage(),
                pageRequest.getSize());
    }

}
