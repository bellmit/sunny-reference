/**
 * Created by zhaoyu on 15-6-10.
 */

var anicloud = anicloud || {};
anicloud.sunny = anicloud.sunny || {};
anicloud.sunny.controller = anicloud.sunny.controller || {};

anicloud.sunny.controller.HomePageCtrl = function ($scope, $rootScope) {
    $scope.isstart = true;
    $scope.name = "";
    $scope.style = "error";
    $rootScope.config = {isstart:true, isopen:true};
}