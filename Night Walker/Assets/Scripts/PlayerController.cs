using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerController : MonoBehaviour {
    public Player player;

    private void Start() {
        player = GameObject.FindGameObjectWithTag("Player").GetComponent<Player>();
    }

    public void MoveLeft(bool move) {
        if (!player)
            return;
        if (move)
            player.MovePlayer(-1f);
        else
            player.MovePlayer(0f);
    }

    public void MoveRight(bool move) {
        if(!player)
            return;
        if (move)
            player.MovePlayer(1f);
        else
            player.MovePlayer(0);
     }

    public void Jump() {
        player.JumpPlayer(true);
    }
}
