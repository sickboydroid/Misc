using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class GameManager : MonoBehaviour {
    public GameObject[] players;
    public static GameManager instance;
    public int selectedCharacter = 0;
    public const int CHARACTER_WHITE = 0;
    public const int CHARACTER_BLACK = 1;
    private void Awake() {
        if (instance == null) {
            instance = this;
            DontDestroyOnLoad(gameObject);
        } else {
            Debug.Log("Destroying newly formed gamemanager object");
            Destroy(gameObject);
        }
    }

    private void OnEnable() {
        SceneManager.sceneLoaded += OnSceneLoaded;
    }
    private void OnDisable() {
        SceneManager.sceneLoaded -= OnSceneLoaded;
    }

    private void OnSceneLoaded(Scene scene, LoadSceneMode mode) {
        if (scene.name == "GamePlay") {
            Debug.Log("Scene loaded. Instantiating player...");
            Instantiate(players[selectedCharacter]);
            Debug.Log("Player instantiated");
        }
    }
}
