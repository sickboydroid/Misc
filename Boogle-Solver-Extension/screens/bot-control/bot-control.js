const botStatusOn_span = document.querySelector(".bot-status#on");
const botStatusOff_span = document.querySelector(".bot-status#off");
const toggleBot_btn = document.querySelector("#toggle-bot");
const [currentTab] = await chrome.tabs.query({ active: true, currentWindow: true });
let isBotRunning = false;

// listeners
chrome.runtime.onMessage.addListener(chromeMessagesListener);
toggleBot_btn.onclick = toggleBotListener;

requestBotData();

async function requestBotData() {
  await chrome.tabs.sendMessage(currentTab.id, { action: "bot-data" });
}

function chromeMessagesListener(request, sender, response) {
  switch (request.action) {
    case "bot-data":
      answerSpeed_input.value = request.answerSpeed;
      answerSpeed_input.dispatchEvent(new Event("input"));
      isBotRunning = request.isBotRunning;
      updateBotStatusSpans();
      break;
  }
}

async function toggleBotListener() {
  isBotRunning = !isBotRunning;
  updateBotStatusSpans();
  await chrome.tabs.sendMessage(currentTab.id, { action: "toggle-bot" });
}

function updateBotStatusSpans() {
  botStatusOn_span.setAttribute("hidden", null);
  botStatusOff_span.setAttribute("hidden", null);
  if (isBotRunning) botStatusOn_span.removeAttribute("hidden");
  else botStatusOff_span.removeAttribute("hidden");
}
