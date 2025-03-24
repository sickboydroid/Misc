i=1
while [ $i -lt 73 ]
do
	curl -o res$i "https://rule34video.com/tags/?mode=async&function=get_block&block_id=list_tags_tags_list&section=All&from=$i"
	i=$(($i+1))
done
