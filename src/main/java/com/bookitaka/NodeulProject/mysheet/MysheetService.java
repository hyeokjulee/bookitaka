package com.bookitaka.NodeulProject.mysheet;

import com.bookitaka.NodeulProject.member.model.Member;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MysheetService {

    public List<SheetForMemberDto> getAllMysheetByMember(MysheetCri mysheetCri, Member member);

    public Long getMySheetCnt(String searchType, String searchWord, Member member);

    public Mysheet canIDownloadSheet(String fileUuid, Member member);

    public boolean checkMySheetIsAvailable(Mysheet mysheet);

    public Page<Mysheet> getAllMysheetForAdmin();

}
