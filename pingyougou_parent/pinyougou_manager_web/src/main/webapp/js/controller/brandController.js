//品牌控制器
app.controller('brandController', function ($scope,$controller,brandService) {

    $controller('baseController',{$scope:$scope});

    //查询品牌列表
    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    };

    //查询实体
    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    };

    //保存方法
    $scope.save = function () {
        var object = null;
        if ($scope.entity.id != null) {
            object = brandService.update($scope.entity);
        } else {
            object = brandService.add($scope.entity);
        }
        object.success(
            function (response) {
                if (response.success) {//如果成功
                    $scope.reloadList();//刷新列表
                } else {
                    alert(response.message);//提示信息
                }
            }
        );
    };

    //删除
    $scope.dele = function () {
        if (confirm('确定要删除吗？')) {
            brandService.dele( $scope.selectIds).success(
                function (response) {
                    if (response.success) {
                        $scope.reloadList();//刷新
                    } else {
                        alert(response.message);
                    }
                }
            );
        }
    };

    $scope.searchEntity = {};
    //条件查询
    $scope.search = function (page, size) {
        brandService.search(page, size,$scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;//显示当前页数据
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

});
