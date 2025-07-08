// Custom JavaScript
// 예를 들어, 일정 시간 후에 플래시 메시지를 자동으로 닫는 기능
document.addEventListener('DOMContentLoaded', function() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 3000); // 3초 후 닫기
    });
});