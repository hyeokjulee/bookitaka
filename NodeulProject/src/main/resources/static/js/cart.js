// 장바구니 목록을 가져와 테이블로 표시하는 함수
function showCarts(callback) {
    fetch('/cart/getCarts', {
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
                  var carts = data.carts;
                  var sheets = data.sheets;
                  var tableBody = $('#carts');
                  tableBody.empty();

                  for (var i = 0; i < carts.length; i++) {
                    var cart = carts[i];
                    var sheet = sheets[i];
                    var row = $('<tr></tr>');
                    row.append('<td><input type="checkbox" name="selectedItem" value="' + cart.sheetNo + '" data-price="' + sheet.sheetPrice + '" checked></td>');
                    row.append('<td><img src="/sheet/bookImg/' + sheet.sheetBookimguuid + sheet.sheetBookimgname + '" width="150" height="150"></td>');
                    row.append('<td>' + sheet.sheetBooktitle + '</td>');
                    row.append('<td>' + sheet.sheetPrice.toLocaleString() + '원</td>');
                    row.append('<td><button type="button" class="btn btn-primary btn-payingOne" value="' + cart.sheetNo + '">구매</button> <button type="button" class="btn btn-primary btn-delete" data-no="' + cart.sheetNo + '">삭제</button></td>');
                    tableBody.append(row);
                  }

                  callback();
                } else {
                  console.log('장바구니 상품 목록 조회 실패');
                }
            });
        } else if (response.status === 201) {
            console.log("토큰 재발급");
            showCarts(callback);
        } else {
            console.error('장바구니 상품 목록 조회 요청 중 에러 발생', response.status);
        }
    })
    .catch(error => {
        console.error('장바구니 상품 목록 조회 요청 실패:', error);
    })
}

// 상품 삭제 요청을 보내고 성공 시 장바구니 목록 갱신
function deleteCart(sheetNo) {
    fetch('/cart/deleteCart', {
        method: 'POST',
        body: new URLSearchParams({ sheetNo: sheetNo }),
        headers: {'ajax':true}
    })
    .then((response) => {
        if (response.status === 200) {
            response.json().then((data) => {
                if (data.success) {
                  showCarts(refresh);  // 목록 갱신
                } else {
                  console.log('상품 삭제 실패');
                }
            });
        } else if (response.status === 201) {
            console.log("토큰 재발급");
            deleteCart(sheetNo);
        } else {
            console.error('상품 삭제 요청 중 에러 발생', response.status);
        }
    }).catch(error => {
        console.error('상품 삭제 요청 실패:', error);
    });
}

// 비우기 요청을 보내고 성공 시 장바구니 목록 갱신
function emptyCart() {
    fetch('/cart/emptyCart', {
        method: 'POST',
        headers: {'ajax':true}
    })
    .then((response) => {
        if (response.status === 200) {
            response.json().then((data) => {
                if (data.success) {
                  showCarts(refresh);  // 목록 갱신
                } else {
                  console.log('비우기 실패');
                }
            });
        } else if (response.status === 201) {
            console.log("토큰 재발급");
            emptyCart();
        } else {
            console.error('비우기 요청 중 에러 발생', response.status);
        }
    }).catch(error => {
        console.error('비우기 요청 실패:', error);
    });
}

// 선택 삭제 요청을 보내고 성공 시 장바구니 목록 갱신
function deleteSelectedCart(selectedItems) {
    fetch('/cart/deleteSelectedCart', {
        method: 'POST',
        body: JSON.stringify(selectedItems),
        headers: {
            'ajax':true,
            'Content-Type': 'application/json'
        }
    })
    .then((response) => {
        if (response.status === 200) {
            response.json().then((data) => {
                if (data.success) {
                  showCarts(refresh);  // 목록 갱신
                } else {
                  console.log('선택 삭제 실패');
                }
            });
        } else if (response.status === 201) {
            console.log("토큰 재발급");
            deleteSelectedCart(selectedItems);
        } else {
            console.error('선택 삭제 요청 중 에러 발생', response.status);
        }
    }).catch(error => {
        console.error('선택 삭제 요청 실패:', error);
    });
}

// 건수랑 합계 새로고침
function refresh() {
  var totalSheets = $('[name="selectedItem"]').length;
  var checkedSheets = $('[name="selectedItem"]:checked').length;
  var isAllChecked = totalSheets === checkedSheets;
  var prices = $('[name="selectedItem"]:checked').map(function() {
    return parseInt($(this).data('price'));
  }).get();
  var sum = 0;
  prices.forEach(function(price) {
    sum += price;
  });

  $('#selectAll').prop('checked', isAllChecked);
  $('#totalcnt').text("총 " + checkedSheets + "건");
  $('#totalprice').text("합계 : " + sum.toLocaleString() + "원");
}

// 페이지 로드 시 장바구니 목록 표시
$(document).ready(function() {
    showCarts(refresh);
});

// 삭제 버튼 클릭 시 상품 삭제 처리
$(document).on('click', '.btn-delete', function() {
    if (!confirm("상품을 장바구니에서 삭제하시겠습니까?")) {
        return;
    }

    var sheetNo = $(this).data('no');
    deleteCart(sheetNo);
});

// 비우기 버튼 클릭 시 비우기 처리
$(document).on('click', '.btn-empty', function() {
    if (!confirm("장바구니를 비우시겠습니까?")) {
        return;
    }

    emptyCart();
});

// 선택삭제 버튼 클릭 시 선택삭제 처리
$(document).on('click', '.btn-deleteselected', function() {
    if (!confirm("선택한 상품들을 장바구니에서 삭제하시겠습니까?")) {
        return;
    }

    // 선택된 체크박스들을 수집합니다.
    var selectedItems = [];
    $('input[name="selectedItem"]:checked').each(function() {
      selectedItems.push($(this).val());
    });

    deleteSelectedCart(selectedItems);
});

// 전체선택 버튼 클릭 시 전체선택 처리
$(document).on('change', '#selectAll', function() {
    var isChecked = $(this).prop('checked');
    $('[name="selectedItem"]').prop('checked', isChecked).trigger("change");
});

// 상품 체크박스의 변경 이벤트 핸들러
$(document).on('change', '[name="selectedItem"]', function() {
    refresh(); // 삭제시 건수랑 합계 새로고침
});

// 구매하기 버튼 클릭 시 결제하기 페이지로 이동 처리
$(document).on('click', '.btn-paying', function() {

    // 선택된 체크박스들을 수집합니다.
  var selectedItems = [];
  $('input[name="selectedItem"]:checked').each(function() {
    selectedItems.push($(this).val());
  });

  if (selectedItems.length != 0) {
      if (confirm("결제 페이지로 이동하시겠습니까?")) {
          // 리스트를 문자열로 변환
          var encodedList = JSON.stringify(selectedItems);

          // 쿠키에 리스트 값 설정
          document.cookie = "carts=" + encodeURIComponent(encodedList) + "; path=/";

          window.location.href = "/payproc/paying";
      } else {
          return;
      }
  } else {
      alert("선택한 제품이 없습니다.")
  }
});

// 구매 버튼 클릭 시 결제하기 페이지로 이동 처리
$(document).on('click', '.btn-payingOne', function() {
    if (!confirm("결제 페이지로 이동하시겠습니까?")) {
        return;
    }

  var selectedItems = [];
  selectedItems.push($(this).val());

  // 리스트를 문자열로 변환
  var encodedList = JSON.stringify(selectedItems);

  // 쿠키에 리스트 값 설정
  document.cookie = "carts=" + encodeURIComponent(encodedList) + "; path=/";

  window.location.href = "/payproc/paying";
});