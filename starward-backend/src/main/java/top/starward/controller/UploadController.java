package top.starward.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.starward.common.Result;
import top.starward.exception.BusinessException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 🌌 静态资源与封面图上传控制器
 */
@Slf4j
@Tag(name = "上传模块", description = "文章封面及静态文件上传接口")
@RestController
@RequestMapping("/v1/admin/upload")
public class UploadController {

    @Value("${starward.upload.dir:${user.dir}/uploads}")
    private String uploadDir;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            ".jpg", ".jpeg", ".png", ".webp", ".gif", ".avif", ".svg"
    );

    @Operation(summary = "上传图片文件", description = "上传本地图片文件（支持 webp、png、jpg、gif 等），需 X-Admin-Token 鉴权")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }

        // 校验文件大小 (限制 15MB)
        if (file.getSize() > 15 * 1024 * 1024) {
            throw new BusinessException("上传图片不能超过 15MB");
        }

        String originalFilename = file.getOriginalFilename();
        String ext = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            // 默认后缀为 .webp
            ext = ".webp";
        }

        try {
            Path targetDir = Paths.get(uploadDir).toAbsolutePath().normalize();
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            // 生成唯一文件名：cover_时间戳_随机串.ext
            String uniqueName = "cover_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + ext;
            Path targetPath = targetDir.resolve(uniqueName);

            file.transferTo(targetPath.toFile());

            String fileUrl = "/api/uploads/" + uniqueName;
            log.info("🖼️ 封面图片上传成功: {} -> {}", originalFilename, fileUrl);

            return Result.success("图片上传成功 🎨", Map.of(
                    "url", fileUrl,
                    "filename", uniqueName
            ));
        } catch (IOException e) {
            log.error("❌ 上传图片保存失败", e);
            throw new BusinessException("文件保存失败: " + e.getMessage());
        }
    }
}
