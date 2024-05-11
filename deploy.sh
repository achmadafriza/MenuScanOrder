rsync -hvrPt -e "ssh" ./deployment/menuscanorder.conf s4728788@infs3202-c5f09018.zones.eait.uq.edu.au:/etc/nginx/frameworks-available/menuscanorder.conf
rsync -hvrPt -e "ssh" ./deployment/menuscanorder.service s4728788@infs3202-c5f09018.zones.eait.uq.edu.au::/lib/systemd/system/menuscanorder.service
rsync -hvrPt -e "ssh" ./build/libs/project-0.0.1-SNAPSHOT.jar s4728788@infs3202-c5f09018.zones.eait.uq.edu.au:/var/www/app/app.jar
rsync -hvrPt -e "ssh" ./deployment/application.properties s4728788@infs3202-c5f09018.zones.eait.uq.edu.au:/var/www/app/application.properties