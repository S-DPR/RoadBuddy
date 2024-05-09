package org.hansung.roadbuddy.dto.rail.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class RailTransferResDto<mvContDtlType> {
    private int chtnMvTpOrdr; // 환승 이동 유형 순서
    private String stMovePath; // 출발지
    private String edMovePath; // 도착지
    private String elvtSttCd; // 승강기 상태 코드
    private String elvtTpCd; // 승강기 유형 코드
    private String imgPath; // 이미지 경로 url
    private mvContDtlType mvContDtl; // 상세 이동 내용
    private int mvPathMgNo; // 이동 경로 상세 경로
}
