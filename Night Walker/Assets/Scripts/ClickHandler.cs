using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class ClickHandler : MonoBehaviour {
    private const string SCENE_GAMEPLAY = "Gameplay";

    public void StartGameUsingWhite() {
        GameManager.instance.selectedCharacter = GameManager.CHARACTER_WHITE;
        SceneManager.LoadScene("Gameplay");
    }

    public void StartGameUsingBlack() {
        GameManager.instance.selectedCharacter = GameManager.CHARACTER_BLACK;
        SceneManager.LoadScene("Gameplay");
    }
}
