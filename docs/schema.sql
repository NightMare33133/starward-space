-- ==============================================================================
-- 🌌 Starward Space - 核心数据库结构定义 (MySQL 8.4 LTS)
-- 字符集：utf8mb4 (支持完整的 Emoji 和特殊符号存储)
-- 引擎：InnoDB (支持事务与行级锁)
-- ==============================================================================

CREATE DATABASE IF NOT EXISTS `starward_space` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `starward_space`;

-- ------------------------------------------------------------------------------
-- 1. 标签表 (tags)
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS `post_tags`;
DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '标签主键ID',
    `name` VARCHAR(50) NOT NULL COMMENT '标签名称 (如: Java, AI Agent, 摄影)',
    `slug` VARCHAR(50) NOT NULL COMMENT 'URL别名/标识',
    `color` VARCHAR(20) DEFAULT '#6366f1' COMMENT '标签主题色 (十六进制颜色值)',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_slug` (`slug`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章标签表';

-- ------------------------------------------------------------------------------
-- 2. 文章表 (posts)
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS `posts`;
CREATE TABLE `posts` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文章主键ID',
    `title` VARCHAR(150) NOT NULL COMMENT '文章标题',
    `slug` VARCHAR(150) NOT NULL COMMENT 'URL友好标识符 (例如: hello-starward-space)',
    `summary` VARCHAR(300) DEFAULT NULL COMMENT '文章摘要 (纯文本简介)',
    `content_md` LONGTEXT NOT NULL COMMENT 'Markdown 源码内容',
    `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '文章封面图外链URL',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PUBLISHED' COMMENT '状态: DRAFT(草稿) / PUBLISHED(已发布)',
    `is_pinned` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否置顶: 0-否, 1-是',
    `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览量/阅读次数',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_post_slug` (`slug`),
    KEY `idx_status_created` (`status`, `created_at` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章核心表';

-- ------------------------------------------------------------------------------
-- 3. 文章-标签多对多关联表 (post_tags)
-- ------------------------------------------------------------------------------
CREATE TABLE `post_tags` (
    `post_id` BIGINT NOT NULL COMMENT '文章ID',
    `tag_id` BIGINT NOT NULL COMMENT '标签ID',
    PRIMARY KEY (`post_id`, `tag_id`),
    KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章标签多对多关联表';

-- ------------------------------------------------------------------------------
-- 4. 星际碎语/说说表 (moments)
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS `moments`;
CREATE TABLE `moments` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '碎语主键ID',
    `content` TEXT NOT NULL COMMENT '随想内容 (支持换行与表情)',
    `mood` VARCHAR(30) DEFAULT '✨' COMMENT '此刻的心情Emoji或微状态',
    `images_json` TEXT DEFAULT NULL COMMENT '附带的图片URL列表 (JSON字符串)',
    `is_pinned` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否置顶',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_created_at` (`created_at` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='星际碎语说说表';

-- ==============================================================================
-- 🌟 初始创世种子数据 (Genesis Seed Data)
-- ==============================================================================
INSERT INTO `tags` (`id`, `name`, `slug`, `color`) VALUES
(1, '星向起航', 'starward', '#6366f1'),
(2, '全栈架构', 'fullstack', '#10b981'),
(3, '思考杂谈', 'thoughts', '#f59e0b');

INSERT INTO `posts` (`id`, `title`, `slug`, `summary`, `content_md`, `cover_image`, `status`, `is_pinned`, `view_count`) VALUES
(1, 
 '你好，星向空间 (Hello Starward Space)', 
 'hello-starward-space', 
 '并不是为了向世界证明什么，而是在嘈杂的信息洪流与内卷浪潮里，亲手为自己搭建一个有温度、有审美的赛博自留地。', 
 '## 🌌 星光初亮\n\n欢迎来到 **Starward Space（星向空间）**。\n\n这个数字空间诞生于 2026 年初秋。在经历了一次次的同辈比较、焦虑与反思后，我决定把所有的敬佩与向往，化作键盘上每一行真实的代码。\n\n### 🛠️ 我们的架构哲学\n- **坚实底盘**：Spring Boot 3 + Java 21 LTS + MySQL 8.4\n- **极客审美**：毛玻璃（Glassmorphism）与暗黑星河自适应\n- **智能分身**：基于 RAG / Agent 编排的专属数字分身\n\n```java\nSystem.out.println("Hello, Starward Space!");\n```\n\n愿你在这里也能找到属于自己的平静与星光。', 
 'https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?auto=format&fit=crop&w=1200&q=80',
 'PUBLISHED', 1, 42);

INSERT INTO `post_tags` (`post_id`, `tag_id`) VALUES (1, 1), (1, 2);

INSERT INTO `moments` (`id`, `content`, `mood`, `is_pinned`) VALUES
(1, '第一缕星光在本地闪耀！Spring Boot 3 接口启动耗时 0.69 秒，属于自己的赛博基地正在拔地而起 ✨', '🚀', 1),
(2, '不赶时间，接纳平凡，一步一个脚印敲出属于自己的宇宙。', '🌱', 0);
