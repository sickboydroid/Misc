using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MyCamera : MonoBehaviour {
    private const string TAG_PLAYER = "Player";
    public GameObject leftPivot;
    public GameObject rightPivot;
    private Transform playerTransform;
    private const float MAX_CAMERA_X = 84.25f;
    private const float MIN_CAMERA_X = -82.5f;

    void Start() {
        playerTransform = GameObject.FindWithTag(TAG_PLAYER).transform;
    }

    void LateUpdate() {
        if (!playerTransform)
            return;
        float cameraNewX = playerTransform.position.x;
        if (cameraNewX < MIN_CAMERA_X) {
            cameraNewX = MIN_CAMERA_X;
        } else if (cameraNewX > MAX_CAMERA_X) {
            cameraNewX = MAX_CAMERA_X;
        }
        transform.position = new Vector3(cameraNewX, transform.position.y, transform.position.z);
    }
}
