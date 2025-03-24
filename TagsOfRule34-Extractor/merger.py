with open("tags.html", "w") as fTags:
    tagsContent = ""
    for i in range(1, 73):
        with open("res" + str(i), "r") as fRes:
            print("reading: " + str(i))
            html = fRes.read()
            tagsContent += "\n\n" + html
    fTags.write(tagsContent)
    fTags.close()
