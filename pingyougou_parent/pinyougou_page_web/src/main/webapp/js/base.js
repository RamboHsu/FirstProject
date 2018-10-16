var app = angular.module('pinyougou', []);//定义品优购模块
//$sce服务写成过滤器
app.filter('trustHtml',['$sce',function ($sce) {
    return function (data) {
        return $sce.trustAsHtml(data);
    }
}]);