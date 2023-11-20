window.addEventListener('DOMContentLoaded', function () {
    var canvas = document.getElementById('renderCanvas');
    var engine = new BABYLON.Engine(canvas, true);

    BABYLON.SceneLoader.Load("babylon/", "floorplan57_s0.5_h3_brick_4k.gltf", engine, function (newScene) {
        var camera = new BABYLON.ArcRotateCamera("Camera", Math.PI / 2, Math.PI / 2, 2, new BABYLON.Vector3(0, 0, 5), newScene);
        camera.attachControl(canvas, true);
        newScene.activeCamera = camera;

        newScene.createDefaultLight();

        engine.runRenderLoop(function () {
            newScene.render();
        });
    }, function (progress) {
        console.log((progress.loaded / progress.total * 100) + "% loaded");
    });
});
