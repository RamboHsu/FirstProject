app.controller('searchController', function ($scope,$location, searchService) {

    //定义搜索对象的结构  category是商品分类
    $scope.searchMap = {
        'keywords': '',
        'category': '',
        'brand': '',
        'spec': {},
        'price': '',
        'pageNo': 1,
        'pageSize': 40,
        'sortField': '',
        'sort': ''
    };//搜索条件封装对象

    //搜索
    $scope.search = function () {
        $scope.searchMap.pageNo = parseInt($scope.searchMap.pageNo);
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;//搜索返回的结果
                buildPageLabel();//调用分页方法
            }
        )
    }

    //构建分页标签
    buildPageLabel = function () {
        $scope.pageLabel = [];//新增分页栏属性
        var firstPage = 1;//起始页码
        var lastPage = $scope.resultMap.totalPages;//截止页码
        $scope.firstDot = true;//前面有点
        $scope.lastDot = true;//后面有点
        if ($scope.resultMap.totalPages > 5) {                                        //如果总页数大于5，显示部分页码
            if ($scope.searchMap.pageNo <= 3) {//如果当前页≤3
                lastPage = 5;//前5页
                $scope.firstDot = false;
            } else if ($scope.searchMap.pageNo >= lastPage - 2) {   //如果当前页≥总页码-2
                firstPage = $scope.resultMap.totalPages - 4;//后5页
                $scope.lastDot = false;
            } else {                                                 //如果当前页为中间的任意连续5页
                firstPage = $scope.searchMap.pageNo - 2;
                lastPage = $scope.searchMap.pageNo + 2;
            }
        } else {
            $scope.firstDot = false;
            $scope.lastDot = false;
        }
        for (var i = firstPage; i <= lastPage; i++) {//循环产生页码标签
            $scope.pageLabel.push(i);
        }
    }


    //添加搜索项
    $scope.addSearchItem = function (key, value) {
        if (key == 'category' || key == 'brand' || key == 'price') {//如果点击的是分类或者品牌
            $scope.searchMap[key] = value;
        } else {
            $scope.searchMap.spec[key] = value;
        }
        $scope.search();//执行搜索
    }

    //移除复合搜索条件
    $scope.removeSearchItem = function (key) {
        if (key == 'category' || key == 'brand' || key == 'price') {//如果点击的是分类或者品牌
            $scope.searchMap[key] = '';
        } else {
            delete $scope.searchMap.spec[key];//移除此属性
        }
        $scope.search();//执行搜索
    }

    //分页查询
    $scope.queryByPage = function (pageNo) {
        //页码验证
        if (pageNo < 1 || pageNo > $scope.resultMap.totalPages) {
            return;
        }
        $scope.searchMap.pageNo = pageNo;
        $scope.search();
    }

    //判断当前页为第一页
    $scope.isTopPage = function () {
        if ($scope.searchMap.pageNo == 1) {
            return true;
        } else {
            return false;
        }
    }

    //判断当前页是否为最后一页
    $scope.isEndPage = function () {
        if ($scope.searchMap.pageNo == $scope.resultMap.totalPages) {
            return true;
        } else {
            return false;
        }
    }

    //设置排序规则
    $scope.sortSearch=function (sortField, sort) {
        $scope.searchMap.sortField = sortField;
        $scope.searchMap.sort = sort;
        $scope.search();
    }

    //判断关键字是否为品牌
    $scope.keywordsIsBrand=function () {
        for(var i=0;i<$scope.resultMap.brandList.length;i++){
            if ($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text)>=0){
                return true;
            }
        }
        return false;
    }

    //加载关键字
    $scope.loadKeywords=function () {
        $scope.searchMap.keywords=$location.search()['keywords'];
        $scope.search();
    }
})