package org.huazhi.system.sysorg.repository;

import java.util.List;

import org.huazhi.system.sysorg.entity.SysOrg;
import org.huazhi.system.sysorg.entity.dto.SysOrgQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.QueryBuilder;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SysOrgRepository implements PanacheRepositoryBase<SysOrg, Integer> {

     public List<SysOrg> selectOrgList(SysOrgQueryDto sysOrgDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .notEqual("id",sysOrgDto.getNotId())
                .equal("type", sysOrgDto.getType())
                .like("name", sysOrgDto.getName())
                .orderBy("sort asc");
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public PageResult<SysOrg> selectOrgPage(SysOrgQueryDto dto, PageRequest pageRequest) {
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
