package org.hzai.system.sysmenu.repository;

import java.util.List;

import org.hzai.system.sysmenu.entity.SysMenu;
import org.hzai.system.sysmenu.entity.dto.SysMenuQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.QueryBuilder;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SysMenuRepository implements PanacheRepository<SysMenu> {

     public List<SysMenu> selectList(SysMenuQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
        .in("id", queryDto.getIds())
        .equal("type", queryDto.getType())
        .equal("roleCode", queryDto.getRoleCode())
        .equal("parentId", queryDto.getParentId())
        .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public PageResult<SysMenu> selectPage(SysMenuQueryDto dto, PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .between("createTime", dto.getBeginTime(), dto.getEndTime())
                .orderBy("createTime desc");

        PanacheQuery<SysMenu> query = find(qb.getQuery(), qb.getParams())
                .page(Page.of(pageRequest.getPageIndex(), pageRequest.getSize()));

        return new PageResult<>(
                query.list(),
                query.count(),
                pageRequest.getPage(),
                pageRequest.getSize());
    }

}