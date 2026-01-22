sudo docker run -d --name srs   -p 1935:1935   -p 1985:1985   -p 8082:8080   -p 8000:8000   -v $(pwd)/srs.conf:/usr/local/srs/conf/srs.conf   ossrs/srs


sudo docker run -d --name mediamtx \
  -e MTX_RTSPTRANSPORTS=tcp \
  -e MTX_WEBRTCADDITIONALHOSTS=192.168.41.227 \
  -p 8554:8554 \
  -p 1935:1935 \
  -p 8888:8888 \
  -p 8889:8889 \
  -p 8890:8890/udp \
  -p 8189:8189/udp \
  bluenviron/mediamtx:1

-p 8554:8554     # RTSP
-p 1935:1935     # RTMP
-p 8888:8888     # HLS / HTTP
-p 8889:8889     # WebRTC (HTTP 信令)
-p 8890:8890/udp # WebRTC ICE
-p 8189:8189/udp # WebRTC RTP


ffmpeg -re -i video.mov \
  -c:v libx264 \
  -profile:v baseline \
  -level 3.1 \
  -pix_fmt yuv420p \
  -bf 0 \
  -g 30 \
  -keyint_min 30 \
  -tune zerolatency \
  -an \
  -rtsp_transport tcp \
  -f rtsp rtsp://192.168.41.227:8554/live/mystream
