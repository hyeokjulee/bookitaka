function showCount() {
    $.ajax({
      url: '/cart/getCount',  // 서버의 장바구니 상품 목록 개수 조회 API 엔드포인트
      type: 'GET',
      success: function(response) {
        if (response.success) {
          var count = response.count;
          $('#cartCnt').text(count);
        } else {
          console.log('장바구니 상품 목록 개수 조회 실패');
        }
      },
      error: function() {
        console.log('장바구니 상품 목록 개수 조회 요청 실패');
      }
    });
}

// 페이지 로드 시 장바구니 목록 개수 표시
$(document).ready(function() {
    showCount();
});