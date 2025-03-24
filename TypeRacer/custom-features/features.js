const HOME_SCREEN = 0;
const RACE_LIVE_SCREEN = 1;
const RACE_COMPLETE_SCREEN = 2;
const PRACTICE_COMPLETE_SCREEN = 3;
const UNKNOWN_SCREEN = 4;
const ENTER_RACE_BUTTON_QUERY = "#gwt-uid-1 > a";
const LEAVE_RACE_BUTTON_QUERY = "a.raceLeaveLink";
const NEXT_RACE_BUTTON_QUERY = "a.raceAgainLink";

function hideUnwantedFeatures() {
  const unlockPremiumRemainderBanner = document.querySelector("div.createAccountLeaderboardPrompt");
  const stats = document.querySelector("div.podContainer.medium.statsView");
  if (unlockPremiumRemainderBanner) unlockPremiumRemainderBanner.style.display = "none";
  if (stats) stats.style.display = "none";

  // hide cursor while on writing portion
  if (getScreenType() === RACE_LIVE_SCREEN)
    document.querySelector("table tbody td  table.inputPanel tbody td").style.cursor = "none";
  document.querySelector("#gwt-uid-20 > table > tbody > tr:nth-child(2) > td > table");
}

function getScreenType() {
  const enterRaceBtn = document.querySelector(ENTER_RACE_BUTTON_QUERY);
  const leaveRaceBtn = document.querySelector(LEAVE_RACE_BUTTON_QUERY);
  const nextRaceBtn = document.querySelector(NEXT_RACE_BUTTON_QUERY);
  const tryAgainBtn = getTryAgainButton();
  const saveRaceBtn = getSaveRaceButton();
  if (isVisible(enterRaceBtn)) return HOME_SCREEN;
  else if (isVisible(leaveRaceBtn) && !isVisible(nextRaceBtn)) return RACE_LIVE_SCREEN;
  else if (isVisible(tryAgainBtn) && isVisible(saveRaceBtn)) return PRACTICE_COMPLETE_SCREEN;
  else if (isVisible(nextRaceBtn) && isVisible(tryAgainBtn)) return RACE_COMPLETE_SCREEN;
  else return UNKNOWN_SCREEN;
}

function getAccuracy() {
  const label = [...document.querySelectorAll("td")].find(e => e.textContent === "Accuracy:");
  if (label) {
    const value = label.nextElementSibling;
    if (value) return Number(value.textContent.substring(0, value.textContent.length - 1));
  }
  return null;
}

function getTryAgainButton() {
  return Array.from(document.querySelectorAll("a")).find(a => a.textContent === "Try again?");
}

function getSaveRaceButton() {
  return Array.from(document.querySelectorAll("a")).find(a => a.textContent === "Save");
}

function isVisible(element) {
  return element && element.style.display !== "none";
}

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

/*************** CLICKERS ***************/
function clickTryAgain() {
  const tryAgainBtn = getTryAgainButton();
  tryAgainBtn.click();
}

function clickNewRace() {
  const nextRaceBtn = document.querySelector(NEXT_RACE_BUTTON_QUERY);
  nextRaceBtn.click();
}

function clickEnterNewRace() {
  const enterRaceBtn = document.querySelector(ENTER_RACE_BUTTON_QUERY);
  enterRaceBtn.click();
}

async function exitAndStartNewRace() {
  const leaveRaceBtn = document.querySelector(LEAVE_RACE_BUTTON_QUERY);
  leaveRaceBtn.click();
  while (getScreenType() !== HOME_SCREEN) await sleep(500);
  clickEnterNewRace();
}

/*************** LISTENER HANDLERS ***************/
function addRequiredListener() {
  removeAllListeners();
  let listener = null;
  const screenType = getScreenType();
  if (screenType === HOME_SCREEN) {
    console.debug("Home Screen");
    listener = homeScreenListener;
  } else if (screenType === RACE_LIVE_SCREEN) {
    console.debug("Race Live");
    listener = raceLiveListener;
  } else if (screenType === RACE_COMPLETE_SCREEN) {
    console.debug("Race complete screen");
    listener = raceCompleteListener;
  } else if (screenType === PRACTICE_COMPLETE_SCREEN) {
    console.debug("Practice complete");
    listener = practiceCompleteListener;
  } else {
    console.debug("Unknown Screen");
    return;
  }
  document.addEventListener("keydown", listener);
}

function removeAllListeners() {
  document.removeEventListener("keydown", homeScreenListener);
  document.removeEventListener("keydown", raceLiveListener);
  document.removeEventListener("keydown", raceCompleteListener);
  document.removeEventListener("keydown", practiceCompleteListener);
}

function homeScreenListener(event) {
  if (event.key !== "Enter") return;
  console.debug("homeScreenListener fired");
  clickEnterNewRace();
}

function raceLiveListener(event) {}

function raceCompleteListener(event) {
  if (event.key !== "Enter") return;
  console.debug("raceCompleteListener fired");
  if (getAccuracy() < 98) clickTryAgain();
  else clickNewRace();
}

function practiceCompleteListener(event) {
  if (event.key !== "Enter") return;
  console.debug("practiceCompleteListener fired");
  if (getAccuracy() < 98) clickTryAgain();
  else exitAndStartNewRace();
}

function main() {
  // keep updating listener as soon as DOM changes
  const observer = new MutationObserver(() => {
    hideUnwantedFeatures();
    addRequiredListener();
  });

  observer.observe(document.body, { childList: true, subtree: true });
}

main();
