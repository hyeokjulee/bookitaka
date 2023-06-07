// 상품 목록을 가져와 테이블로 표시하는 함수
function showSheets(sheets) {
  var tableBody = $('#sheets');
  tableBody.empty();
  var sum = 0;

  for (var i = 0; i < sheets.length; i++) {
    var sheet = sheets[i];
    var row = $('<tr></tr>');
    row.append('<td><img src="/sheet/bookImg/' + sheet.sheetBookimguuid + sheet.sheetBookimgname + '" width="150" height="150"></td>');
    row.append('<td>' + sheet.sheetBooktitle + '</td>');
    row.append('<td>' + sheet.sheetPrice.toLocaleString() + '원</td>');
    tableBody.append(row);
    sum += parseInt(sheet.sheetPrice);
  }

  $('#totalcnt').text("총 " + sheets.length + "건");
  $('#totalprice').text("합계 : " + sum.toLocaleString() + "원");
}

// 활동지 번호들을 넘겨서 활동지들을 받고 목록을 표시하는 함수
function nosToSheets(decodedList) {
    console.log(decodedList);
    $.ajax({
      url: '/payproc/getSheets',  // 서버의 상품 삭제 API 엔드포인트
      type: 'POST',
      data: JSON.stringify(decodedList),
      contentType: "application/json",
      success: function(response) {
        if (response.success) {
          showSheets(response.sheets);  // 목록 갱신
        } else {
          console.log('상품 목록 조회 실패');
        }
      },
      error: function() {
        console.log('상품 목록 조회 요청 실패');
      }
    });
}

// 페이지 로드 시 이전 페이지에서 선택했던 목록 표시
$(document).ready(function() {
    var decodedList;

    // 쿠키 값을 가져오기
    var cookies = document.cookie.split("; ");
    for (var i = 0; i < cookies.length; i++) {
      var cookie = cookies[i].split("=");
      var name = cookie[0];
      var value = cookie[1];

      if (name.trim() === "carts") {
        decodedList = JSON.parse(decodeURIComponent(value));
        break;
      }
    }

    // 쿠키에 저장된 리스트 사용
    nosToSheets(decodedList);
});