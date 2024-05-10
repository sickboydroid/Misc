using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class EnemySpawner : MonoBehaviour
{
    public GameObject[] enimies;
    public Transform left, right;
    // Start is called before the first frame update
    void Start()
    {
        StartCoroutine(SpawnMonster());
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    IEnumerator SpawnMonster() {
        while(true) {
            yield return new WaitForSeconds(Random.Range(3, 7));
            int randomSide = Random.Range(0, 2);
            int randomIndex = Random.Range(0, enimies.Length);
            int randomVelocity = Random.Range(5, 12);
            GameObject spawnedMonster = Instantiate(enimies[randomIndex]);
            if(randomSide == 0) { // left
                spawnedMonster.transform.position = left.transform.position;
                spawnedMonster.GetComponent<Enemy>().speed = randomVelocity;
            } else if(randomSide == 1) { // right
                spawnedMonster.transform.position = right.transform.position;
                spawnedMonster.GetComponent<SpriteRenderer>().flipX = true;
                spawnedMonster.GetComponent<Enemy>().speed = -randomVelocity;
            }
        }
    }
}
