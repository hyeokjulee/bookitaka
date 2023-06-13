// 전역 변수
var memberInfo = [];
var sum = 100;

// 내 정보 초기화
function memberSet(member) {
    memberInfo.push(member.memberEmail);
    memberInfo.push(member.memberName);
    memberInfo.push(member.memberPhone);
}

// 내 정보 조회
function myInfo() {
    $.ajax({
      url: '/coupon/myInfo',  // 서버의 내 정보 조회 API 엔드포인트
      type: 'POST',
      success: function(response) {
        if (response.success) {
          memberSet(response.member);  // 내 정보 초기화
        } else {
          console.log('내 정보 조회 실패');
        }
      },
      error: function() {
        console.log('내 정보 조회 요청 실패');
      }
    });
}

// 페이지 로드 시 내 정보 초기화
$(document).ready(function() {
    myInfo();
});