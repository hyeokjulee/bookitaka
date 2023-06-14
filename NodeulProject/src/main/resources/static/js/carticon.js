function showCartCount() {
    fetch('/cart/getCount', {
        method: 'GET',
        headers: {
            'ajax':true,
            'Content-Type': 'application/json'
        }
    })
    .then((response) => {
        if (response.status === 200) {
            response.json().then((data) => {
                if (data.success) {
                  var count = data.count;
                  $('#cartCnt').text(count);
                } else {
                  console.log('장바구니 상품 목록 개수 조회 실패');
                }
            });
        } else if (response.status === 201) {
            console.log("토큰 재발급");
            showCartCount();
        } else {
            console.error('장바구니 상품 목록 개수 조회 요청 중 에러 발생', response.status);
        }
    }).catch(error => {
        console.error('장바구니 상품 목록 개수 조회 요청 실패:', error);
    });
}

// 페이지 로드 시 장바구니 목록 개수 표시
$(document).ready(function() {
    showCartCount();
});