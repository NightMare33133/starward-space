package top.starward.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.starward.dto.MomentCreateRequest;
import top.starward.entity.Moment;
import top.starward.mapper.MomentMapper;
import top.starward.service.MomentService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MomentServiceImpl implements MomentService {

    private final MomentMapper momentMapper;

    @Override
    public List<Moment> getMomentTimeline() {
        return momentMapper.findAllMoments();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Moment createMoment(MomentCreateRequest request) {
        Moment moment = Moment.builder()
                .content(request.getContent().trim())
                .mood(request.getMood() != null ? request.getMood().trim() : "✨")
                .imagesJson(request.getImagesJson())
                .isPinned(0)
                .build();

        momentMapper.insertMoment(moment);
        log.info("成功发布星际碎语 ID: {}", moment.getId());
        return moment;
    }
}
