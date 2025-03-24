const tagLinks = [...document.querySelectorAll("div.tags_content  div.item a")];
const res = [];
for (const anchor of tagLinks) {
  const link = anchor.getAttribute("href");
  const innerText = anchor.innerText;
  const lastSpaceIdx = innerText.lastIndexOf(" ");
  const name = innerText.substring(0, lastSpaceIdx).trim();
  const count = parseInt(innerText.substring(lastSpaceIdx, innerText.length).trim());
  res.push(createTag(name, count, link));
}

// tags_content item a => extract href and innerText (includes name svg count)

function createTag(name, count, link) {
  return { name, count, link };
}

console.log(JSON.stringify(res));
