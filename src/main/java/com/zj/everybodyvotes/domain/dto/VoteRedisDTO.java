package com.zj.everybodyvotes.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author cuberxp
 * @date 2022/4/12 22:50
 * @since 1.0.0
 */
@Data
public class VoteRedisDTO implements Serializable {
    private static final long SerializableUid = 0L;

    private Integer voteCount;

    private LocalDateTime beginStartTime;
}
