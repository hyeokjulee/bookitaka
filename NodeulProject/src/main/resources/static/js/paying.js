// 전역 변수
var sum = 0;
var cnt = 0;
var memberInfo = [];
var sheetInfo = [];
var sInfo = "";
var decodedList;
var usedCoupon = 0;

// 내 정보 초기화
function memberSet(member) {
    memberInfo.push(member.memberEmail);
    memberInfo.push(member.memberName);
    memberInfo.push(member.memberPhone);
}

// 상품 목록을 가져와 테이블로 표시하는 함수
function showSheets(sheets) {
  var tableBody = $('#sheets');
  tableBody.empty();

  for (var i = 0; i < sheets.length; i++) {
    var sheet = sheets[i];
    var row = $('<tr></tr>');
    row.append('<td><img src="/sheet/bookImg/' + sheet.sheetBookimguuid + sheet.sheetBookimgname + '" width="150" height="150" style="object-fit: contain"></td>');
    row.append('<td>' + sheet.sheetBooktitle + '</td>');
    row.append('<td>'+ '<input type="checkbox" name="selectedCoupon" data-price=\"' + sheet.sheetPrice + '\"> </td>');
    row.append('<td>' + sheet.sheetPrice.toLocaleString() + '원</td>');
    tableBody.append(row);
    sum += parseInt(sheet.sheetPrice);

    sheetInfo.push(sheet.sheetBooktitle);
  }

  $('#totalcnt').text("총 " + sheets.length + "건");
  $('#totalprice').text("합계 : " + sum.toLocaleString() + "원");

  cnt = sheets.length;
}

// 활동지 번호들을 넘겨서 활동지들을 받고 목록을 표시하는 함수
function nosToSheets(decodedList) {
    fetch('/payproc/getSheets', {
        method: 'POST',
        body: JSON.stringify(decodedList),
        headers: {
            'ajax':true,
            'Content-Type': 'application/json'
        }
    })
    .then((response) => {
        if (response.status === 200) {
            response.json().then((data) => {
                if (data.success) {
                    showSheets(data.sheets);  // 목록 갱신
                    memberSet(data.member);  // 내 정보 초기화
                } else {
                    console.error('상품 목록 조회 실패');
                }
            });
        } else if (response.status === 201) {
            console.log("토큰 재발급");
            nosToSheets(decodedList);
        } else {
            console.error('상품 목록 조회 요청 중 에러 발생', response.status);
        }
    }).catch(error => {
        console.error('상품 목록 조회 요청 실패:', error);
    });
}

// 결제완료시 쿠키에 있는 목록 선택 삭제 요청을 보냄
function deleteSelectedCart() {
    fetch('/cart/deleteSelectedCart', {
        method: 'POST',
        body: JSON.stringify(decodedList),
        headers: {
            'ajax':true,
            'Content-Type': 'application/json'
        }
    })
    .then((response) => {
        if (response.status === 200) {
            response.json().then((data) => {
                if (data.success) {
                  console.log('선택 삭제 성공');
                } else {
                  console.log('선택 삭제 실패');
                }
            });
        } else if (response.status === 201) {
            console.log("토큰 재발급");
            deleteSelectedCart();
        } else {
            console.error('선택 삭제 요청 중 에러 발생', response.status);
        }
    }).catch(error => {
        console.error('선택 삭제 요청 실패:', error);
    });
}

// 페이지 로드 시 이전 페이지에서 선택했던 목록 표시
$(document).ready(function() {
    // 쿠키 값을 가져오기
    var cookies = document.cookie.split("; ");
    for (var i = 0; i < cookies.length; i++) {
      var cookie = cookies[i].split("=");
      var name = cookie[0];
      var value = cookie[1];

      if (name.trim() === "carts") {
        decodedList = JSON.parse(decodeURIComponent(value));

        // 쿠키에 저장된 리스트 사용
        nosToSheets(decodedList);

        break;
      }
    }
});

// 건수랑 합계, 쿠폰 남은 갯수까지 새로고침
function refresh() {
    var totalCoupons = $('[name="selectedCoupon"]').length;
    var checkedCoupons = $('[name="selectedCoupon"]:checked').length;
    var isAllChecked = totalCoupons === checkedCoupons;
    var prices = $('[name="selectedCoupon"]:not(:checked)').map(function() {
        return parseInt($(this).data('price'));
    }).get();
    var sum = 0;
    prices.forEach(function(price) {
        sum += price;
    });
    window.sum = sum;
    window.usedCoupon = checkedCoupons;

    let couponLeftCnt = parseInt($('#couponLeft').data("left")) - checkedCoupons;

    $('#couponLeft').text("쿠폰사용 : (남은갯수 " + couponLeftCnt + ")");
    $('#couponAll').prop('checked', isAllChecked);
    $('#totalprice').text("합계 : " + sum.toLocaleString() + "원");

}

// 쿠폰 전체선택 버튼 클릭 시 전체선택 처리
$(document).on('change', '#couponAll', function() {
    var totalCoupons = $('[name="selectedCoupon"]').length;
    let totalLeftCoupon = parseInt($('#couponLeft').data("left"));
    if (totalCoupons > totalLeftCoupon) {
        alert("쿠폰이 부족합니다")
        $(this).prop('checked', false);
    } else {
        var isChecked = $(this).prop('checked');
        $('[name="selectedCoupon"]').prop('checked', isChecked).trigger("change");
    }

});

// 쿠폰 체크박스의 변경 이벤트 핸들러
$(document).on('change', '[name="selectedCoupon"]', function() {
    let checkbox = $(this);
    let row = checkbox.closest('tr');
    let priceCell = row.find('td:eq(3)'); // 4번째 열에 위치한 가격 정보

    var checkedCoupons = $('[name="selectedCoupon"]:checked').length;
    let couponLeftCnt = parseInt($('#couponLeft').data("left")) - checkedCoupons;

    if (checkbox.is(':checked')) {
        // 체크되었을 때의 동작
        if (couponLeftCnt < 0) {
            alert("쿠폰이 부족합니다.")
            checkbox.prop('checked', false);
        } else {
            priceCell.text("0(쿠폰사용)");
            sheetInfo[row.index()] = sheetInfo[row.index()] + "(쿠폰)" //쿠폰구입은 쿠폰이라고 붙여주기
        }
    } else {
        // 체크가 해제되었을 때의 동작
        var savedPrice = checkbox.data('price'); // 저장된 가격 가져오기
        priceCell.text(savedPrice.toLocaleString() + '원'); // 원래 가격으로 변경
        sheetInfo[row.index()] = sheetInfo[row.index()].slice(0, -4); //쿠폰이라고 붙였던거 떼기

    }

    refresh();
});