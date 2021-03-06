/**
 * Created by libiya on 15/6/27.
 */

var anicloud = anicloud || {};
anicloud.sunny = anicloud.sunny || {};
anicloud.sunny.controller = anicloud.sunny.controller || {};

anicloud.sunny.controller.FeatureEditCtrl = function ($rootScope, $scope, ManagerService) {
    // $scope.$watch('trigger',function (newVal,oldVal) {
    //     var latestTaskTime;
    //     if(newVal['triggerType']==="TIMER"){
    //         latestTaskTime=moment(new Date());
    //         $rootScope.strategies.forEach(function (strategy) {
    //             if(strategy.state==='RUNNING'){
    //                 strategy.featureList.forEach(function (feature) {
    //                     if(feature.device.id==$scope.device.id&&feature.deviceFeature.featureId==$scope.feature.featureId){
    //                         feature.triggerDtoList.forEach(function (triggerDto) {
    //                             var startTime=JSON.parse(triggerDto.value)['startTime'];
    //                             console.log(startTime);
    //                             startTime=moment(startTime);
    //                             console.log('temp1',JSON.parse(triggerDto.value)['startTime'],startTime,latestTaskTime);
    //                             if(latestTaskTime.isBefore(startTime)) latestTaskTime=startTime;
    //                         })
    //                     }
    //                 })
    //             }
    //         });
    //         console.log('latestTaskTime',latestTaskTime,moment(latestTaskTime).add(1,'months'));
    //         $('#datetimepicker12').datetimepicker({
    //             inline: true,
    //             minDate:latestTaskTime,
    //             maxDate:moment(latestTaskTime).add(1,'months')
    //         });
    //     }
    // },true);
    // feature Template
    $scope.connectedDevices=$scope.devices.filter(function (device) {
       return device.deviceState==="CONNECTED";
    });
    $scope.device = null;
    $scope.feature = null;
    $scope.trigger = {
        "triggerValue": "",
        "triggerType": ""
    };
    $scope.triggerTimer = {
        "startTime": new Date(),
        "repeatInterval": 0,
        "repeatCount": 0
    };
    $scope.isCalenderOpen = true;
    $scope.valid = true;
    $scope.error = "";

    $scope.getDeviceFeatures = function () {
        if ($scope.device == null) {
            return null;
        }
        return $rootScope.features[$scope.device.id].filter(function (feature) {
            return feature.privilegeType==="EXECUTE";
        });
    };
    $scope.initFeatureParam=function (feature) {
        $scope.featureArgMap = {};
        feature.argDtoList.forEach(function (item) {
            $scope.featureArgMap[item.name]=$scope.device.initParam[item.name];
        });
    };
    $scope.addFeature = function (strategy) {
        $scope.valid = true;

        // if ($scope.device == null) {
        //     $scope.valid = false;
        //     $scope.error = "设备不能为空";
        //     return false;
        // }
        // if ($scope.feature == null) {
        //     $scope.valid = false;
        //     $scope.error = "任务不能为空";
        //     return false;
        // }
        // if ($scope.trigger.triggerType == '') {
        //     $scope.valid = false;
        //     $scope.error = "触发条件不能为空";
        //     return false;
        // }

        var argumentList = [];
        for (var arg in $scope.featureArgMap) {
            var obj = {};
            obj.argName = arg;
            obj.value = $scope.featureArgMap[arg];
            argumentList.push(obj);
        }
        console.log(argumentList);

        if ($scope.trigger.triggerType == "TIMER") {
            console.log($scope.triggerTimer);
            $scope.trigger.triggerValue = JSON.stringify($scope.triggerTimer);
            console.log($scope.trigger.triggerValue);
        }

        var featureInstance = new anicloud.sunny.model.FeatureInstance(
            "",
            $scope.device,
            $scope.feature,
            argumentList,
            [$scope.trigger],
            false,
            $scope.currentAbsTime
        );
        ManagerService.addFeature(featureInstance, strategy);
        return true;
    };

    $scope.openCalendar = function(e) {
        e.preventDefault();
        e.stopPropagation();

        $scope.isCalenderOpen = true;
    };
    //
    var jsonClone = function (obj) {
        return JSON.parse(JSON.stringify(obj));
    };

        $scope.mytime = new Date();

        $scope.hstep = 1;
        $scope.mstep = 1;

        $scope.options = {
            hstep: [1, 2, 3],
            mstep: [1, 5, 10, 15, 25, 30]
        };

        $scope.ismeridian = true;
        $scope.toggleMode = function() {
            $scope.ismeridian = ! $scope.ismeridian;
        };
    $scope.lastFeatureAbsTime=$scope.ngDialogOpenNum?$scope.strategyTemplate.featureList[$scope.ngDialogOpenNum-1].absTime:$scope.strategyRepeat.startTime
    $scope.currentAbsTime=$scope.lastFeatureAbsTime;
    //console.log($scope.currentAbsTime,$scope.strategyRepeat.startTime);
    $scope.currentRelTime=$scope.currentAbsTime.from($scope.lastFeatureAbsTime);
    $scope.changeInterval=function (ary) {
        var num=ary[0],unit=ary[1];
        if(num>0){
            var newTime=moment($scope.currentAbsTime).add(Math.abs(num),unit)
        }else{
            newTime=moment($scope.currentAbsTime).subtract(Math.abs(num),unit)
        }
        if(newTime.isBefore(moment($scope.strategyRepeat.startTime).add(1,'day'))
            &&newTime.isAfter(moment($scope.lastFeatureAbsTime))){
            $scope.currentAbsTime=newTime;
            $scope.currentRelTime=$scope.currentAbsTime.from($scope.lastFeatureAbsTime);
        }else{
            return false;
        }
    }
};