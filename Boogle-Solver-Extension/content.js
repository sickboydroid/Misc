class Node {
  constructor(eow = false) {
    this.children = Array(26);
    this.eow = false;
  }
}

class Trie {
  constructor() {
    this.root = new Node();
    this.codePointOfA = 97;
  }

  insert(word) {
    word = word.map(e => this.toIndex(e));
    let root = this.root;
    for (let i = 0; i < word.length; i++) {
      if (!root.children[word[i]]) root.children[word[i]] = new Node();
      root = root.children[word[i]];
    }
    root.eow = true;
  }

  isValidPrefixOrWord(word) {
    word = word.map(e => this.toIndex(e));
    let root = this.root;
    let isValidPrefix = true;
    for (let i = 0; i < word.length; i++) {
      if (!root.children[word[i]]) {
        isValidPrefix = false;
        break;
      }
      root = root.children[word[i]];
    }
    return { isValidPrefix, isValidWord: isValidPrefix && root.eow };
  }

  toIndex(char) {
    return char.codePointAt(0) - this.codePointOfA;
  }

  toChar(index) {
    return String.fromCodePoint(index + this.codePointOfA);
  }
}

const trie = generateTrie(getWords());
waitForGame();

async function waitForGame() {
  await sleep(1500);
  if (canPlay()) {
    for (let r = 0; r < 4; r++) {
      for (let c = 0; c < 4; c++) {
        await play(buildBoard(4), r, c);
      }
    }
  } else requestAnimationFrame(waitForGame);
}

function canPlay() {
  return !!document.querySelector("#game-wrap > div.board-wrap");
}

async function play(board, r = 0, c = 0, vis = new Map(), prefix = []) {
  const cellKey = `${r} ${c}`;
  if (r < 0 || c < 0 || r >= board.length || c >= board.length || vis.get(cellKey)) return;
  vis.set(cellKey, true);
  prefix.push([[r, c], board[r][c]]);
  const validity = trie.isValidPrefixOrWord(prefix.map(e => e[1]));
  if (!validity.isValidPrefix) return prefix.pop();
  else if (validity.isValidWord) await enterWord(prefix);
  await play(board, r + 1, c, vis, prefix);
  await play(board, r, c + 1, vis, prefix);
  await play(board, r + 1, c + 1, vis, prefix);
  await play(board, r - 1, c, vis, prefix);
  await play(board, r, c - 1, vis, prefix);
  await play(board, r - 1, c - 1, vis, prefix);
  vis.set(cellKey, false);
  prefix.pop();
}

function buildBoard(size) {
  const board = Array(size)
    .fill()
    .map(e => Array(size));
  for (let r = 0; r < size; r++) {
    for (let c = 0; c < size; c++) {
      board[r][c] = getCharAt(r, c);
    }
  }
  return board;
}

async function enterWord(coordinatedWord) {
  for (let i = 0; i < coordinatedWord.length; i++) {
    const [[r, c], letter] = coordinatedWord[i];
    performClick(getCellAt(r, c), "mousedown");
    performClick(getCellAt(r, c), "mouseup");
    await sleep(250);
  }
  document.dispatchEvent(new KeyboardEvent("keydown", { key: "Enter" }));
  await sleep(1000);
}

function performClick(element, eventName) {
  const box = element.getBoundingClientRect();
  const coordX = box.left + (box.right - box.left) / 2;
  const coordY = box.top + (box.bottom - box.top) / 2;
  element.dispatchEvent(
    new MouseEvent(eventName, {
      view: window,
      bubbles: true,
      cancelable: true,
      clientX: coordX,
      clientY: coordY,
      button: 0,
    })
  );
}

function getCharAt(r, c) {
  return getCellAt(r, c).innerText.toLowerCase();
}

function getCellAt(r, c) {
  const cellIdx = r * 4 + c + 2;
  return document.querySelector(`#letter-board > div:nth-child(${cellIdx})`);
}

function isValidPrefixOrWord(word) {
  // implement using trie
  return { validPrefix: true, validWord: false };
}

function generateTrie(words) {
  const trie = new Trie();
  for (const word of words) {
    trie.insert([...word]);
  }
  return trie;
}

function sleep(milliseconds) {
  return new Promise(resolve => {
    const interval = setInterval(() => {
      clearInterval(interval);
      resolve();
    }, milliseconds);
  });
}

function getWords() {
  // prettier-ignore
}
