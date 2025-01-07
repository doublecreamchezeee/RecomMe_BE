Docker build and push:
- docker build . -t tuantruong03/recomme_be:latest
- docker push tuantruong03/recomme_be:latest
Docker run:
- docker pull tuantruong03/recomme_be
- docker run -p 8888:8888 tuantruong03/recomme_be 