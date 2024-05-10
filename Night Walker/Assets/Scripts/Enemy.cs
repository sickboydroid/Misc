using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Enemy : MonoBehaviour {
    public float speed = 5f;
    private Rigidbody2D myRigidBody;
    private const string TAG_COLLECTOR = "Collector";
    void Start() {
        myRigidBody = GetComponent<Rigidbody2D>();
    }

    void FixedUpdate() {
        myRigidBody.velocity = new Vector2(speed, myRigidBody.velocity.y);
    }
}
