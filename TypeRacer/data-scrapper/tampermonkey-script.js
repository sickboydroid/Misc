// ==UserScript==
// @name         TypeRacer Statistics Extractor
// @namespace    http://tampermonkey.net/
// @version      2024-12-10
// @description  try to take over the world!
// @author       sickboy
// @match        https://data.typeracer.com/pit/*
// @icon         https://www.google.com/s2/favicons?sz=64&domain=typeracer.com
// @grant        GM_setValue
// @grant        GM_getValue
// ==/UserScript==

/**
 * HOW TO USE:
 * - Navigate to the history page similar to https://data.typeracer.com/pit/race_history?user=sickboydroid
 * - If this script is enabled it will automatically start storing race history
 * - Results will printed in console at the end
 */

function parseRows() {
  const rows = document.querySelectorAll(".Scores__Table__Row");
  for (const row of rows) {
    const curRace = [];
    const cells = row.querySelectorAll("div");
    for (let i = 0; i < cells.length; i++) {
      curRace.push(cells[i].innerText);
    }
    races.push(curRace);
  }
  GM_setValue("races", races);
}

function main() {
  parseRows();
  let links = document.querySelectorAll("span a");
  console.log(links);
  if (links.length === 0 || (links.length === 1 && links[0].innerHTML.includes("newer"))) {
    console.log("No link found, assuming parsing completed");
    console.log(JSON.stringify(races));
    return;
  }
  let link = null;
  if (links.length === 1) {
    link = links[0];
  } else {
    link = links[1];
  }
}

// Since I am lazy, you need to modify this portion to reset
// races if it didn't work for the first time
if (GM_getValue("isFirstTime", true)) {
  GM_setValue("isFirstTime", false);
  GM_setValue("races", []);
}

let races = GM_getValue("races", null);
console.log("Total accumulated races: ", races.length);
setTimeout(main, 2500); // let the page load correctly
