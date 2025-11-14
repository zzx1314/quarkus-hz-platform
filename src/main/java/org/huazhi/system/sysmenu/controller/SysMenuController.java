package org.huazhi.system.sysmenu.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.huazhi.system.sysmenu.entity.SysMenu;
import org.huazhi.system.sysmenu.entity.dto.SysMenuDto;
import org.huazhi.system.sysmenu.entity.dto.SysMenuQueryDto;
import org.huazhi.system.sysmenu.entity.vo.MenuVo;
import org.huazhi.system.sysmenu.service.SysMenuService;
import org.huazhi.util.CommonConstants;
import org.huazhi.util.MenuMeta;
import org.huazhi.util.MenuTree;
import org.huazhi.util.R;
import org.huazhi.util.SecurityUtil;
import org.huazhi.util.TreeUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/sysMenu")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SysMenuController {
    @Inject
    SysMenuService sysMenuService;

    @Inject
    SecurityUtil securityUtil;

    private static final ObjectMapper mapper = new ObjectMapper();


    @GET
    public R<Object> getUserMenu() {
        // 获取符合条件的菜单
        List<Long> roleIds  = securityUtil.getRole();
        List<MenuVo> listMenuByRoleId = sysMenuService.listMenuByRoleId(roleIds.get(0));
        if (listMenuByRoleId != null && !listMenuByRoleId.isEmpty()) {
            List<MenuTree> menuTreeList = listMenuByRoleId.stream().map(one ->{
                MenuMeta menuMeta = new MenuMeta();
				menuMeta.setIcon(one.getIcon());
				menuMeta.setRank(one.getSort());
				menuMeta.setShowParent(one.getLeaf());
				menuMeta.setTitle(one.getName());
				menuMeta.setAuths(one.getAuths());
				one.setMeta(menuMeta);
				return one;
            })
            .filter(menuVo -> !CommonConstants.ACTION_BUTTON.equals(menuVo.getType()))
				.map(MenuTree::new)
				.sorted(Comparator.comparingInt(one -> one.getMeta().getRank()))
            .collect(Collectors.toList());
            ArrayNode arrayNode = mapper.convertValue(menuTreeList, ArrayNode.class);
            return R.ok(TreeUtil.listToTree(arrayNode));
        } else{
            return R.failed();
        }
    }

    @GET
    @Path("/getByDto")
    public R<List<SysMenu>> getByDto(@BeanParam SysMenuQueryDto dto) {
        return R.ok(sysMenuService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<SysMenu>> getAll() {
        return R.ok(sysMenuService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(SysMenu entity) {
        return R.ok(sysMenuService.register(entity));
    }

    @PUT
    @Path("/update")
    @Transactional
    public R<SysMenu> update(SysMenuDto dto) {
        return R.ok(sysMenuService.updateMenu(dto));
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        SysMenu entity = SysMenu.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}