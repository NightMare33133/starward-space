# 🌌 Starward Space (星向空间)

<p align="center">
  <img src="starward-frontend/public/images/pompom.png" width="96" height="96" alt="Pom-Pom" style="border-radius: 24px;" />
</p>

<p align="center">
  <strong>愿此行，终抵群星 ✦ 二次元深空极客全栈博客与个人数字领航空间</strong>
</p>

<p align="center">
  <a href="https://github.com/NightMare33133/starward-space/actions/workflows/ci.yml">
    <img src="https://img.shields.io/github/actions/workflow/status/NightMare33133/starward-space/ci.yml?branch=main&style=for-the-badge&logo=githubactions&logoColor=white&label=CI%20Pipeline" alt="CI Pipeline" />
  </a>
  <a href="https://starward-space.pages.dev">
    <img src="https://img.shields.io/badge/Cloudflare_Pages-Deployed-F38020?style=for-the-badge&logo=cloudflarepages&logoColor=white" alt="Cloudflare Pages" />
  </a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21_LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot 3" />
  <img src="https://img.shields.io/badge/Vue.js-3.5-4FC08D?style=for-the-badge&logo=vuedotjs&logoColor=white" alt="Vue 3" />
  <img src="https://img.shields.io/badge/Vite-6.x-646CFF?style=for-the-badge&logo=vite&logoColor=white" alt="Vite" />
  <img src="https://img.shields.io/badge/Tailwind_CSS-3.4-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white" alt="Tailwind CSS" />
  <img src="https://img.shields.io/badge/MySQL-8.4_LTS-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL 8.4" />
  <img src="https://img.shields.io/badge/License-Apache_2.0-blue.svg?style=for-the-badge" alt="License" />
</p>

---

## 🧭 项目初心

> *“并不是为了向世界证明什么，而是在嘈杂的信息洪流与内卷浪潮里，亲手为自己搭建一个有温度、有审美、能长久安放灵魂的赛博自留地。”*

**Starward Space（星向空间）** 是一套融合**深空二次元极客美学（《崩坏：星穹铁道》星穹列车设计哲学）**与**工业级高可靠架构**自研的个人全栈数字空间。前端采用流光毛玻璃拟态（Glassmorphism）与高交互动效，后端采用 Spring Boot 3 与 Java 21 LTS 现代技术栈，并配备开箱即用的自动化运维与轻量级 CMS 控制中枢。

---

## 🌟 核心功能全景

### 1. 💽 星穹音乐厅 · 极客留声机 (`/music`)
* **黑胶唱机拟物物理动效**：
  * 高质感黑胶唱片：带凹槽光栅同心圆环纹，随播放状态平滑旋转（`animate-spin-slow`）；
  * 机械唱针臂物理联动：播放时机械臂平滑倾斜落针触碰唱片（`rotate-[24deg]`），暂停时归位；
  * 氛围星云光晕：背景根据音轨风格实时投射大尺寸高斯模糊径向星云光斑。
* **毫秒级 LRC 官方录音室歌词滚动引擎**：
  * 对接网易云音乐 HOYO-MiX 官方录音室大碟 LRC 时间戳流；
  * 视口平滑自适应居中滚动，当前演唱行以琥珀金-玫瑰粉-霓虹青大号流光渐变发光展示；
  * **随点随听（Click-to-Seek）**：点击歌词任意行即可毫秒级跳转播放；
  * 纯音乐（Instrumental）特殊占位：自动呈现虚数引擎脉冲波纹与空灵星际诗意文案。
* **全景星轨歌单与全局音轨协同**：
  * 双选项卡无缝切换「动态歌词」与「星轨歌单（5首精选 OST）」；
  * 列表循环 🔁、单曲循环 🔂、随机播放 🔀 一键轮转；
  * **Pinia 全局单例**：全站路由跳转音乐不间断、不重载；
  * **智能界面避让**：常规页面右下角显示迷你微缩黑胶胶囊岛，进入 `/music` 后右下角自动隐藏，告别双播放器冗余。

### 2. 🐾 帕姆列车长彩蛋与星轨控制中枢 (`/admin`)
* **帕姆 3 击彩蛋暗门**：
  * 顶部导航栏帕姆头像在 1 秒内**连续快速点击 3 次**，呼出科幻星轨全息终端弹窗；
  * 输入列车长跃迁密令（`starward-secret-token-2026`）验证，燃放星轨彩色粒子礼花（`canvas-confetti`）并自动跃迁至管理中枢；
  * 页脚（Footer）低调保留控制中枢直达入口。
* **文章管理与状态流转 (`/admin/posts`)**：
  * 数据大盘实时展示总文章数、已发布数、草稿箱数、累计阅读量；
  * 一键切换发布状态（🟢 已公开发布 / 🟡 草稿箱）；
  * 置顶权重切换、标签级联解绑、二次确认安全删除。
* **双栏沉浸式 Markdown 编辑器 (`/admin/posts/new` & `/admin/posts/edit/:id`)**：
  * 人体工学打磨：编辑器内按 `Tab` 键自动缩进 2 空格、全局 `Cmd+S` / `Ctrl+S` 快捷保存；
  * 快捷模板注入：`+ Mermaid 架构图`、`+ 引用提示框 (> [!NOTE])`；
  * 双栏实时预览、纯写作模式、纯预览模式自由切换。
* **标签分类中枢 (`/admin/tags`) 与碎语极速发射台 (`/admin/moments`)**：
  * 7 款二次元星际霓虹调色盘，标签增删管理；
  * 情绪 Emoji 点选 + 星际空间坐标标记，一键发射即时同步至前台广播流。
* **路由守卫与鉴权隔离**：
  * Vue Router 4 全局前置守卫（`beforeEach`）拦截未授权访问并自动重定向至登录页（`/admin/login`）；
  * 登录成功后携带 `redirect` 参数原路无缝回跳；
  * 前台公开导航栏与后台控制中枢智能隔离，杜绝双导航栏重叠干扰。

### 3. 📖 星际文章与知识沉淀 (`/posts` & `/posts/:id`)
* **专业级 Markdown 渲染引擎**：
  * 代码语法高亮（`highlight.js`）；
  * 实时架构与流程图渲染（`mermaid`）；
  * 警告与提示块（GitHub 风格 Alerts：`NOTE` / `TIP` / `IMPORTANT` / `WARNING`）；
  * 基于 `DOMPurify` 深度净化防御 XSS 跨站攻击。
* **多维检索**：支持按星际标签分类筛选、搜索以及阅读量自动累加。

### 4. 💬 星际碎语 (`/moments`)
* 瀑布式时间线流，支持轻量级灵感便签、动态广播与心情打卡；
* 配合二次元情绪 Emoji 胶囊与星际坐标徽章。

### 5. 📷 摄影视界 (`/gallery`)
* 瀑布流（Masonry）排版，展示画廊缩略图与视觉珍藏；
* 稀有度流光边框视觉特效；
* **Vue 3 `<teleport to="body">` 全屏大图 Lightbox**：突破父级 CSS 层叠上下文（Stacking Context）约束，彻底避免导航栏遮挡大图。

---

## 🛠️ 技术栈总览

| 层次 | 核心技术 / 库 | 用途与特性 |
| :--- | :--- | :--- |
| **前端框架** | **Vue 3.5 + TypeScript** | Composition API、响应式 `computed` / `ref`、单文件组件 |
| **构建工具** | **Vite 6** | 毫秒级冷启动、极速 HMR 热重载、生产打包自动优化 |
| **全局状态** | **Pinia 4** | 共享单例音乐播放器、深浅主题状态管理 |
| **路由体系** | **Vue Router 4** | 嵌套路由、路由守卫权限拦截、平滑滚动行为 |
| **样式与动效** | **Tailwind CSS 3.4** | 响应式原子类、毛玻璃 Glassmorphism、自定义星云动画 |
| **富文本与图表** | **Markdown-it + Mermaid** | Markdown 解析、KaTeX 数学公式、流程架构图实时绘制 |
| **图标与视觉** | **Lucide Vue Next + Confetti** | 现代化极简矢量图标、粒子礼花激励特效 |
| **后端核心** | **Spring Boot 3.3.4 (Java 21 LTS)** | RESTful API、嵌入式高性能 Tomcat、虚拟线程支持 |
| **持久层** | **MyBatis-Plus 3.5 / MyBatis 3** | 高效数据库 CRUD、条件构造器、SQL 预编译防注入 |
| **数据源** | **MySQL 8.4 LTS** | 生产级主业务数据存储、UTF-8 字符集支持 |
| **安全与防御** | **Custom Token Interceptor** | `X-Admin-Token` 列车长跃迁密令校验、敏感词/异常拦截 |
| **接口文档** | **Springdoc OpenAPI 3.0** | 自动化 Swagger UI 接口交互文档 (`/api/swagger-ui/index.html`) |

---

## 🚀 极速起航 (Quick Start)

### 1. 环境准备
* **JDK 21 LTS**（推荐 Eclipse Temurin 或 Homebrew OpenJDK 21）
* **Node.js 18+** / **npm 9+**
* **MySQL 8.4 LTS**
* **Maven 3.9+**

### 2. 一键启动 (全栈自愈脚本)
项目根目录下提供了全自动工程化管理脚本 [`dev.sh`](dev.sh)，集成了环境探测、MySQL 状态自检、端口冲突自愈与退出清理：

```bash
# 赋予执行权限并一键启动前后端全栈
chmod +x dev.sh
./dev.sh
```

> **提示**：终端中按下 `Ctrl + C`，脚本会自动捕获信号并优雅清理所有后台子进程。

也可以单独启动某一端：
```bash
./dev.sh backend   # 仅启动 Spring Boot 3 后端 (端口 8080)
./dev.sh frontend  # 仅启动 Vue 3 + Vite 前端 (端口 5173)
```

---

### 3. 手动分步启动

#### 启动后端 (Spring Boot 3)
```bash
cd starward-backend
mvn spring-boot:run
```
* **接口基址**：`http://localhost:8080/api`
* **Swagger 接口文档**：`http://localhost:8080/api/swagger-ui/index.html`

#### 启动前端 (Vue 3 + Vite)
```bash
cd starward-frontend
npm install
npm run dev
```
* **前台空间入口**：`http://localhost:5173`
* **星穹音乐厅**：`http://localhost:5173/music`
* **星轨控制中枢**：`http://localhost:5173/admin`

---

## 📁 目录结构规范

```text
starward-space/
├── dev.sh                     # 🌌 本地一键启动与端口自愈守护脚本
├── README.md                  # 📖 项目核心设计与全景说明文档
├── LICENSE                    # 📜 Apache 2.0 开源授权协议
├── starward-backend/          # ⚙️ 后端服务 (Spring Boot 3 + Java 21)
│   ├── src/main/java/top/starward/
│   │   ├── config/            # WebMvc、跨域与安全拦截器配置
│   │   ├── controller/        # RESTful 控制器 (Post, Tag, Moment, EasterEgg)
│   │   ├── entity/            # 数据库持久化实体
│   │   ├── dto/               # 数据传输对象与字段校验 (双向类型容错)
│   │   ├── mapper/            # MyBatis 数据访问接口
│   │   └── service/           # 业务逻辑契约与实现
│   └── src/main/resources/
│       ├── application.yml    # 全局核心配置与列车长密令定义
│       └── mapper/            # XML 复杂动态 SQL 映射
└── starward-frontend/         # 🎨 前端工程 (Vue 3 + Vite + Tailwind CSS)
    ├── src/
    │   ├── assets/            # 静态全局样式与动画
    │   ├── components/        # 公共组件 (Navbar, Footer, MusicPlayer, ConductorModal)
    │   ├── router/            # Vue Router 4 路由表与前置权限拦截守卫
    │   ├── stores/            # Pinia 全局状态仓 (music 响应式音轨流)
    │   ├── types/             # 全局 TypeScript 接口类型规范
    │   └── views/             # 视图页面
    │       ├── HomeView.vue         # 首页
    │       ├── PostsView.vue        # 文章列表页
    │       ├── PostDetailView.vue   # 文章详情页 (Markdown 渲染)
    │       ├── MusicView.vue        # 星穹音乐厅 (黑胶唱机拟态与 LRC 同步歌词)
    │       ├── MomentsView.vue      # 星际碎语时间线
    │       ├── GalleryView.vue      # 摄影视界 (瀑布流与 Teleport 弹窗)
    │       ├── AboutView.vue        # 空间介绍与关于页
    │       └── admin/               # 星轨管理中枢 (文章/标签/碎语 CMS)
    └── public/
        ├── audio/             # 星铁官方录音室 OST 本地音频大碟
        └── images/            # 角色壁纸、光锥珍藏与帕姆专属头像
```

---

## 🗺️ 未来星轨演进路线 (Roadmap)

- [x] **v0.5.0**：二次元深空毛玻璃视觉体系与响应式布局完成
- [x] **v0.8.0**：Spring Boot 3 CRUD 闭环、帕姆 3 击彩蛋与极客 CMS 控制中枢落地
- [x] **v0.9.0**：独立星穹音乐厅（黑胶留声机拟态、毫秒级官方原版 LRC 歌词同步滚屏）落地
- [ ] **v1.0.0**：Docker Compose 容器化编排、Nginx 生产安全加固与 HTTPS 证书配置
- [ ] **v1.2.0**：Redis 热度榜单排行榜、IP 频控限流与文章点赞互动
- [ ] **v2.0.0**：基于 LangGraph + RAG 的「**星际 AI 数字分身**」常驻智能体接入

---

## 📜 开源协议

本项目基于 [Apache License 2.0](LICENSE) 协议开源。欢迎学习交流，愿此行，终抵群星！🌟
