using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class HomeAndRestartClickHandler : MonoBehaviour {
    public void restart() {
        Debug.Log("Restart");
        SceneManager.LoadScene("Gameplay");
    }

    public void home() {
        SceneManager.LoadScene("MainMenu");
        Debug.Log("Home");
    }
}
