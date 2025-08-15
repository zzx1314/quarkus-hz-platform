package org.hzai.system.menu.repository;

import java.util.List;

import org.hzai.system.menu.entity.SysMenu;
import org.hzai.system.menu.entity.dto.SysMenuQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.QueryBuilder;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SysMenuRepository implements PanacheRepository<SysMenu>{
    public List<SysMenu> selectOrgList(SysMenuQueryDto sysMenuQueryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .like("name", sysMenuQueryDto.getName());
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public PageResult<SysMenu> selectUserPage(SysMenuQueryDto dto, PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .like("name", dto.getName())
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
