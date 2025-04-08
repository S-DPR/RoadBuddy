package org.hansung.roadbuddy.dto.naver.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hansung.roadbuddy.dto.Coordinate;
import org.hansung.roadbuddy.dto.naver.response.PlaceSearchItem;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class PlaceSearchItemReqDto extends PlaceSearchItem {
    Coordinate coordinate = null;
}
