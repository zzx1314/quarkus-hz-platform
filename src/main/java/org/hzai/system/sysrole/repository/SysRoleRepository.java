package org.hzai.system.sysrole.repository;

import java.util.List;

import org.hzai.system.sysrole.entity.SysRole;
import org.hzai.system.sysrole.entity.dto.SysRoleQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.QueryBuilder;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SysRoleRepository implements PanacheRepository<SysRole>{

    public List<SysRole> selectRoleList(SysRoleQueryDto sysRoleQueryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .alias("r")
                .equal("id", sysRoleQueryDto.getId())
                .equal("isDeleted", 0)
                .like("name", sysRoleQueryDto.getName());

        String baseQuery = "FROM SysRole r LEFT JOIN FETCH r.menus WHERE " + qb.getQuery();
        return find(baseQuery, qb.getParams()).list();
    }

    public PageResult<SysRole> selectRolePage(SysRoleQueryDto dto, PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .like("name", dto.getName())
                .between("createTime", dto.getBeginTime(), dto.getEndTime())
                .orderBy("createTime desc");

        PanacheQuery<SysRole> query = find(qb.getQuery(), qb.getParams())
                .page(Page.of(pageRequest.getPageIndex(), pageRequest.getSize()));

        return new PageResult<>(
                query.list(),
                query.count(),
                pageRequest.getPage(),
                pageRequest.getSize());
    }

    public SysRole selectOne(SysRoleQueryDto sysRoleQueryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .alias("r")
                .equal("code", sysRoleQueryDto.getCode())
                .equal("id", sysRoleQueryDto.getId())
                .equal("isDeleted", 0)
                .like("name", sysRoleQueryDto.getName());

        String baseQuery = "FROM SysRole r LEFT JOIN FETCH r.menus WHERE " + qb.getQuery();
        return find(baseQuery, qb.getParams()).singleResult();
    }
}
