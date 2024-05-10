using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Player : MonoBehaviour {

    private Animator animator;
    private SpriteRenderer spriteRenderer;
    private Rigidbody2D rigidBody2D;
    private const string ANIM_MOVING = "Moving";
    private const string TAG_GROUND = "Ground";
    private const string TAG_ENEMY = "Enemy";
    private bool isGrounded = true;
    private float speedForce = 20f;
    public float jumpForce = 15f;
    private float movementX = 0f;

    void Start() {
        animator = GetComponent<Animator>();
        spriteRenderer = GetComponent<SpriteRenderer>();
        rigidBody2D = GetComponent<Rigidbody2D>();
    }

    void Update() {
        //MovePlayer();
        AnimatePlayerMovement();
        JumpPlayer(Input.GetButtonDown("Jump"));
        transform.position += new Vector3(movementX, 0, 0) * speedForce * Time.deltaTime;
    }

    private void AnimatePlayerJump() {
        animator.SetBool("Jump", true);
    }

    public void JumpPlayer(bool jump) {
        if (!(isGrounded && jump))
            return;
        rigidBody2D.velocity = new Vector2(rigidBody2D.velocity.x, 9.8f + jumpForce);
        AnimatePlayerJump();
        isGrounded = false;
        Debug.Log("Jumped");
    }

    public void MovePlayer() {
       // movementX = Input.GetAxisRaw("Horizontal");
        MovePlayer(movementX);
    }

    public void MovePlayer(float movementX) {
        this.movementX = movementX;
    }

    private void AnimatePlayerMovement() {
        // movement
        if (movementX > 0) {
            animator.SetBool(ANIM_MOVING, true);
            spriteRenderer.flipX = false;
        } else if (movementX < 0) {
            animator.SetBool(ANIM_MOVING, true);
            spriteRenderer.flipX = true;
        } else {
            animator.SetBool(ANIM_MOVING, false);
        }
    }

    private void OnCollisionEnter2D(Collision2D collision) {
        if (collision.collider.CompareTag(TAG_GROUND)) {
            isGrounded = true;
            animator.SetBool("Jump", false);
        }
        if (collision.collider.CompareTag(TAG_ENEMY)) {
            Destroy(gameObject);
        }
    }

    private void OnTriggerEnter2D(Collider2D collision) {
        if (collision.GetComponent<Collider2D>().CompareTag(TAG_ENEMY)) {
            Destroy(gameObject);
        }
    }
}
