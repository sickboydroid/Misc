movieSearch = document.querySelector("#movie-search-field")
searchResults = document.querySelector("#search-results")

movieSearch.addEventListener('input', async function() {
    respo = await fetch("/search?q="+movieSearch.value)
    results = await respo.json()
    html = ''
    results['result'].forEach(element => html += `<li><a href="/open?title=${encodeURIComponent(element[0])}">${element[0]}</li>`);
    searchResults.innerHTML = html
 })