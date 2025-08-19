package org.hzai.system.sysuser.repository;

import java.util.List;

import org.hzai.system.sysuser.entity.SysUser;
import org.hzai.system.sysuser.entity.dto.SysUserQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.QueryBuilder;
import org.jboss.logging.Logger;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SysUserRepository implements PanacheRepository<SysUser> {
    private static final Logger logger = Logger.getLogger(SysUserRepository.class);
    public List<SysUser> selectUserList(SysUserQueryDto sysUserDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .equal("orgId", sysUserDto.getOrgId())
                .greaterThan("createTime", sysUserDto.getBeginTime())
                .lessThan("createTime", sysUserDto.getEndTime())
                .like("username", sysUserDto.getUsername())
                .like("phone", sysUserDto.getPhone());
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public PageResult<SysUser> selectUserPage(SysUserQueryDto dto, PageRequest pageRequest) {
        logger.info(dto.toString());
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .equal("orgId", dto.getOrgId())
                .in("orgId", dto.getOrgIds())
                .greaterThan("createTime", dto.getBeginTime())
                .lessThan("createTime", dto.getEndTime())
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

    public SysUser selectOne(SysUserQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .like("username", queryDto.getUsername())
                .like("phone", queryDto.getPhone());
        return this.find(qb.getQuery(), qb.getParams()).firstResult();
    }

}
