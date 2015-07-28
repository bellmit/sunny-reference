/**
 * Created by libiya on 15/6/27.
 */

var anicloud = anicloud || {};
anicloud.sunny = anicloud.sunny || {};
anicloud.sunny.controller = anicloud.sunny.controller || {};

anicloud.sunny.controller.StrategyCtrl = function ($rootScope, $scope, ngDialog, StrategyService, DeviceService) {
    //    For Strategy page
    // feature Template
    $scope.featureTemplate = {
        "featureId": "",
        "deviceId": "",
        "trigger": {
            "triggerValue": "",
            "triggerType": ""
        },
        "argumentList": [],
        "valid" : true,
        "error" : ""
    };


    $scope.featureTemplate.clearAll = function () {
        $scope.featureTemplate.deviceId = "";
        $scope.featureTemplate.featureId = "";
        $scope.featureTemplate.trigger.triggerType = "";
        $scope.featureTemplate.trigger.triggerValue = "";
        $scope.featureTemplate.argumentList = [];
    };

    $scope.featureTemplate.getDeviceFeatures = function () {
        if ($scope.featureTemplate.deviceId == '') {
            return null;
        }
        return $rootScope.features[$scope.featureTemplate.deviceId];
    };


    $scope.featureTemplate.getDevice = function () {
        if ($scope.featureTemplate.deviceId == '') {
            return null;
        }
        for (var i = 0; i < $rootScope.devices.length; i++) {
            if ($rootScope.devices[i].id == $scope.featureTemplate.deviceId) {
                return $rootScope.devices[i];
            }
        }
        return null;
    }

    $scope.featureTemplate.getFeature = function () {
        if ($scope.featureTemplate.featureId == '') {
            return null;
        }
        for (var i = 0; i < $rootScope.features[$scope.featureTemplate.deviceId].length; i++) {
            if ($rootScope.features[$scope.featureTemplate.deviceId][i].featureId == $scope.featureTemplate.featureId) {
                return $rootScope.features[$scope.featureTemplate.deviceId][i];
            }
        }
        // Todo : add feature args
        return null;
    }

    $scope.featureTemplate.open = function() {
        ngDialog.open(
            {
                template: 'public/src/view/feature.html',
                scope: $scope
            });
    }

    $scope.addFeature = function (strategy) {
        $scope.featureTemplate.valid = true;
        var feature = $scope.featureTemplate.getFeature();
        var device = $scope.featureTemplate.getDevice();
        var trigger = $scope.featureTemplate.trigger;
        var argumentList = $scope.featureTemplate.argumentList;

        if (device == null) {
            $scope.featureTemplate.valid = false;
            $scope.featureTemplate.error = "设备不能为空";
            return false;
        }
        if (feature == null) {
            $scope.featureTemplate.valid = false;
            $scope.featureTemplate.error = "任务不能为空";
            return false;
        }
        if (trigger.triggerType == '') {
            $scope.featureTemplate.valid = false;
            $scope.featureTemplate.error = "触发条件不能为空";
            return false;
        }
        if (trigger.triggerType == 'TIMER' && trigger.triggerValue == '') {
            $scope.featureTemplate.valid = false;
            $scope.featureTemplate.error = "选择时间";
            return false;
        }

        var featureInstance = new anicloud.sunny.model.FeatureInstance(
            feature.featureId,
            feature.featureName,
            device,
            argumentList,
            [trigger]
        );
        strategy.featureList.push(jsonClone(featureInstance));
        return true;
    };

    $scope.deleteFeature = function (index, strategy) {
        strategy.featureList.splice(index, 1);
    };

    // strategy Template
    $scope.strategyTemplate = {
        "strategyId": "",
        "strategyName": "",
        "strategyState": "",
        "strategyDescription": "",
        "strategyStage": "",
        "featureList": [],
        "valid" : true,
        "error" : ""
    };

    $scope.strategyTemplate.clearAll = function () {
        $scope.strategyTemplate.strategyName = "";
        $scope.strategyTemplate.strategyDescription = "";
        $scope.strategyTemplate.strategyState = "";
    };

    $scope.addStrategy = function () {
        $scope.strategyTemplate.valid = true;
        if ($scope.strategyTemplate.strategyName.length == 0) {
            $scope.strategyTemplate.valid = false;
            $scope.strategyTemplate.error = "名称不能为空";
            return false;
        }
        if ($scope.strategyTemplate.featureList.length == 0) {
            $scope.strategyTemplate.valid = false;
            $scope.strategyTemplate.error = "任务不能为空";
            return false;
        }
        var strategyInstance = new anicloud.sunny.model.StrategyInstance(
            $scope.strategyTemplate.strategyId,
            $scope.strategyTemplate.strategyName,
            null,
            null,
            jsonClone($scope.strategyTemplate.featureList));

        StrategyService.saveStrategies(strategyInstance, function(data) {
            if (data.status == "success") {
                console.log("start strategy:");
                console.log(data.strategy);
                $rootScope.strategies.push(jsonClone(data.strategy));
            } else if(data.status == "error") {
                console.error("add strategy error: ");
                console.error(data.message);
            }
        });
        return true;
    };

    $scope.deleteStrategy = function (index, strategy) {
        StrategyService.deleteStrategy(strategy, function(data) {
            if (data.status == "success") {
                console.log("delete strategy:");
                console.log(data.strategy);
                $rootScope.strategies.splice(index, 1);
            } else if(data.status == "error") {
                console.error("add strategy error: ");
                console.error(data.message);
            }
        });
        return true;
    };

    $scope.resumeStrategy = function (index, strategy) {
        StrategyService.operateStrategy(strategy.strategyId, "resume", function(data) {
            if (data.status == "success") {
                console.log("resume strategy:");
                console.log(data.strategy);
                $rootScope.strategies.splice(index, 1, data.strategy);
            } else if(data.status == "error") {
                console.error("resume strategy error: ");
                console.error(data.message);
            }
        });
        return true;
    };

    $scope.stopStrategy = function (index, strategy) {
        StrategyService.operateStrategy(strategy.strategyId, "stop", function(data) {
            if (data.status == "success") {
                console.log("stop strategy:");
                console.log(data.strategy);
                $rootScope.strategies.splice(index, 1, data.strategy);
            } else if(data.status == "error") {
                console.error("resume strategy error: ");
                console.error(data.message);
            }
        });
        return true;
    };

    $scope.pauseStrategy = function (index, strategy) {
        StrategyService.operateStrategy(strategy.strategyId, "pause", function(data) {
            if (data.status == "success") {
                console.log("pause strategy:");
                console.log(data.strategy);
                $rootScope.strategies.splice(index, 1, data.strategy);
            } else if(data.status == "error") {
                console.error("pause strategy error: ");
                console.error(data.message);
            }
        });
        return true;
    };
    //
    var jsonClone = function (obj) {
        return JSON.parse(JSON.stringify(obj));
    };

}