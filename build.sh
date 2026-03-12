#make a better script

while getopts :br OPTION; do
    if [ "${OPTION}" = 'b' ];then
	docker image rm ing/primo
	docker build -t ing/primo .
    elif [ "${OPTION}" = 'r' ];then
	docker run --rm -ti -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix -p 20:2222 ing/primo 
    fi
done
if [ "$OPTION" = "?" ];then
    echo 'usage: -b to build -r to run'
fi
