package com.bookitaka.NodeulProject.sheet.mysheet;

import java.util.List;

public interface MysheetService {

    public List<Mysheet> getAllMysheetByEmail(String memberEmail);

    public List<Mysheet> getAllMysheetAvailableByEmail(String memberEmail);

    public List<Mysheet> getAllMysheetForAdmin();

}
