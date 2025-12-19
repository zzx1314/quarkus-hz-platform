package org.huazhi.drones.media.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.huazhi.config.FileConfig;
import org.huazhi.drones.media.entity.DronesMedia;
import org.huazhi.drones.media.entity.dto.DronesMediaDto;
import org.huazhi.drones.media.entity.dto.DronesMediaQueryDto;
import org.huazhi.drones.media.service.DronesMediaService;
import org.huazhi.oauth.annotation.TokenRequired;
import org.huazhi.util.FileUtil;
import org.huazhi.util.JsonUtil;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.R;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/dronesMedia")
@TokenRequired
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
    public R<Long> create(DronesMedia entity) {
        return R.ok(dronesMediaService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<Void> update(DronesMediaDto dto) {
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

    @POST
    @Path(value = "/uploadFile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public R<Long> uploadFile(@RestForm("file") FileUpload file, @RestForm String mediaInfo) throws Exception {
        DronesMediaDto mediaDto = JsonUtil.fromJson(mediaInfo, DronesMediaDto.class);
        return dronesMediaService.uploadFile(file, mediaDto);
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

    @GET
    @Path("/downFile")
    public Response downloadByPath(@QueryParam("filePath") String filePath) {
        return FileUtil.downloadFile(filePath);
    }

}