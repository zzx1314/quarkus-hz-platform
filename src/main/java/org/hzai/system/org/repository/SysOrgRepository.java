package org.hzai.system.org.repository;

import java.util.List;

import org.hzai.system.org.entity.SysOrg;
import org.hzai.system.org.entity.dto.SysOrgQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.QueryBuilder;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SysOrgRepository implements PanacheRepository<SysOrg> {

     public List<SysOrg> selectOrgList(SysOrgQueryDto sysOrgDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .like("name", sysOrgDto.getName());
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public PageResult<SysOrg> selectUserPage(SysOrgQueryDto dto, PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .like("name", dto.getName())
                .between("createTime", dto.getBeginTime(), dto.getEndTime())
                .orderBy("createTime desc");

        PanacheQuery<SysOrg> query = find(qb.getQuery(), qb.getParams())
                .page(Page.of(pageRequest.getPageIndex(), pageRequest.getSize()));

        return new PageResult<>(
                query.list(),
                query.count(),
                pageRequest.getPage(),
                pageRequest.getSize());
    }

}
