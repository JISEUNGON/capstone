package com.danram.server.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class PartyInfoImgNotDto {
    private String groupType; // 모집 타입
    private String groupName; // 모임 이름
    private String description; // 모집 상세 글
    private Long max; // 모집 최대 인원
    private String startAt; // 시작알
    private String endAt; // 종료일
    private String location; // 위치(시 구 동)
    private String alarmFrequency; // 알람 빈도(월, 화, 수)
    private String alarmTime; //알람 시간
}
