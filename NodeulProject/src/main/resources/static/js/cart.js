// 장바구니 목록을 가져와 테이블로 표시하는 함수
function showCarts() {
    $.ajax({
      url: '/cart/getCarts',  // 서버의 장바구니 상품 목록 조회 API 엔드포인트
      type: 'GET',
      success: function(response) {
        if (response.success) {
          var carts = response.carts;
          var tableBody = $('#carts');
          tableBody.empty();

          for (var i = 0; i < carts.length; i++) {
            var cart = carts[i];
            var row = $('<tr></tr>');
            row.append('<td><input type="checkbox" name="selectedItem" value="' + cart.sheetNo + '"></td>');
            row.append('<td>' + cart.memberEmail + '</td>');
            row.append('<td>' + cart.sheetNo + '</td>');
            row.append('<td>' + cart.cartRegdate + '</td>');
            row.append('<td><button type="button" class="btn btn-secondary">구매하기</button><button type="button" class="btn btn-secondary btn-delete" data-no="' + cart.sheetNo + '">삭제</button></td>');
            tableBody.append(row);
          }
        } else {
          console.log('장바구니 상품 목록 조회 실패');
        }
      },
      error: function() {
        console.log('장바구니 상품 목록 조회 요청 실패');
      }
    });
}

// 상품 삭제 요청을 보내고 성공 시 장바구니 목록 갱신
function deleteCart(sheetNo) {
    $.ajax({
      url: '/cart/deleteCart',  // 서버의 상품 삭제 API 엔드포인트
      type: 'POST',
      data: { sheetNo: sheetNo },
      beforeSend: function(xhr) {
        xhr.setRequestHeader('X-CSRF-TOKEN', $('meta[name="_csrf"]').attr('content'));
      },
      success: function(response) {
        if (response.success) {
          showCarts();  // 목록 갱신
        } else {
          console.log('상품 삭제 실패');
        }
      },
      error: function() {
        console.log('상품 삭제 요청 실패');
      }
    });
}

// 비우기 요청을 보내고 성공 시 장바구니 목록 갱신
function emptyCart() {
    $.ajax({
      url: '/cart/emptyCart',  // 서버의 비우기 API 엔드포인트
      type: 'POST',
      beforeSend: function(xhr) {
        xhr.setRequestHeader('X-CSRF-TOKEN', $('meta[name="_csrf"]').attr('content'));
      },
      success: function(response) {
        if (response.success) {
          showCarts();  // 목록 갱신
        } else {
          console.log('비우기 실패');
        }
      },
      error: function() {
        console.log('비우기 요청 실패');
      }
    });
}

// 선택 삭제 요청을 보내고 성공 시 장바구니 목록 갱신
function deleteSelectedCart() {
  // 선택된 체크박스들을 수집합니다.
  var selectedItems = [];
  $('input[name="selectedItem"]:checked').each(function() {
    selectedItems.push($(this).val());
  });

  // AJAX 요청을 보냅니다.
  $.ajax({
    type: 'POST',
    url: '/cart/deleteSelectedCart',
    data: JSON.stringify(selectedItems),
    contentType: 'application/json',
    beforeSend: function(xhr) {
      xhr.setRequestHeader('X-CSRF-TOKEN', $('meta[name="_csrf"]').attr('content'));
    },
    success: function(response) {
      if (response.success) {
          showCarts();  // 목록 갱신
        } else {
          console.log('선택 삭제 실패');
        }
    },
    error: function() {
      console.log('선택 삭제 요청 실패');
    }
  });
}

// 페이지 로드 시 장바구니 목록 표시
$(document).ready(function() {
    showCarts();
});

// 삭제 버튼 클릭 시 상품 삭제 처리
$(document).on('click', '.btn-delete', function() {
    var sheetNo = $(this).data('no');
    deleteCart(sheetNo);
});

// 비우기 버튼 클릭 시 비우기 처리
$(document).on('click', '.btn-empty', function() {
    emptyCart();
});

// 선택삭제 버튼 클릭 시 선택삭제 처리
$(document).on('click', '.btn-deleteselected', function() {
    deleteSelectedCart();
});

// 전체선택 버튼 클릭 시 전체선택 처리
$(document).on('change', '#selectAll', function() {
    var isChecked = $(this).prop('checked');
    $('[name="selectedItem"]').prop('checked', isChecked).trigger("change");
});

// 상품 체크박스의 변경 이벤트 핸들러
$(document).on('change', '[name="selectedItem"]', function() {
    var totalSheets = $('[name="selectedItem"]').length;
    var checkedSheets = $('[name="selectedItem"]:checked').length;
    var isAllChecked = totalSheets === checkedSheets;

    $('#selectAll').prop('checked', isAllChecked);
    $('#totalcnt').text("총 " + checkedSheets + "건");
});