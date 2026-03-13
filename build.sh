#make a better script
NETWORK='ing-net'

while getopts :br OPTION; do
    if [ "${OPTION}" = 'b' ];then
	docker network rm "${NETWORK}"
	docker image rm ing/primo
	docker network create "${NETWORK}" --subnet 10.0.0.0/24
	docker build -t ing/primo .
    elif [ "${OPTION}" = 'r' ];then
	docker run --rm -ti -e DISPLAY=$DISPLAY \
	    --mount type=bind,src="./Codice_Sorgente",dst="/app" \
	    --network "${NETWORK}" \
	    --ip 10.0.0.2 \
	    -v /tmp/.X11-unix:/tmp/.X11-unix -p 20:2222 ing/primo 
    fi
done
if [ "$OPTION" = "?" ];then
    echo 'usage: -b to build -r to run'
fi
