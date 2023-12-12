window.addEventListener('DOMContentLoaded', function () {
    var canvas = document.getElementById('renderCanvas');
    var engine = new BABYLON.Engine(canvas, true);

    BABYLON.SceneLoader.Load("babylon/", "floorplan57_s0.5_h3_brick_4k.gltf", engine, function (newScene) {
            var camera = new BABYLON.ArcRotateCamera("Camera", Math.PI / 2, Math.PI / 2, 2, new BABYLON.Vector3(0, 0, 5), newScene);

            // camera.inputs.removeByType("ArcRotateCameraKeyboardMoveInput");

            camera.attachControl(canvas, true);
            newScene.activeCamera = camera;

            newScene.createDefaultLight();

            // 캔버스 배경 색상 변경
            newScene.clearColor = new BABYLON.Color3(20/255, 20/255, 20/255);

            engine.runRenderLoop(function () {
                newScene.render();
            });

            // SceneOptimizer를 이용한 성능 최적화
            var options = new BABYLON.SceneOptimizerOptions();
            options.addOptimization(new BABYLON.HardwareScalingOptimization(0, 1));

            var optimizer = new BABYLON.SceneOptimizerOptions(newScene, options);
            optimizer.start();

        }, function (progress) {
            console.log((progress.loaded / progress.total * 100) + "% loaded");
        }, function (scene, message, exception) {
            console.error("scene 로딩 중 에러", message, exception);
        }

    );
});
