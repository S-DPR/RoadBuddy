//package org.hansung.roadbuddy.service;
//
//import org.hansung.roadbuddy.dto.roadBuddy.request.EscalatorReqDto;
//import org.hansung.roadbuddy.entity.Escalator;
//import org.hansung.roadbuddy.repos.EscalatorRepos;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class EscalatorService {
//    private final EscalatorRepos escalatorRepos;
//
//    public EscalatorService(EscalatorRepos escalatorRepos) {
//        this.escalatorRepos = escalatorRepos;
//    }
//
//    public List<Escalator> findByLineName(EscalatorReqDto dto) {
//        return escalatorRepos.findByEscalatorName(dto.getEscalatorName());
//    }
//}
