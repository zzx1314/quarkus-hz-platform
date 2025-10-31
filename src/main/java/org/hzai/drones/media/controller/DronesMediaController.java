package org.hzai.drones.media.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.hzai.config.FileConfig;
import org.hzai.drones.media.entity.DronesMedia;
import org.hzai.drones.media.entity.dto.DronesMediaDto;
import org.hzai.drones.media.entity.dto.DronesMediaQueryDto;
import org.hzai.drones.media.service.DronesMediaService;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.R;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/dronesMedia")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DronesMediaController {
    @Inject
    DronesMediaService dronesMediaService;

    @Inject
    FileConfig fileConfig;

    @GET
    @Path("/getPage")
    public R<PageResult<DronesMedia>> getPage(@BeanParam DronesMediaQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(dronesMediaService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<DronesMedia>> getByDto(@BeanParam DronesMediaQueryDto dto) {
        return R.ok(dronesMediaService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<DronesMedia>> getAll() {
        return R.ok(dronesMediaService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(DronesMedia entity) {
        return R.ok(dronesMediaService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<DronesMedia> update(DronesMediaDto dto) {
        dronesMediaService.replaceByDto(dto);
        return R.ok();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        DronesMedia entity = DronesMedia.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

    @GET
    @Path("/stream/{filename}")
    public Response streamVideo(@PathParam("filename") String filename, @HeaderParam("Range") String range)
            throws IOException {
        File video = new File(fileConfig.basePath() + "/videos", filename);
        if (!video.exists()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        String mimeType = Files.probeContentType(video.toPath());
        if (mimeType == null) {
            // 手动指定 mov 的类型
            if (filename.endsWith(".mov")) {
                mimeType = "video/quicktime";
            } else {
                mimeType = "application/octet-stream";
            }
        }

        // 处理分片请求（支持拖动进度条）
        long fileLength = video.length();
        long start = 0;
        long end = fileLength - 1;

        if (range != null) {
            String[] parts = range.replace("bytes=", "").split("-");
            start = Long.parseLong(parts[0]);
            if (parts.length > 1 && !parts[1].isEmpty()) {
                end = Long.parseLong(parts[1]);
            }
        }

        long contentLength = end - start + 1;
        FileInputStream fis = new FileInputStream(video);
        fis.skip(start);
        Response.ResponseBuilder response = Response.status(range != null ? 206 : 200)
                .entity(new BufferedInputStream(fis))
                .header("Accept-Ranges", "bytes")
                .header("Content-Range", "bytes " + start + "-" + end + "/" + fileLength)
                .header("Content-Length", contentLength)
                .header("Content-Type", mimeType);

        return response.build();
    }

}