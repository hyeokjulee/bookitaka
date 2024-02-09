// 전역 변수
var memberInfo = [];
var sum = 100000;

// 내 정보 초기화
function memberSet(member) {
    memberInfo.push(member.memberEmail);
    memberInfo.push(member.memberName);
    memberInfo.push(member.memberPhone);
}

// 내 정보 조회
function myInfo() {
    fetch('/coupon/myInfo', {
        method: 'POST',
        headers: {
            'ajax':true,
            'Content-Type': 'application/json'
        }
    })
    .then((response) => {
        if (response.status === 200) {
            response.json().then((data) => {
                if (data.success) {
                  memberSet(data.member);  // 내 정보 초기화
                } else {
                  console.log('내 정보 조회 실패');
                }
            });
        } else if (response.status === 201) {
            console.log("토큰 재발급");
            myInfo();
        } else {
            console.error('내 정보 조회 요청 중 에러 발생', response.status);
        }
    }).catch(error => {
        console.error('내 정보 조회 요청 실패:', error);
    });
}

// 페이지 로드 시 내 정보 초기화
$(document).ready(function() {
    myInfo();
});