 //控制层 
app.controller('userController' ,function($scope,$controller ,userService){

    //注册用户
    $scope.reg = function () {
        //比较两次输入密码是否一致
        if($scope.password != $scope.entity.password){
            $scope.password = "";
            $scope.entity.password = "";
            return ;
        }
        //增加
        userService.add($scope.entity,$scope.smsCode).success(
            function (response) {
                alert(response.message);
        })
    }
    
    //发送验证码
    $scope.sendCode = function () {
        if ($scope.entity.phone == null || $scope.entity.phone == ""){
            alert("请输入手机号!");
            return ;
        }
        userService.sendCode($scope.entity.phone).success(
            function (response) {
                alert(response.message);
            }
        )
    }
    
});	
