$(function(){
    $(".follow-btn").click(follow);
});

function follow() {
    var btn = this;
    $.post(
        CONTEXT_PATH + "/team/status",
        {"teamId":$(btn).prev().val()},
        function (data) {
            data = $.parseJSON(data);
            if (data.code == 0) {
                window.location.reload();
            } else {
                alert(data.msg);
            }
        }
    );
}
$(function(){
    $("#publishBtn").click(publish);
});

function publish() {
    $("#publishModal").modal("hide");

    //获取标题内容
    var name = $("#recipient-name").val();
    var introduce = $("#message-text").val();
    var endTime = $("#endTime").val();
    var needPerson = $("#person").val();
    var activityName = $("#activity").val();
    //发送异步请求
    $.post(
        CONTEXT_PATH+"/team/add",
        {"name":name,"introduce":introduce,"endTime":endTime,"needPerson":needPerson,"activityName":activityName},
        function(data){
            data = $.parseJSON(data);
            //在提示框中显示返回消息
            $("#hintBody").text(data.msg);
            //显示提示框
            $("#hintModal").modal("show");
            //2秒后，自动隐藏提示框
            setTimeout(function(){
                $("#hintModal").modal("hide");
                //刷新页面
                if(data.code == 200){
                    window.location.reload();
                }
            }, 2000);
        }
    );
}