// ==UserScript==
// @name         New Userscript
// @namespace    http://tampermonkey.net/
// @version      2024-12-10
// @description  try to take over the world!
// @author       You
// @match        https://play.typeracer.com/
// @icon         https://www.google.com/s2/favicons?sz=64&domain=typeracer.com
// @grant        none
// ==/UserScript==

/**
 * PaGARhsI => 1st correct
 * PaGARhsI fbawLOkL =>
 * no class => typing
 *.swtBYkWW > span:nth-child(3)
 */
async function main() {
  while (!getInputBox()) {
    console.log("No input box found, sleeping for 1 sec");
    await sleep(1000);
  }
  let inputBox = getInputBox();
  const test = getTextToType();
  for (let char of test) {
    console.log("Writing ", char);
    sendKeyEvent(inputBox, char);
    await sleep(500);
  }
  console.log("Race completed");
}

function getInputBox() {
  const inputBox = document.querySelector(".txtInput");
  if (!inputBox || inputBox.hasAttribute("disabled")) return null;
  return inputBox;
}

function getTextToType() {
  const span = document.querySelector(".swtBYkWW > span:nth-child(3)");
  return span.innerHTML;
}

function sendKeyEvent(element, key) {
  const event = new KeyboardEvent("keydown", { key });
  element.dispatchEvent(event);
}

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

console.log("Script inititated");

main();
