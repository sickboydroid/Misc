const [currentTab] = await chrome.tabs.query({ active: true, currentWindow: true });
