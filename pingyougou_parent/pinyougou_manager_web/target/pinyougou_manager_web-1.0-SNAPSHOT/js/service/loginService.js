//登录服务层
app.service('loginService',function ($http) {
    //读取登录人名
    this.loginName=function () {
       return $http.get('../login/name.do')
    }
});