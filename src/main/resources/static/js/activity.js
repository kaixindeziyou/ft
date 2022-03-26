$(function(){
    $("#publishBtn").click(publish);
});

function publish() {
    $("#publishModal").modal("hide");

    //获取标题内容
    var title = $("#recipient-name").val();
    var content = $("#message-text").val();
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
    var email = $("#recipient-email").val();
    var formData = new FormData();
    formData.append("title",title);
    formData.append("content",content);
    formData.append("startTime",startTime);
    formData.append("endTime",endTime);
    formData.append("email",email);
    formData.append("photo",$("#head-image")[0].files[0]);
    //发送异步请求
    $.ajax({
        type : "post",
        url :CONTEXT_PATH + "/activity/add",
        data : formData,
        processData : false,
        contentType : false,
        success : function(data) {
            data = $.parseJSON(data);
            //在提示框中显示返回消息
            $("#hintBody").text(data.msg);
            //显示提示框
            $("#hintModal").modal("show");
            //2秒后，自动隐藏提示框
            setTimeout(function () {
                $("#hintModal").modal("hide");
                //刷新页面
                if (data.code == 200) {
                    window.location.reload();
                }
            }, 2000);
        }
    });
}