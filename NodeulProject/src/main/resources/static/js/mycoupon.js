function showCouponCount() {
    $.ajax({
      url: '/coupon/getCount',  // 서버의 쿠폰 개수 조회 API 엔드포인트
      type: 'GET',
      success: function(response) {
        if (response.success) {
          var couponcount = response.couponcount;
          $('#couponCnt').text("쿠폰 개수 : " + couponcount);
        } else {
          console.log('쿠폰 개수 조회 실패');
        }
      },
      error: function() {
        console.log('쿠폰 개수 조회 요청 실패');
      }
    });
}

// 페이지 로드 시 쿠폰 개수 표시
$(document).ready(function() {
    showCouponCount();
});